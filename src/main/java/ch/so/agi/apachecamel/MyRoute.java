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
//    Predicate ilivalidatorPredicate = new IlivalidatorPredicate();
    
    @Value("${app.ftpUserInfogrips}")
    private String ftpUserInfogrips;

    @Value("${app.ftpPwdInfogrips}")
    private String ftpPwdInfogrips;

    @Value("${app.ftpUrlInfogrips}")
    private String ftpUrlInfogrips;

    @Value("${app.idempotentFileUrl}")
    private String idempotentFileUrl;
    
    @Override
    public void configure() throws Exception {       
                        
        log.info("*************: " + ftpUserInfogrips);
        
        // ENV Variablen "direkt": {{env:ftpUserInfogrips}}
        from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\gb2av\\?fileName=VOLLZUG_SO0200002401_1531_20170420081516.zip&password="+ftpPwdInfogrips+"&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\gb2av\\?password={{env:ftpPwdInfogrips}}&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000")
        .to("file:///Users/stefan/Downloads/output/")
        .split(new ZipSplitter())
        .streaming().convertBodyTo(String.class) //org.apache.camel.InvalidPayloadException: No body available of type: java.io.File but has value: org.apache.camel.dataformat.zipfile.ZipInputStreamWrapper@14fa8459 of type: org.apache.camel.dataformat.zipfile.ZipInputStreamWrapper on: Message[]. Caused by: No type converter available to convert from type: org.apache.camel.dataformat.zipfile.ZipInputStreamWrapper to the required type: java.io.File with value org.apache.camel.dataformat.zipfile.ZipInputStreamWrapper@14fa8459. Exchange[ID-SemucChampey-local-1551902511781-0-3]. Caused by: [org.apache.camel.NoTypeConversionAvailableException - No type converter available to convert from type: org.apache.camel.dataformat.zipfile.ZipInputStreamWrapper to the required type: java.io.File with value org.apache.camel.dataformat.zipfile.ZipInputStreamWrapper@14fa8459]
            .choice()
                .when(body().isNotNull())
                    .to("file:///Users/stefan/Downloads/output_unzipped/")
            .end()
        .end().process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                log.info(exchange.getIn().getBody().getClass().toString());
                log.info(exchange.getIn().getBody().toString());
           }
        });
        
        // mit inline process-klasse body zeigen.
        
        // "Transaktion" geht verloren.
        // idempotentRepository....
//        from("file:///Users/stefan/Downloads/output_unzipped/")
//        .to("file:///Users/stefan/Downloads/output_unzipped_ready/")
//        .end();        
    }
    
    @Bean
    public FileIdempotentRepository fileConsumerRepo() {
        FileIdempotentRepository fileConsumerRepo = null;
        try {
            fileConsumerRepo = new FileIdempotentRepository();
            fileConsumerRepo.setFileStore(new File("/Users/stefan/tmp/dm01_shp_downloaded.txt"));
            fileConsumerRepo.setCacheSize(5000);
            fileConsumerRepo.setMaxFileStoreSize(51200000);

        } catch (Exception e) {
            log.error("############ Caught exception inside Creating fileConsumerRepo ..." + e.getMessage());
        }
        if (fileConsumerRepo == null) {
            log.error("############ fileConsumerRepo == null ...");
        }
        return fileConsumerRepo;
    }
}
