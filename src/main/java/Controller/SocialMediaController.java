package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

// As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

// - The deletion of an existing message should remove an existing message from the database. If the message existed,
//  the response body should contain the now-deleted message. The response status should be 200, which is the default.
// - If the message did not exist, the response status should be 200, but the response body should be empty. 
// This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with
//  the same type of response.
    private void deleteMessage(Context ctx) throws JsonProcessingException { 
        
        // String id = ctx.pathParam("message_id");
        //     ctx.result(message_id);
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        // System.out.println("id: " + id);
        Message message = messageService.deleteMessage(message_id);
            if(message == null){
                // System.out.println("message: " + message);
                ctx.status(200);
            } else {
                ctx.json(message);
            }

    }

    // ## 4: Our API should be able to retrieve all messages.

// As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
// - The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
// It is expected for the list to simply be empty if there are no messages. The response status should always be 200, 
// which is the default.

    private void getAllMessages(Context ctx){
        List<Message> allMessages = messageService.listOfAllMessages();
        ctx.json(allMessages);
        
    }

    private void getAllMesagesFromAUser(Context ctx) throws JsonProcessingException {
        String accountIdParam = ctx.queryParam("posted_by");
        if (accountIdParam == null) {
            ctx.status(400);
            return;
        }
        try {
            int accountId = Integer.parseInt(accountIdParam);
            List<Message> messagesFromUser = messageService.listOfMessagesFromUser(accountId);
            ctx.json(messagesFromUser);
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
}


}
