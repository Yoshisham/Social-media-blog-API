package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    static int MAX_MESSAGE_LENGTH = 255;
    public MessageDAO messageDAO;
    
    /**
     * No-args constructor for MessageService which creates a MessageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a MessageService when a MessageDAO is provided.
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * Uses messageDAO to add a new message to the 'message' table. First confirms that
     * the message is posted by an actual user and then makes sure that the message is of appropiate length.
     * @param message a message object.
     * @return the message that is added.
     */
    public Message addMessage(Message message) {
        if (messageDAO.confirmMessageAccount(message.getPosted_by())) {
            if (message.getMessage_text().length() <= MAX_MESSAGE_LENGTH && !message.getMessage_text().isBlank()){
                return messageDAO.createMessage(message);
            }
        }
        return null;
    }
    
    /**
     * Uses messageDAO to get all messages from the 'message' table.
     * @return a list of messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Uses messageDAO to get all messages from the 'message' table that correspond to
     * the given user.
     * @param user the value of 'posted_by'.
     * @return a list of messages belonging to the user.
     */
    public List<Message> getAllMessagesByUser(int user) {
        return messageDAO.getAllMessagesByUser(user);
    }

    /**
     * Uses the messageDAO to get a message by a given 'message_id'.
     * @param id message_id.
     * @return a message object.
     */
    public Message getMessageByID (int id) {
        return messageDAO.getMessageByID(id);
    }

    /**
     * Uses the messageDAO to delete a message by a given 'message_id'. This 
     * method first stores a copy of a message and returns it if it was deleted.
     * @param id message_id.
     * @return the deleted message object.
     */
    public Message deleteMessageByID (int id) {
        Message message = messageDAO.getMessageByID(id);
        if (messageDAO.deleteMessageByID(id) > 0) {
            return message;
        }
        return null;
    }

    /**
     * Uses the messageDAO to update a findable message with an updated "message_text".
     * Makes sure that the message exists, and that the new message is of 
     * appropiate length and it is no blank.
     * @param message the message object containing the new information
     * @return the updated message object.
     */
    public Message updateMessageByID (Message message) {
        Message dbMessage = messageDAO.getMessageByID(message.getMessage_id());

        if (dbMessage != null) {
            if (message.getMessage_text().length() <= MAX_MESSAGE_LENGTH && !message.getMessage_text().isBlank()){
                dbMessage.setMessage_text(message.getMessage_text());
                messageDAO.updateMessageByID(dbMessage);
                return dbMessage;
            }
        }
        
        return null;
    }
}