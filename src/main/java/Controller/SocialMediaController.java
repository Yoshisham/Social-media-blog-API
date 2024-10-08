package Controller;

//import org.eclipse.jetty.http.HttpTester.Message;
//import org.h2.engine.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postUserRegHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("/messages", this::postMsgCreatHandler);
        app.get("/messages", this::getMsgsHandler);
        app.get("/messages/{message_id}", this::getMsgByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMsgByIdHandler);
        app.patch("/messages/{message_id}", this::patchMsgByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getUserMsgsHandler);

        return app;
    }

    /**
     * Handler to process new User registration.
     * Registration will be successful if username is not blank, password length is greater than
     * four, and the username is unique. Response status will be 200 if successful, otherwise 400 (Client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postUserRegHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredAccount = accountService.addAccount(account);
        if(registeredAccount != null) {
            ctx.json(mapper.writeValueAsString(registeredAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to process User login.
     * Login will be successful if username and password match. Response status will be 200 if
     * successful, otherwise 401 (Unauthorized).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postUserLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account verifiedAccount = accountService.loginAccount(account);
        if(verifiedAccount != null) {
            ctx.json(mapper.writeValueAsString(verifiedAccount));
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    /**
     * Handler to process new message creation.
     * Creation of a message will be successful if message is not blank and less than 255 characters, and
     * the user posting exists. Response status will be 200 if successful, otherwise 400 (Client error).
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMsgCreatHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.addMessage(message);
        if(createdMessage != null) {
            ctx.json(mapper.writeValueAsString(createdMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getMsgsHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /**
     * Handler to retrieve message by id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getMsgByIdHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if(message != null) {
            ctx.json(message);
        }
        else{
            ctx.status(200);
            ctx.result("");
        }
    }

    /**
     * Handler to delete message by id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void deleteMsgByIdHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageByID(id);
        if(message != null) {
            ctx.json(message);
        }
        else{
            ctx.status(200);
            ctx.result("");
        }
    }

    /**
     * Handler to update a message by id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void patchMsgByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        message.setMessage_id(id);
        Message updatedMessage = messageService.updateMessageByID(message);
        if(updatedMessage != null) {
            ctx.json(updatedMessage);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    /**
     * Handler to get all messages by a given user id.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getUserMsgsHandler(Context ctx) {
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByUser(accountID);
        if (!messages.isEmpty()) {
            ctx.json(messages);
        } else { 
            ctx.json(messages);
            ctx.status(200);
        }
    }
}