package app.netbooks.backend.errors;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class ValidationsError extends RuntimeException {
    @Getter
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    @Getter
    private final List<ValidationError> errors;

    public ValidationsError() {
        super();
        this.errors = new LinkedList<>();
    };

    public Map<String, Object> getError() {
        Map<String, Object> error = new LinkedHashMap<>();
        List<Map<String, Object>> errors = this.errors
            .stream()
            .map(
                (ValidationError validationError) -> 
                    validationError.getError()
            )
            .collect(Collectors.toList());
        
        error.put("status", this.status.value());
        error.put("errors", errors);

        return error;
    };
};
