package ch.so.agi.apachecamel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyProcessor implements Processor {
    public MyProcessor() {}
    
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Hallo Welt.");

    }

}
