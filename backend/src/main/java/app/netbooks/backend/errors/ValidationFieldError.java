package app.netbooks.backend.errors;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.util.Tuple;

public class ValidationFieldError extends RuntimeException {
    private final String field;
    private List<Tuple<Boolean, String>> messages;

    public ValidationFieldError(String field) {
        super();
        this.field = field;
        this.messages = new LinkedList<>();
    };

    public void addMessage(String message) {
        this.messages.add(new Tuple<Boolean,String>(true, message));
    };

    public void addErrorMessage(String message) {
        this.messages.add(new Tuple<Boolean,String>(false, message));
    };

    public boolean hasAnyError() {
        return this.messages.stream().anyMatch((message) -> !message._1());
    };

    public Map<String, Object> getError() {
        Map<String, Object> error = new LinkedHashMap<>();
        
        error.put("field", this.field);
        error.put("messages", this.messages
            .stream()
            .filter((message) -> !message._1() || message._2() != null)
            .map((message) -> {
                Map<String, Object> content = new LinkedHashMap<>();

                content.put("error", !message._1());
                content.put("content", message._2());

                return content;
            }).collect(Collectors.toList()));

        return error;
    };
};
