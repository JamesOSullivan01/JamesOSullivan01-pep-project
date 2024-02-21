package DAO;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.Message;
import Util.ConnectionUtil;


public class MessageDAO {
    Connection connection = ConnectionUtil.getConnection();

    public Message getMessageById(int id) {
        try {
            String SQL = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                allMessages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMessages;
    }


    public Message createMessage(Message message){
        try{
            String SQL = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(SQL,  java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
            int generatedId = generatedKeys.getInt(1);
            message.setMessage_id(generatedId);
            return new Message(generatedId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }

        } catch(Exception e){
            e.printStackTrace();
        }
            return null;
}

    public Message deleteMessage(int message_id){
        Message message = null;
            try{
                String SQL = "SELECT * FROM message WHERE message.message_id = ?";
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setInt(1, message_id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    message = new Message(
                       rs.getInt("message_id") ,
                       rs.getInt("posted_by"), 
                       rs.getString("message_txt"),
                       rs.getLong("time_posted_epoch"));
                
                }
                String SQL2 = "DELETE FROM message WHERE message.message_id = ?";
                PreparedStatement ps2 = connection.prepareStatement(SQL2);
                ps2.setInt(1, message_id);
                int row_count = ps2.executeUpdate();
                
                if(row_count == 0){
                    message = null;
                }

            } catch(Exception e) {
                    e.printStackTrace();
            }

            return message;

}

    public List<Message> getAllMessagesFromUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        try {
            String SQL = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message foundMessage = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getInt("time_posted_epoch")
                );
                messages.add(foundMessage);
            }
            
        } catch(Exception e) {
                e.printStackTrace();
        }
        return messages;
    }

    // public Message getMessageById(int messageId){
    //     Message message = null;
    //     try{
    //         String SQL = " Select * FROM message where message_id = ?";
    //         PreparedStatement ps = connection.prepareStatement(SQL);
    //         ps.setInt(1, messageId);
    //         ResultSet rs = ps.executeQuery();
    //         while(rs.next()){
    //             message = new Message(
    //                 rs.getInt("message_id"),
    //                 rs.getInt("posted_by"),
    //                 rs.getString("message_text"),
    //                 rs.getInt("time_posted_epoch"));
    //         }
    //     } catch (Exception e){
    //         e.printStackTrace();
    //         return null;
    //     }
    //     return message;
    // }

    public Message updateMessage(int messageId, String messageText){
        try {
            String SQL = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, messageText);
            ps.setInt(2, messageId);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
             
                return getMessageById(messageId);
            } else {
                
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }


}
