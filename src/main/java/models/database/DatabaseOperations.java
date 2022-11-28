package models.database;

import models.chatClients.Message;

import java.util.List;

public interface DatabaseOperations {
    void addMessage(Message message);
    List<Message> getMessages();
}
