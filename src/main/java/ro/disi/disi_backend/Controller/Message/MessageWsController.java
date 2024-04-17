package ro.disi.disi_backend.Controller.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import ro.disi.disi_backend.Service.Chat.MessageService;

@Controller
public class MessageWsController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    public void transmitMessage(@Payload String requestBody) throws JsonProcessingException, ParseException {
        messageService.processHandleWsMessageTransmission(requestBody);
    }

}
