package models.chatClients;

import models.Message;

import java.util.List;

public interface ChatClient {
    void sendMessage(String text);
    void login(String username);
    void logout();
    Boolean isAuthenticted();
    List<String> getLoggedUsers();
    List<Message> getMessages();
}
