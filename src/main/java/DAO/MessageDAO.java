package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
    /**
     *  Inserts a new message into the 'message' table.
     *  @return message that was created.
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkResultSet = preparedStatement.getGeneratedKeys();
            if(pkResultSet.next()) {
                int generatedMsgID = (int) pkResultSet.getLong(1);
                return new Message(generatedMsgID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all messages from the 'message' table.
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"), 
                        rs.getInt("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrieve a message from the 'message' table given its id.
     * @return message matching id.
     * */
    public Message getMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"), 
                        rs.getInt("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Delete a message from the 'message' table given its id.
     * @return the number of deleted messages (1 if successful, 0 otherwise).
     */
    public int deleteMessageByID (int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            int res = preparedStatement.executeUpdate();

            return res;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Update a message from the 'message' table given its id.
     * @return the number of updated statements.
     */
    public int updateMessageByID (Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text=? WHERE message_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());  
            preparedStatement.setInt(2, message.getMessage_id());

            int res = preparedStatement.executeUpdate();
            return res;

        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Retrieve all messages from 'message' table given by the posted_by user id. 
     * @return a list containing all messages by the selected user.
     */
    public List<Message> getAllMessagesByUser (int user) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, user);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"), 
                        rs.getInt("time_posted_epoch"));
                messages.add(message);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Confirm that the user represented by 'posted_by' column in the 'message' table
     * exists in the 'account' table
     * @return true if the account exists
     */
    public boolean confirmMessageAccount (int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, posted_by);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            
        }
        return false;
    }
}
