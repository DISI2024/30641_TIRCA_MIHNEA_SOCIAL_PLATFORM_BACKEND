package ro.disi.disi_backend.security.model.dto;

/**
 * Request send by the authenticating user
 */
public record AuthenticationRequest(String identifier, String password) {
}
