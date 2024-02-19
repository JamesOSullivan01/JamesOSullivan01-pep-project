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
        List<Message> allMessages = messageDao.getAllMessages(); 
        List<Message> messagesFromUser = new ArrayList<>();
        for (Message message : allMessages) {
            if (message.getPosted_by() == accountId) {
                messagesFromUser.add(message);
            }
        }
        return messagesFromUser;
    }
}

