package ro.disi.disi_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.disi.disi_backend.Dto.MessagePullDto;
import ro.disi.disi_backend.Dto.NewMessageDto;
import ro.disi.disi_backend.model.entity.Message;
import ro.disi.disi_backend.Service.MessageService;
import ro.disi.disi_backend.utility.JsonUtility;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/messages")
public class MessageRestController {

    private final MessageService messageService;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageRestController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

//    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/findAllMessagesForUsers")
    public ResponseEntity<String> findAllMessagesForUsers(@RequestBody MessagePullDto requestBody) throws JsonProcessingException {

        List<Message> messageList = messageService.processFindAllMessagesForUsersRequest(requestBody);
        if (messageList == null)
            return ResponseEntity.badRequest().body("Retrieving messages for user profiles failed!");

        return JsonUtility.createJsonResponse(messageList);
    }

//    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/markAllReceivedMessagesAsSeen")
    public ResponseEntity<String> markAllReceivedMessagesAsSeen(@RequestBody MessagePullDto requestBody) {

        boolean status = messageService.processMarkAllReceivedMessagesAsSeenRequest(requestBody);
        if (!status)
            return ResponseEntity.badRequest().body("Marking messages as seen has failed!");

        return ResponseEntity.ok("Marking messages succeeded!");
    }

//    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/findMostRecentPrivateMessageForUser")
    public ResponseEntity<String> findMostRecentPrivateMessageForUser(@RequestBody MessagePullDto requestBody) throws JsonProcessingException {
        Message recentMessage = messageService.processFindMostRecentPrivateMessageForUserRequest(requestBody);
        return JsonUtility.createJsonResponse(recentMessage);
    }

//    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/imageMessageUpload")
    public ResponseEntity<String> imageMessageUpload(@RequestParam("imageData") MultipartFile imageData,
                                                     @RequestParam("senderUserProfileId") long senderUserProfileId,
                                                     @RequestParam("receiverUserProfileId") long receiverUserProfileId
                                                    ) throws IOException {

//        long senderId = Long.parseLong(senderUserProfileId);
//        long receiverId = Long.parseLong(receiverUserProfileId);
        long senderId = senderUserProfileId;
        long receiverId = receiverUserProfileId;

        NewMessageDto messageDto = new NewMessageDto();
        messageDto.setSenderUserProfileId(senderId);
        messageDto.setReceiverUserProfileId(receiverId);

        Message message = messageService.processImageMessageUploadRequest(imageData, messageDto);
        if (message == null) {
            System.out.println("The processImageMessageUploadRequest is NULL !!!");
            return ResponseEntity.badRequest().body("Uploading and image message failed!");
        }

        String senderDestination = "/user/" + senderId
                + "/" + receiverId + "/messages";
        messagingTemplate.convertAndSend(senderDestination, message);

        String receiverDestination = "/user/" + receiverId
                + "/" + senderId + "/messages";
        messagingTemplate.convertAndSend(receiverDestination, message);

        return JsonUtility.createJsonResponse(message);
    }

    @PostMapping("/vocalMessageUpload")
    public ResponseEntity<String> vocalMessageUpload(@RequestParam("soundData") MultipartFile soundData
            , @RequestParam("senderUserProfileId") long senderUserProfileId
            , @RequestParam("receiverUserProfileId") long receiverUserProfileId) throws IOException {
        long senderId = senderUserProfileId;
        long receiverId = receiverUserProfileId;

        NewMessageDto messageDto = new NewMessageDto();
        messageDto.setSenderUserProfileId(senderId);
        messageDto.setReceiverUserProfileId(receiverId);

        Message message = messageService.processVocalMessageUploadRequest(soundData, messageDto);
        if (message == null)
            return ResponseEntity.badRequest().body("Uploading a vocal message failed!");

        String senderDestination = "/user/" + senderId + "/" + receiverId + "/messages";
        messagingTemplate.convertAndSend(senderDestination, message);

        String receiverDestination = "/user/" + receiverId + "/" + senderId + "/messages";
        messagingTemplate.convertAndSend(receiverDestination, message);

        return JsonUtility.createJsonResponse(message);
    }
}
