package com.hair.business.rest.resources.chat;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.hair.business.beans.entity.Customer;
import com.hair.business.beans.entity.Merchant;
import com.hair.business.beans.entity.chat.Chat;
import com.hair.business.beans.entity.chat.Message;
import com.hair.business.dao.datastore.abstractRepository.Repository;
import com.hair.business.dao.datastore.impl.ChatBotElasticRepositoryExt;
import com.hair.business.rest.resources.AbstractRequestServlet;
import com.x.business.exception.EntityNotFoundException;
import com.x.business.utilities.Assert;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by ejanla on 7/10/20.
 */

@Path("/messaging")
public class MessagingServlet extends AbstractRequestServlet {

    private final ChatBotElasticRepositoryExt chatRepository;
    private final Repository datastore;

    @Inject
    public MessagingServlet(ChatBotElasticRepositoryExt chatRepository, Repository datastore) {
        this.chatRepository = chatRepository;
        this.datastore = datastore;
    }

    @POST
    @Path("/createChat")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response createChat(Chat chat) {
        Assert.notNull(chat.getCustomerId(), chat.getMerchantId(), "Chat must have a set Customer and Merchant");
        Assert.validIds(datastore.peekOne(chat.getCustomerId(), Customer.class), datastore.peekOne(chat.getMerchantId(), Merchant.class));
        // save the chat
        // send a push notification to receiver. They will tap the notification open the MessagingView, which will fetch all messages and boom, they see it
        try {
            Long id = datastore.allocateId(Chat.class);
            DateTime now = DateTime.now();
            chat.setId(id);
            chat.setPermanentId(id);
            chat.setCreatedDate(now);
            chat.setStartedOn(now);
            chat = chatRepository.saveOne(chat);
            //TODO trigger APNS
            return Response.ok(wrapString("chatId", chat.getId().toString())).build(); // client will use this ID to route messages to this Chat
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path("/closeChat")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response closeChat(@QueryParam("chatId") Long chatId) {
        try {
            final Chat chat = chatRepository.findOne(chatId, Chat.class);
            Assert.notNull(chat, String.format("Chat with Id %s not found", chatId));

            chat.setActive(false);
            chatRepository.saveOne(chat);
            chatRepository.archive(chatId, Chat.class);
            return Response.ok(wrapString("success")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }
    }

    @POST
    @Path("/sendMessage")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response sendMessage(@QueryParam("chatId") Long chatId, List<Message> messages) {
        // Make sure to tell frontend to only send one message/min. We don't need to be that fast, hence the use of List<Messages>
        Assert.notNull(messages, "Messages cannot be null");
        messages.forEach(m -> m.setReceived(DateTime.now().toString()));

        // send a push notification. They will tap the notification to get to the MessagingView, which will fetch all messages and boom, they see it
        try {
            final Chat chat = chatRepository.findOne(chatId, Chat.class);
            Assert.notNull(chat, String.format("Chat with Id %s not found", chatId));

            chat.getMessages().addAll(messages);
            chatRepository.saveOne(chat);

            //TODO trigger APNS
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }

    /**
     * Fetch the chat with Id messageId
     * This call is triggered when:
     *      The Messaging View is loaded by either the receiver or sender
     *      Then every minute from then on while the Messaging View remains open
     * If there's no active message, this call is not invoked by client
     * The client (mobile app) knows that there's a message started by this person, so comes here with that message's Id to fetch it
     * */
    @GET
    @Path("/fetch")
    @Produces(APPLICATION_JSON)
    public Response fetchChat(@QueryParam("chatId") Long chatId) {
        try {
            final Chat chat = chatRepository.findOne(chatId, Chat.class);
            Assert.notNull(chat, String.format("Chat with Id %s not found", chatId));

            return Response.ok(chat).build();
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(generateErrorResponse(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(generateErrorResponse(e)).build();
        }

    }
}
