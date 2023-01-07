package models.chatClients.fileOperations;

import models.chatClients.Message;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class XmlChatFileOperations implements ChatFileOperations{
    private static final String MESSAGES_FILE = "./messages.xml";
    @Override
    public void writeMessage(List<Message> messages) {
        DocumentFactory df = DocumentFactory.getInstance();
        Document doc = df.createDocument();
        Element root = df.createElement("messages");
        doc.add(root);

        for (Message message :
                messages) {
            Element elMessage = df.createElement("message");
            elMessage.addAttribute("author", message.getAuthor())
                    .addAttribute("text", message.getText())
                    .addAttribute("created", message.getCreated().toString());
            root.add(elMessage);
        }

        try (FileWriter writer = new FileWriter(MESSAGES_FILE)) {
            writer.write(doc.asXML());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> readMessage() {
        List<Message> messages = new ArrayList<>();
        DocumentFactory df = new DocumentFactory();
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(MESSAGES_FILE);
            List<Node> ndMessages = doc.selectNodes("//message");
            for (Node ndMessage :
                    ndMessages) {
                Element elMessage = (Element) ndMessage;
                Message message = new Message(elMessage.attributeValue("author"), elMessage.attributeValue("text"),
                        LocalDateTime.parse(elMessage.attributeValue("created")));
                messages.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
