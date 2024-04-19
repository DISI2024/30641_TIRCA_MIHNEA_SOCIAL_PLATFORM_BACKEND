package ro.disi.disi_backend.security.model.dto;

/**
 * Request send by the registering user
 */
public record RegisterRequest(String email, String username, String password, String firstName, String lastName) {
}
