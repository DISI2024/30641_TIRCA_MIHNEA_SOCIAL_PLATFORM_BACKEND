package ro.disi.disi_backend.Dto;

import ro.disi.disi_backend.model.enums.UserFriendStatus;

public record UserFriendDTO(long userId1, long userId2, UserFriendStatus status) {

}
