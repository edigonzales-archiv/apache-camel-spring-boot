package ch.so.agi.apachecamel.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ch.so.agi.apachecamel.dao.ExecutionMessageDAOImpl;
import ch.so.agi.apachecamel.models.ExecutionMessage;

@Controller
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    ExecutionMessageDAOImpl ExecutionMessageDAO;
    
    @RequestMapping(value="/rss", method=RequestMethod.GET, produces={MediaType.ALL_VALUE})
    public ResponseEntity<?> getXmlExtractById () throws Exception {
        
        List<ExecutionMessage> messages = ExecutionMessageDAO.getExecutionMessages();
        log.info(messages.get(0).getDatasetname());
        
        return ResponseEntity.ok("fubar");
    }
}
