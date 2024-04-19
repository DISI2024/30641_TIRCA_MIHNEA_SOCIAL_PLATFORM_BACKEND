package ro.disi.disi_backend.Utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

public interface JsonUtility {

    static ResponseEntity<String> createJsonResponse(Object responseData) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseData);
        return ResponseEntity.ok(jsonResponse);
    }
}
