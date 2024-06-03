package ro.disi.disi_backend.dto;

import ro.disi.disi_backend.model.enums.UserRole;

/**
 * Data about the currently logged-in user, stored in Redux
 */
public record UserDataDto(Long id, String username, String firstName, String lastName, UserRole role) {
}