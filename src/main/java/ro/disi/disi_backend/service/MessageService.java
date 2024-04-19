package ro.disi.disi_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.dto.MessagePullDto;
import ro.disi.disi_backend.model.entity.Message;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.repository.MessageRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserProfileRepository userProfileRepository;

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(SimpMessagingTemplate messagingTemplate, UserProfileRepository userProfileRepository, MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userProfileRepository = userProfileRepository;
        this.messageRepository = messageRepository;
    }

    private Optional<List<Message>> findAllMessagesForUsers(UserProfile senderUserProfile, UserProfile receiverUserProfile) {

        List<Message> firstList = messageRepository.findAllBySenderUserProfileAndReceiverUserProfile(senderUserProfile, receiverUserProfile).orElse(null);
        List<Message> secondList = messageRepository.findAllBySenderUserProfileAndReceiverUserProfile(receiverUserProfile, senderUserProfile).orElse(null);

        if (firstList == null || secondList == null)
            return Optional.empty();

        List<Message> retList = new ArrayList<>();
        retList.addAll(firstList);
        retList.addAll(secondList);

        retList.sort((m1, m2) -> Math.toIntExact(m1.getId() - m2.getId()));

        return Optional.of(retList);
    }

    public List<Message> processFindAllMessagesForUsersRequest(MessagePullDto requestBody) {
        long firstUserProfileId = requestBody.getFirstUserProfileId();
        long secondUserProfileId = requestBody.getSecondUserProfileId();

        UserProfile firstUserProfile = userProfileRepository.findById(firstUserProfileId).orElse(null);
        UserProfile secondUserProfile = userProfileRepository.findById(secondUserProfileId).orElse(null);

        if (firstUserProfile == null || secondUserProfile == null)
            return null;

        List<Message> messageList = this.findAllMessagesForUsers(firstUserProfile, secondUserProfile).orElse(null);
        if (messageList == null)
            return null;

        // Replace with streams ?!?!
        for (int i = 0; i < messageList.size(); i++)
            if (firstUserProfile.equals(messageList.get(i).getReceiverUserProfile())) {
                messageList.get(i).setSeenByReceiver(true);
                messageRepository.save(messageList.get(i));
            }

        return messageList;
    }

    public Message processFindMostRecentPrivateMessageForUserRequest(MessagePullDto requestBody) {
        long firstUserProfileId = requestBody.getFirstUserProfileId();
        long secondUserProfileId = requestBody.getSecondUserProfileId();

        UserProfile firstUserProfile = userProfileRepository.findById(firstUserProfileId).orElse(null);
        UserProfile secondUserProfile = userProfileRepository.findById(secondUserProfileId).orElse(null);

        if (firstUserProfile == null || secondUserProfile == null)
            return null;

        Message mostRecentMessage1 = messageRepository.findFirstBySenderUserProfileAndReceiverUserProfileOrderByIdDesc(
                firstUserProfile, secondUserProfile
        ).orElse(null);

        Message mostRecentMessage2 = messageRepository.findFirstBySenderUserProfileAndReceiverUserProfileOrderByIdDesc(
                firstUserProfile, secondUserProfile
        ).orElse(null);

        if (mostRecentMessage1 != null && mostRecentMessage2 != null) {
            return (mostRecentMessage1.getId() > mostRecentMessage2.getId()) ? mostRecentMessage1 : mostRecentMessage2;
        } else if (mostRecentMessage1 != null) {
            return mostRecentMessage1;
        } else {
            return mostRecentMessage2;
        }

    }

    public void processHandleWsMessageTransmission(String requestBody) throws ParseException, JsonProcessingException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(requestBody);

        long senderUserProfileId = ((Number) json.get("senderUserProfileId")).longValue();
        long receiverUserProfileId = ((Number) json.get("receiverUserProfileId")).longValue();
        String content = (String) json.get("content");

        UserProfile senderUserProfile = userProfileRepository.findById(senderUserProfileId).orElse(null);
        UserProfile receiverUserProfile = userProfileRepository.findById(receiverUserProfileId).orElse(null);

        Message message = new Message(content, senderUserProfile, receiverUserProfile);

        Message checkedMessage = messageRepository.save(message);

        ObjectMapper objectMapper = new ObjectMapper();
        String ret = objectMapper.writeValueAsString(checkedMessage);


        String senderDestination = "/user/" + senderUserProfileId
                + "/" + receiverUserProfileId + "/messages";
        messagingTemplate.convertAndSend(senderDestination, ret);

        String receiverDestination = "/user/" + receiverUserProfileId
                + "/" + senderUserProfileId + "/messages";
        messagingTemplate.convertAndSend(receiverDestination, ret);
    }
}
