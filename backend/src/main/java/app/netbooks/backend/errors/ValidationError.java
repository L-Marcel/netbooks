package app.netbooks.backend.errors;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

public class ValidationError extends RuntimeException {
    @Getter
    private final String field;

    public ValidationError(String field, String message) {
        super(message);
        this.field = field;
    };

    public Map<String, Object> getError() {
        Map<String, Object> error = new LinkedHashMap<>();
        
        error.put("field", this.field);
        error.put("message", this.getMessage());

        return error;
    };
};
