package ro.disi.disi_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.disi.disi_backend.dto.MessagePullDto;
import ro.disi.disi_backend.dto.NewMessageDto;
import ro.disi.disi_backend.dto.UserIdDto;
import ro.disi.disi_backend.model.entity.ImageMessage;
import ro.disi.disi_backend.model.entity.Message;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.entity.VocalMessage;
import ro.disi.disi_backend.repository.MessageRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.utility.ImageUtility;

import java.io.IOException;
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

    public UserIdDto processGetUserProfileIdRequest(UserIdDto userIdDto) {
        UserProfile userProfile = userProfileRepository.findByUserId(userIdDto.getUserId()).orElse(null);
        if (userProfile == null) {
            return null;
        }

        UserIdDto userProfileDto = new UserIdDto();
        userProfileDto.setUserId(userProfile.getId());
        return userProfileDto;
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
        {
            System.out.println("One of the users are null!");
            return null;
        }

        List<Message> messageList = this.findAllMessagesForUsers(firstUserProfile, secondUserProfile).orElse(null);
        if (messageList == null)
        {
            System.out.println("The message list is null");
            return null;
        }

        messageList.stream().filter(message -> firstUserProfileId == message.getReceiverUserProfile().getId()).forEach(
                message -> {
                    message.setSeenByReceiver(true);
                    messageRepository.save(message);
                }
        );

        for (Message message : messageList) {
            if (message instanceof ImageMessage && ((ImageMessage) message).getImageData() != null) {
                byte[] imageData = ((ImageMessage) message).getImageData();
                ((ImageMessage) message).setImageData(ImageUtility.decompressImage(imageData));
            }
        }

        return messageList;
    }

    public boolean processMarkAllReceivedMessagesAsSeenRequest(MessagePullDto requestBody) {
        long firstUserProfileId = requestBody.getFirstUserProfileId();
        long secondUserProfileId = requestBody.getSecondUserProfileId();

        UserProfile firstUserProfile = userProfileRepository.findById(firstUserProfileId).orElse(null);
        UserProfile secondUserProfile = userProfileRepository.findById(secondUserProfileId).orElse(null);

        if (firstUserProfile == null || secondUserProfile == null)
            return false;

        List<Message> messageList = this.findAllMessagesForUsers(firstUserProfile, secondUserProfile).orElse(null);
        if (messageList == null)
            return false;

        messageList.stream().filter(message -> firstUserProfileId == message.getReceiverUserProfile().getId()).forEach(
                message -> {
                    message.setSeenByReceiver(true);
                    messageRepository.save(message);
                }
        );

        return true;
    }

    public Message processImageMessageUploadRequest(MultipartFile file, NewMessageDto requestBody) throws IOException {

        MultipartFile imageData = file;
        long senderUserProfileId = requestBody.getSenderUserProfileId();
        long receiverUserProfileId = requestBody.getReceiverUserProfileId();

        UserProfile senderUserProfile = userProfileRepository.findById(senderUserProfileId).orElse(null);
        UserProfile receiverUserProfile = userProfileRepository.findById(receiverUserProfileId).orElse(null);

        if (senderUserProfile == null || receiverUserProfile == null) {
            System.out.println("senderUserProfile or receiverUserProfile is NULL!");
            return null;
        }

        ImageMessage imageMessage = new ImageMessage();
        byte[] imageBytes = ImageUtility.compressImage(imageData.getBytes());
        imageMessage.setImageData(imageBytes);
        imageMessage.setSenderUserProfile(senderUserProfile);
        imageMessage.setReceiverUserProfile(receiverUserProfile);
        imageMessage.setContent(null);
        imageMessage.setSeenByReceiver(false);
        messageRepository.save(imageMessage);

        ImageMessage uncompressedImageMessage = new ImageMessage();
        uncompressedImageMessage.setImageData(imageData.getBytes());
        uncompressedImageMessage.setSenderUserProfile(senderUserProfile);
        uncompressedImageMessage.setReceiverUserProfile(receiverUserProfile);
        uncompressedImageMessage.setContent(null);
        uncompressedImageMessage.setSeenByReceiver(false);

        return uncompressedImageMessage;
    }

    public Message processVocalMessageUploadRequest(MultipartFile file, NewMessageDto requestBody) throws IOException {
        MultipartFile soundData = file;
        long senderUserProfileId = requestBody.getSenderUserProfileId();
        long receiverUserProfileId = requestBody.getReceiverUserProfileId();

        UserProfile senderUserProfile = userProfileRepository.findById(senderUserProfileId).orElse(null);
        UserProfile receiverUserProfile = userProfileRepository.findById(receiverUserProfileId).orElse(null);

        if (senderUserProfile == null || receiverUserProfile == null)
            return null;

        VocalMessage vocalMessage = new VocalMessage();
        // Sa incercam fara compresie
        //vocalMessage.setSoundData(ImageUtility.compressImage(soundData.getBytes()));
        vocalMessage.setSoundData(soundData.getBytes());
        vocalMessage.setSenderUserProfile(senderUserProfile);
        vocalMessage.setReceiverUserProfile(receiverUserProfile);
        vocalMessage.setContent(null);
        vocalMessage.setSeenByReceiver(false);

        VocalMessage checkedMessage = (VocalMessage) messageRepository.save(vocalMessage);
        if (checkedMessage == null)
            return null;

        VocalMessage uncompressedVocalMessage = new VocalMessage();
        uncompressedVocalMessage.setSoundData(soundData.getBytes());
        uncompressedVocalMessage.setSenderUserProfile(senderUserProfile);
        uncompressedVocalMessage.setReceiverUserProfile(receiverUserProfile);
        uncompressedVocalMessage.setContent(null);
        uncompressedVocalMessage.setSeenByReceiver(false);

        return uncompressedVocalMessage;

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
                secondUserProfile, firstUserProfile
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
