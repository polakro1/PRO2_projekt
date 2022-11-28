package models.database;

import models.chatClients.Message;

import java.sql.*;
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
            String sql = "INSERT INTO ChatMessages (author, text, created)"
                    + "VALUES ("
                        + "'" + message.getAuthor() + ","
                        + "'" + message.getText() + ","
                        + "'" + Timestamp.valueOf(message.getCreated()) + "'"
                    + ")";

            Statement statement = connection.createStatement();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Message> getMessage() {
        return null;
    }
}
