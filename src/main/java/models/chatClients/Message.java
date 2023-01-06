package models.chatClients;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Locale;

public class Message {
    @Expose(serialize = true, deserialize = true)
    private String author;
    @Expose(serialize = true, deserialize = true)
    private String text;
    @Expose(serialize = false, deserialize = true)
    private LocalDateTime created;

    public static final int USER_LOGGED_IN = 1;
    public static final int USER_LOGGED_OUT = 2;
    public static final String AUTHOR_SYSTEM = "System";
    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Message(String author, String text) {
        this.author = author;
        this.text = text;
        created = LocalDateTime.now();
    }

    public Message(String author, String text, LocalDateTime created) {
        this.author = author;
        this.text = text;
        this.created = created;
    }

    public Message(int type, String username) {
        this.author = AUTHOR_SYSTEM;
        if (type == USER_LOGGED_IN) {
            text = "user joined the chat " + username;
        }
        if(type == USER_LOGGED_OUT) {
            text = "user left the chat " + username;
        }
        created = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (author.toUpperCase(Locale.ROOT).equals(AUTHOR_SYSTEM.toUpperCase())) {
            return text + "\n";
        }

        String s = author + "[" + created + "]\n";
        s += text + "\n";
        return s;
    }
}
