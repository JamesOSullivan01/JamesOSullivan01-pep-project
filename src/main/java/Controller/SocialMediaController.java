package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import DAO.MessageDAO;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    MessageDAO messageDAO;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
     }


    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    };


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createUser);
        app.post("/messages", this::createMessage);
        //DELETE localhost:8080/messages/{message_id}
        app.delete("/messages/{message_id}",this::deleteMessage);
        // As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
        app.get("/messages", this::getAllMessages);
        // As a user, I should be able to submit a GET request on the endpoint 
        // GET localhost:8080/accounts/{account_id}/messages.
        app.get("/accounts/{account_id}/messages", this::getAllMesagesFromAUser);
        // ## 2: Our API should be able to process User logins.
        // As a user, I should be able to verify my login on the endpoint POST localhost:8080/login.
        app.post("/login", this::loginUser);
        // As a user, I should be able to submit a GET request on the endpoint 
        // GET localhost:8080/messages/{message_id}.
        app.get("/messages/{message_id}", this::getMessagesById);
        // As a user, I should be able to submit a PATCH request on the 
        // endpoint PATCH localhost:8080/messages/{message_id}.
        app.patch("/messages/{message_id}", this::updateMessage);

        return app;
    }

    private void createUser(Context ctx) throws JsonProcessingException {
        // System.out.println("CREATING USER!!!!");
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount == null || addedAccount.getUsername() == null 
        || addedAccount.getUsername().isBlank() || addedAccount.getPassword().length() < 4){
                ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage == null || addedMessage.getMessage_text() == null || addedMessage.getMessage_text().isEmpty()){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }
        //DELETE localhost:8080/messages/{message_id}
//         //## 6: Our API should be able to delete a message identified by a message ID.
    private void deleteMessage(Context ctx) throws JsonProcessingException { 
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(message_id);
            if(message == null){
                ctx.status(200);
            } else {
                ctx.json(message);
            }

    }

    // ## 4: Our API should be able to retrieve all messages.
        private void getAllMessages(Context ctx){
        List<Message> allMessages = messageService.listOfAllMessages();
        ctx.json(allMessages);
        
    }
    // ## 8: Our API should be able to retrieve all messages written by a particular user.
    private void getAllMesagesFromAUser(Context ctx) throws JsonProcessingException {
        int account_id = Integer.valueOf(ctx.pathParam("account_id"));
       List<Message> allMessagesFromUser = messageService.listOfMessagesFromUser(account_id);
       ctx.json(allMessagesFromUser);
}

    private void loginUser(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginToAccount(account);
        if (loggedInAccount != null) {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        } else {
            ctx.status(401); 
        }
        
    }
    // ## 5: Our API should be able to retrieve a message by its ID.

// As a user, I should be able to submit a GET request on the endpoint 
// GET localhost:8080/messages/{message_id}.

// - The response body should contain a JSON representation of the message identified 
// by the message_id. It is expected for the response body to simply be empty if there is 
// no such message. The response status should always be 200, which is the default.

    private void getMessagesById(Context ctx){
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
            message = null;
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

private void updateMessage(Context ctx) throws JsonProcessingException {
    ObjectMapper obj = new ObjectMapper();
    int messageId = Integer.valueOf(ctx.pathParam("message_id"));

    Message newMessage = obj.readValue(ctx.body(), Message.class);
    String newMessageText = newMessage.getMessage_text();
        if (!newMessageText.isEmpty() && newMessageText.length() <=255 ) {
        
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);
        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    } else {
        ctx.status(400);
    }
}

}
