package models.chatClients.fileOperations;

import models.chatClients.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvChatFileOperations implements ChatFileOperations {
    private static final String MESSAGES_FILE = "./messages.csv";

    @Override
    public void writeMessage(List<Message> messages) {
        try (PrintWriter writer = new PrintWriter(MESSAGES_FILE);)
        {
            for (Message message:
                 messages) {
                String radek = message.getAuthor() + ";" + message.getText() + ";" + message.getCreated();
                writer.println(radek);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> readMessage() {
        List<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MESSAGES_FILE));) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                Message message = new Message(split[0], split[1], LocalDateTime.parse(split[2]));
                messages.add(message);
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
