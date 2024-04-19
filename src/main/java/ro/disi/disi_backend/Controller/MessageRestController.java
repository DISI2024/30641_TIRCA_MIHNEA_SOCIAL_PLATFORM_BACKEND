package ro.disi.disi_backend.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.disi.disi_backend.Dto.MessagePullDto;
import ro.disi.disi_backend.Model.Message;
import ro.disi.disi_backend.Service.MessageService;
import ro.disi.disi_backend.Utility.JsonUtility;

import java.util.List;

@RestController
@RequestMapping("api/messages")
public class MessageRestController {

    private final MessageService messageService;

    @Autowired
    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/findAllMessagesForUsers")
    public ResponseEntity<String> findAllMessagesForUsers(@RequestBody MessagePullDto requestBody) throws JsonProcessingException {

        List<Message> messageList = messageService.processFindAllMessagesForUsersRequest(requestBody);
        if (messageList == null)
            return ResponseEntity.badRequest().body("Retrieving messages for user profiles failed!");

        return JsonUtility.createJsonResponse(messageList);
    }

    @PostMapping("/findMostRecentPrivateMessageForUser")
    public ResponseEntity<String> findMostRecentPrivateMessageForUser(@RequestBody MessagePullDto requestBody) throws JsonProcessingException {
        Message recentMessage = messageService.processFindMostRecentPrivateMessageForUserRequest(requestBody);
        return JsonUtility.createJsonResponse(recentMessage);
    }
}
