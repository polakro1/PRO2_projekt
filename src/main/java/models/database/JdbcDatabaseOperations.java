package models.database;

import models.chatClients.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseOperations implements DatabaseOperations {
    private final Connection connection;

    public JdbcDatabaseOperations(String driver, String url) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        this.connection = DriverManager.getConnection(url);
    }
    @Override
    public void addMessage(Message message) {

        try {
            String sql = "INSERT INTO Chat (author, text, created)"
                    + "VALUES ("
                        + "'" + message.getAuthor() + ","
                        + "'" + message.getText() + ","
                        + "'" + Timestamp.valueOf(message.getCreated()) + "'"
                    + ")";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Message> getMessages() {
        try {
            String sql = "SELECT Author, Text, Created FROM Chat";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<Message> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(new Message(resultSet.getString("Author"),
                        resultSet.getString("Text")));
            }

            return messages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
