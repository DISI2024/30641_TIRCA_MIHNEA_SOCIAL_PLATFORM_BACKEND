package ro.disi.disi_backend.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class NewImageMessageDto {

    @NotNull
    public MultipartFile imageData;

    public long senderUserProfileId;
    public long receiverUserProfileId;

    public MultipartFile getImageData() {
        return imageData;
    }

    public void setImageData(MultipartFile imageData) {
        this.imageData = imageData;
    }

    public long getSenderUserProfileId() {
        return ((Number)senderUserProfileId).longValue();
    }

    public void setSenderUserProfileId(long senderUserProfileId) {
        this.senderUserProfileId = senderUserProfileId;
    }

    public long getReceiverUserProfileId() {
        return ((Number)receiverUserProfileId).longValue();
    }

    public void setReceiverUserProfileId(long receiverUserProfileId) {
        this.receiverUserProfileId = receiverUserProfileId;
    }
}
