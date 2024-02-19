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
}

