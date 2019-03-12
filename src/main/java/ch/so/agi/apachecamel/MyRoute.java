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
//    Processor myProcessor = new MyProcessor();
//    Predicate myPredicate = new MyPredicate();
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

    @Override
    public void configure() throws Exception {      
        ilivalidatorPredicate.setSettings();                                
        
        from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\gb2av\\?fileName=VOLLZUG_SO0200002401_1531_20170420081516.zip&password="+ftpPwdInfogrips+"&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&readLock=changed&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
        .to("file://"+pathToDownloadFolder)
        .split(new ZipSplitter())
        .streaming().convertBodyTo(String.class) //What happens when it gets huge? Is 'String.class' a problem? 
            .choice()
                .when(body().isNotNull())
                    .to("file://"+pathToUnzipFolder)
            .end()
        .end();
        
        from("file:///Users/stefan/Downloads/output_unzipped/?readLock=changed&noop=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
            .choice()
                .when(ilivalidatorPredicate).to("file:///Users/stefan/Downloads/output_unzipped_ready/")
                .otherwise().to("file:///Users/stefan/Downloads/output_error/")
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
