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

// ## 2: Our API should be able to process User logins.

// As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
// The request body will contain a JSON representation of an Account, not containing an account_id. 
// In the future, this action may generate a Session token to allow the user to securely use the site.
//  We will not worry about this for now.

// - The login will be successful if and only if the username and password provided in the request 
// body JSON match a real account existing on the database. If successful, the response body should 
// contain a JSON of the account in the response body, including its account_id. The response status 
// should be 200 OK, which is the default.
// - If the login is not successful, the response status should be 401. (Unauthorized)

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
}
