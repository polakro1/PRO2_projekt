package models.chatClients;

import java.awt.event.ActionListener;
import java.util.List;

public interface ChatClient {
    void sendMessage(String text);
    void login(String username);
    void logout();
    Boolean isAuthenticted();
    List<String> getLoggedUsers();
    List<Message> getMessages();
    void addActionListenerLoggedUsersChanged(ActionListener toAdd);
    void addActionListenerMessagesChanged(ActionListener toAdd);
}
