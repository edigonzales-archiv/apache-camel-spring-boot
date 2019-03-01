package ch.so.agi.apachecamel;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import ch.ehi.basics.settings.Settings;
import org.interlis2.validator.Validator;

public class IlivalidatorPredicate implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        Settings settings = new Settings();
        settings.setValue(Validator.SETTING_ILIDIRS, Validator.SETTING_DEFAULT_ILIDIRS);

//        boolean valid = Validator.runValidation(inputFileName, settings);

        return false;
    }

}
