package ro.disi.disi_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import ro.disi.disi_backend.Service.MessageService;

@Controller
public class MessageWsController {

    private final MessageService messageService;

    @Autowired
    public MessageWsController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/message")
    public void transmitMessage(@Payload String requestBody) throws JsonProcessingException, ParseException {
        messageService.processHandleWsMessageTransmission(requestBody);
    }

}
