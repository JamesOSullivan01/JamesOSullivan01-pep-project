package Service;
import DAO.MessageDAO;
import Model.Message; 

public class MessageService {
    MessageDAO messageDao;


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
}
