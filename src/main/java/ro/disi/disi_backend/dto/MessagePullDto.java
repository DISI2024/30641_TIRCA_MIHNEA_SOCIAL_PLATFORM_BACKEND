package ro.disi.disi_backend.dto;

public class MessagePullDto {

    private long firstUserProfileId;
    private long secondUserProfileId;

    public long getFirstUserProfileId() {
        return firstUserProfileId;
    }

    public void setFirstUserProfileId(long firstUserProfileId) {
        this.firstUserProfileId = firstUserProfileId;
    }

    public long getSecondUserProfileId() {
        return secondUserProfileId;
    }

    public void setSecondUserProfileId(long secondUserProfileId) {
        this.secondUserProfileId = secondUserProfileId;
    }
}
