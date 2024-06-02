package ro.disi.disi_backend.Dto;

import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserFriendStatus;

public record UserFriendDTO(UserProfile userProfile1, UserProfile userProfile2, UserFriendStatus status) {

}
