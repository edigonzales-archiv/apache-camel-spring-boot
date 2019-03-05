package ch.so.agi.apachecamel;

import java.io.File;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.dataformat.zipfile.ZipSplitter;

@Component
public class MyRoute extends RouteBuilder {
//    Processor myProcessor = new MyProcessor();
//    Predicate myPredicate = new MyPredicate();
//    Predicate ilivalidatorPredicate = new IlivalidatorPredicate();
    
    @Override
    public void configure() throws Exception {       
                        

        // TODO:
        // - download only 'VOLLZUG' (?)
        
        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\gb2av\\?password={{env:ftpPwdInfogrips}}&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000")
        .to("file:///Users/stefan/Downloads/output/")
        .split(new ZipSplitter())
        .streaming().convertBodyTo(String.class) 
            .choice()
                .when(body().isNotNull())
                    .to("file:///Users/stefan/Downloads/output_unzipped/")
            .end()
        .end();

//        .to("ili2pg://disableValiation=true");
//        .to("ili2pg://bar");
        
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
