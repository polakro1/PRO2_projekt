package models.chatClients.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.chatClients.Message;
import models.chatClients.ChatClient;
import models.chatClients.fileOperations.LocalDatetimeDeserializer;
import models.chatClients.fileOperations.LocalDatetimeSerializer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

// http://fimuhkpro22021.aspifyhost.cz/swagger/index.html
public class ApiChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessagesChanged = new ArrayList<>();

    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";
    private String token;
    private Gson gson;

    public ApiChatClient() {
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDatetimeSerializer()).registerTypeAdapter(LocalDateTime.class, new LocalDatetimeDeserializer()).create();

        Runnable refreshData = () -> {
            Thread.currentThread().setName("RefreshData");
            try {
                while (true) {
                    if (isAuthenticted()) {
                        refreshLoggedUsers();
                        refreshMessages();
                    }
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread refreshDataThread = new Thread(refreshData);
        refreshDataThread.start();
    }

    @Override
    public void sendMessage(String text) {
        try {
            SendMessageRequest msgRequest = new SendMessageRequest(token, text);
            String url = BASE_URL + "/api/Chat/SendMessage";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(gson.toJson(msgRequest), "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204) {
                refreshMessages();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("new messsage " + text);
    }

    @Override
    public void login(String userName) {
        try {
            String url = BASE_URL + "/api/Chat/Logim";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\"" + userName  + "\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 200) {
                token = EntityUtils.toString(response.getEntity());
                token = token.replace("\"", "".trim());

                loggedUser = userName;
                refreshLoggedUsers();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        try {
            String url = BASE_URL + "/api/Chat/Logout";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\"" + token  + "\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204) {
                token = null;
                loggedUser = null;
                loggedUsers = new ArrayList<>();
                raiseEventLoggedUsersChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean isAuthenticted() {
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
    private void refreshLoggedUsers() {
        try {
            String url = BASE_URL + "/api/Chat/GetLoggedUsers";
            HttpGet get = new HttpGet(url);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200) {
                String resultJson = null;
                resultJson = EntityUtils.toString(response.getEntity());

                loggedUsers = gson.fromJson(resultJson, new TypeToken<ArrayList<String>>(){}.getType());

                raiseEventLoggedUsersChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshMessages() {
        try {
            String url = BASE_URL + "/api/Chat/GetMessages";
            HttpGet get = new HttpGet(url);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200) {
                String resultJson = EntityUtils.toString(response.getEntity());

                messages = gson.fromJson(resultJson, new TypeToken<ArrayList<Message>>(){}.getType());

                raiseEventMessagesChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
