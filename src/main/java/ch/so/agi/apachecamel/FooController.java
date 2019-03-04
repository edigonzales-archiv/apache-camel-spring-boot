package ch.so.agi.apachecamel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FooController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @RequestMapping(value = "/version.txt", method = RequestMethod.GET)
    public void version() {
        log.info("*********HAAAAALOOOOO");
    }
}
