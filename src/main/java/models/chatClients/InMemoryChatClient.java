package models.chatClients;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessagesChanged = new ArrayList<>();

    public InMemoryChatClient() {
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        raiseEventMessagesChanged();

        System.out.println("new messsage " + text);
    }

    @Override
    public void login(String username) {
        loggedUser = username;
        loggedUsers.add(username);
        raiseEventLoggedUsersChanged();
        addSystemMessage(Message.USER_LOGGED_IN, username);

        System.out.println("user logged in " + username);
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        raiseEventLoggedUsersChanged();
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);

        System.out.println("user logged out " + loggedUser);
    }

    @Override
    public Boolean isAuthenticted() {
        System.out.println("is authenticated: " + (loggedUser != null));
        return loggedUser != null;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedUsersChanged.add(toAdd);
    }

    @Override
    public void addActionListenerMessagesChanged(ActionListener toAdd) {
        listenersMessagesChanged.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged () {
        for (ActionListener al : listenersLoggedUsersChanged) {
            al.actionPerformed(new ActionEvent(this, 1, "usersChanged"));
        }
    }

    private void raiseEventMessagesChanged () {
        for (ActionListener al : listenersMessagesChanged) {
            al.actionPerformed(new ActionEvent(this, 1, "messagesChanged"));
        }
    }

    private void addSystemMessage(int type, String author) {
        messages.add(new Message(type, author));
        raiseEventMessagesChanged();
    }
}
