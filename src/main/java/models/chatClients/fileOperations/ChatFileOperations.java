package models.chatClients.fileOperations;

import models.Message;

import java.util.List;

public interface ChatFileOperations {
    void writeMessage(List<Message> messages);
    List<Message> readMessage();
}
