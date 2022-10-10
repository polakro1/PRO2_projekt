import models.chatClients.ChatClient;
import models.chatClients.InMemoryChatClient;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {

        MainFrame window = new MainFrame(800, 600);

        //test();
    }

    private static void test() {
        ChatClient client = new InMemoryChatClient();

        client.login("Polak");

        client.sendMessage("Message 1");
        client.sendMessage("Hello");

        client.logout();
    }
}
