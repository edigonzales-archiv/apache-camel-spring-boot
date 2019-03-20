package ch.so.agi.apachecamel;

import java.io.File;
import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

@Component
public class MyRoute extends RouteBuilder {
    IlivalidatorPredicate ilivalidatorPredicate = new IlivalidatorPredicate();
    
    @Value("${app.ftpUserInfogrips}")
    private String ftpUserInfogrips;

    @Value("${app.ftpPwdInfogrips}")
    private String ftpPwdInfogrips;

    @Value("${app.ftpUrlInfogrips}")
    private String ftpUrlInfogrips;

    @Value("${app.idempotentFileUrl}")
    private String idempotentFileUrl;

    @Value("${app.pathToDownloadFolder}")
    private String pathToDownloadFolder;

    @Value("${app.pathToUnzipFolder}")
    private String pathToUnzipFolder;

    @Value("${app.pathToErrorFolder}")
    private String pathToErrorFolder;

    @Override
    public void configure() throws Exception {      
        ilivalidatorPredicate.setSettings(); // just a test                        
        
        // Download the zip files from the infogrips ftp and unzip the file into another directory.
        //from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\gb2av\\?fileName=VOLLZUG_SO0200002401_1531_20170420081516.zip&password="+ftpPwdInfogrips+"&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&readLock=changed&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
        from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\gb2av\\?password="+ftpPwdInfogrips+"&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&readLock=changed&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
        .to("file://"+pathToDownloadFolder)
        .split(new ZipSplitter())
        .streaming().convertBodyTo(String.class) // What happens when it gets huge? Is 'String.class' a problem? 
            .choice()
                .when(body().isNotNull())
                    .to("file://"+pathToUnzipFolder)
            .end()
        .end();
        
        // Validate the file with ilivalidator. If valid, import it into the database with the camel ili2pg component.
        // If not valid, copy file into an error directory.
        from("file://"+pathToUnzipFolder+"/?readLock=changed&noop=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        .setHeader("dataset", simple("${in.header.CamelFileName}"))
            .choice()
                .when(ilivalidatorPredicate).toD("ili2pg:import?dbhost=192.168.50.8&dbport=5432&dbdatabase=pub&dbschema=agi_gb2av&dbusr=ddluser&dbpwd=ddluser&dataset=${in.header.CamelFileName}")
                .otherwise().to("file://"+pathToErrorFolder)
        .end();        
    }
    
    @Bean
    public FileIdempotentRepository fileConsumerRepo() {
        FileIdempotentRepository fileConsumerRepo = null;
        try {
            fileConsumerRepo = new FileIdempotentRepository();
            fileConsumerRepo.setFileStore(new File(idempotentFileUrl));
            fileConsumerRepo.setCacheSize(5000);
            fileConsumerRepo.setMaxFileStoreSize(51200000);
        } catch (Exception e) {
            log.error("Caught exception inside Creating fileConsumerRepo ..." + e.getMessage());
        }
        if (fileConsumerRepo == null) {
            log.error("fileConsumerRepo == null ...");
        }
        return fileConsumerRepo;
    }
}
