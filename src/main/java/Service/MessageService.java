package Service;
import DAO.MessageDAO;
import Model.Message; 
import java.util.*;

public class MessageService {
    MessageDAO messageDao;
    Message myMessage = new Message();


    public MessageService(){
        messageDao = new MessageDAO();
    }

public MessageService(MessageDAO messageDAO){
        this.messageDao = messageDAO;
}

public Message addMessage(Message message){
    Message addedMessage = messageDao.createMessage(message);
    if(addedMessage != null){
        return addedMessage;
    } else {
        return null;
    }
}

    public Message deleteMessage(int id){
        Message messageToDelete = messageDao.getMessageById(id);
        if (messageToDelete != null) {
            messageDao.deleteMessage(id);
            return messageToDelete; 
        } else {
            return null;
        }
    }

    public List<Message> listOfMessagesFromUser(int accountId) {
        List<Message> messagesFromUser = null;  
        return messageDao.getAllMessagesFromUser(accountId);
    }

        // ## 4: Our API should be able to retrieve all messages.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
// - The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
// It is expected for the list to simply be empty if there are no messages. The response status should always be 200, 
// which is the default.
    public List<Message> listOfAllMessages(){
       List<Message> allMessages = messageDao.getAllMessages();
       return allMessages;
    }

      // ## 5: Our API should be able to retrieve a message by its ID.

// As a user, I should be able to submit a GET request on the endpoint 
// GET localhost:8080/messages/{message_id}.

// - The response body should contain a JSON representation of the message identified 
// by the message_id. It is expected for the response body to simply be empty if there is 
// no such message. The response status should always be 200, which is the default.
    public Message getMessageById(int message_id){
        Message message = messageDao.getMessageById(message_id);
        if(message != null){
            return message;
        } else {
        return null;
        }
    }
// ## 7: Our API should be able to update a message text identified by a message ID.

// As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
//  The request body should contain a new message_text values to replace the message identified by message_id. 
//  The request body can not be guaranteed to contain any other information.

// - The update of a message should be successful if and only if the message id already exists and the new message_text 
// is not blank and is not over 255 characters. If the update is successful, the response body should contain the full updated 
// message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which 
// is the default. The message existing on the database should have the updated message_text.
// - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    public Message updateMessage(int messageId, String newMessageText) {
        Message message = messageDao.getMessageById(messageId);
        if(message != null){
            message = messageDao.updateMessage(messageId, newMessageText);
        } else {
        return null;
        }
        return message;
    }



}

