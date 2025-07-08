package app.netbooks.backend.validation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.yaml.snakeyaml.util.Tuple;

import app.netbooks.backend.errors.ValidationFieldError;
import app.netbooks.backend.errors.ValidationStepError;
import app.netbooks.backend.errors.ValidationsError;

public class Validator {
    private final List<Field<?>> fields;

    public Validator() {
        this.fields = new LinkedList<>();
    };

    public <T> Field<T> validate(String field, T value) {
        Field<T> newField = new Field<T>(field, value);
        this.fields.add(newField);
        return newField;
    };

    public void run() throws ValidationsError {
        ValidationsError errors = new ValidationsError();
        
        for(Field<?> field : this.fields) {
            try {
                field.validate();
            } catch (ValidationFieldError e) {
                errors.getErrors().add(e);
            };
        };

        if(errors.getErrors().size() > 0) {
            throw errors;
        };
    };

    public class Field<T> {
        private final String field;
        private final T value;
        private final List<Tuple<Runnable, String>> steps;
        private boolean nullable = false;

        private Field(String field, T value) {
            this.field = field;
            this.value = value;
            this.steps = new LinkedList<>();
        };

        private void validate() throws ValidationFieldError {
            if(this.nullable && this.value == null) return;

            ValidationFieldError error = new ValidationFieldError(this.field);
            for(Tuple<Runnable, String> step : this.steps) {
                try {
                    step._1().run();
                    error.addMessage(step._2());
                } catch (ValidationStepError e) {
                    error.addErrorMessage(e.getMessage());
                }
            };

            if(error.hasAnyError()) throw error;
        };

        public Field<T> nullable() {
            this.nullable = true;
            return this;
        };

        public Field<T> min(int size, String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                int length = -1;

                if(this.value == null) {
                    throw new ValidationStepError(error);
                } else if(value instanceof String) {
                    length = ((String) value).length();
                } else if(value instanceof Collection) {
                    length = ((Collection<?>) value).size();
                } else if(value.getClass().isArray()) {
                    length = Array.getLength(value);
                } else if(value instanceof Number) {
                    length = ((Number) value).intValue();
                };

                if(length < size)
                    throw new ValidationStepError(error);
            }, message));

            return this;
        };

        public Field<T> max(int size, String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                int length = Integer.MAX_VALUE;

                if(this.value == null) {
                    throw new ValidationStepError(error);
                } else if(value instanceof String) {
                    length = ((String) value).length();
                } else if(value instanceof Collection) {
                    length = ((Collection<?>) value).size();
                } else if(value.getClass().isArray()) {
                    length = Array.getLength(value);
                } else if(value instanceof Number) {
                    length = ((Number) value).intValue();
                };

                if(length > size) 
                    throw new ValidationStepError(error);
            }, message));

            return this;
        };

        public Field<T> email(String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                if(!(this.value instanceof String))
                    throw new ValidationStepError(error);
                else {
                    String email = (String) this.value;

                    if(!email.contains("@")) 
                        throw new ValidationStepError(error);
                };
            }, message));

            return this;
        };

        public Field<T> pattern(String regex, String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                try {
                    Pattern pattern = Pattern.compile(regex);

                    if(!(this.value instanceof CharSequence)) {
                        throw new ValidationStepError(error);
                    };
                
                    CharSequence str = (CharSequence) value;
                    if(!pattern.matcher(str).matches()) {
                        throw new ValidationStepError(error);
                    };
                } catch (PatternSyntaxException e) {
                    throw new ValidationStepError(error);
                }
            }, message));

            return this;
        };

        public Field<T> verify(Function<T, Boolean> condition, String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                if(this.value == null || !condition.apply(this.value))
                    throw new ValidationStepError(error);
            }, message));

            return this;
        };

        public Field<T> verifyIfCatch(ThrowingConsumer<T> condition, String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                try {
                    if(this.value == null)
                        throw new ValidationStepError(error);
                    condition.apply(this.value);
                } catch (Exception e) {
                    throw new ValidationFieldError(e.getMessage());
                };
            }, message));

            return this;
        };

        public Field<T> verifyWithCatch(ThrowingFunction<T, Boolean> condition, String message, String error) {
            this.steps.add(new Tuple<Runnable, String>(() -> {
                try {
                    if(this.value == null || !condition.apply(this.value))
                        throw new ValidationStepError(error);
                } catch (Exception e) {
                    throw new ValidationFieldError(e.getMessage());
                };
            }, message));

            return this;
        };
    };
};
