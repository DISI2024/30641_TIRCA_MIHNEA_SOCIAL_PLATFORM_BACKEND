package ro.disi.disi_backend.dto;

public class NewMessageDto {

    private long senderUserProfileId;

    private long receiverUserProfileId;

    private String content;

    public long getSenderUserProfileId() {
        return ((Number) senderUserProfileId).longValue();
    }

    public void setSenderUserProfileId(long senderUserProfileId) {
        this.senderUserProfileId = senderUserProfileId;
    }

    public long getReceiverUserProfileId() {
        return ((Number) receiverUserProfileId).longValue();
    }

    public void setReceiverUserProfileId(long receiverUserProfileId) {
        this.receiverUserProfileId = receiverUserProfileId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
