package ch.so.agi.apachecamel;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {
    Processor myProcessor = new MyProcessor();
    Predicate myPredicate = new MyPredicate();
    
    @Override
    public void configure() throws Exception {
//        from("file:///Users/stefan/tmp/data/?noop=true").to("file:///Users/stefan/Downloads/output/");
        from("file:///Users/stefan/tmp/data/?readLock=changed")
        .choice()
            .when(myPredicate).to("file:///Users/stefan/Downloads/output/")
        .otherwise()
            .to("file:///Users/stefan/Downloads/output2/")
        .end();
//        .process(myProcessor)
//        .to("file:///Users/stefan/Downloads/output/").end();
    }

}
