package ch.so.agi.apachecamel.dao;

import java.util.List;

import ch.so.agi.apachecamel.models.ExecutionMessage;

public interface ExecutionMessageDAO {
    List<ExecutionMessage> getExecutionMessages();
}
