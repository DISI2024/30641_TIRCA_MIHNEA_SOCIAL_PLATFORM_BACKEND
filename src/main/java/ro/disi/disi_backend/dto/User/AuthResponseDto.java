package ro.disi.disi_backend.dto.user;

public class AuthResponseDto {

    private long userId;
    private long userProfileId;

    // Add JWT + isAdmin flags later

    public AuthResponseDto() {

    }

    public AuthResponseDto(long userId, long userProfileId) {
        this.userId = userId;
        this.userProfileId = userProfileId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(long userProfileId) {
        this.userProfileId = userProfileId;
    }
}
