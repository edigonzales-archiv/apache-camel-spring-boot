package ch.so.agi.apachecamel;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class MyPredicate implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        System.out.println("Hallo Predicate");
        return true;
    }

}
