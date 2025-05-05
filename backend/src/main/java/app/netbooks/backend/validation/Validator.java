package app.netbooks.backend.validation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import app.netbooks.backend.errors.ValidationError;
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
            } catch (ValidationError e) {
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
        private final List<Runnable> steps;
        private boolean nullable = false;

        private Field(String field, T value) {
            this.field = field;
            this.value = value;
            this.steps = new LinkedList<>();
        };

        private void validate() throws ValidationError {
            if(this.nullable && this.value == null) return;
            for(Runnable step : this.steps) {
                step.run();
            };
        };

        public Field<T> nullable() {
            this.nullable = true;
            return this;
        };

        public Field<T> min(int size, String message) {
            this.steps.add(() -> {
                int length = -1;

                if(this.value == null) {
                    throw new ValidationError(this.field, message);
                } else if (value instanceof String) {
                    length = ((String) value).length();
                } else if (value instanceof Collection) {
                    length = ((Collection<?>) value).size();
                } else if (value.getClass().isArray()) {
                    length = Array.getLength(value);
                } else if (value instanceof Number) {
                    length = ((Number) value).intValue();
                };

                if (length < size)
                    throw new ValidationError(this.field, message);
            });

            return this;
        };

        public Field<T> max(int size, String message) {
            this.steps.add(() -> {
                int length = Integer.MAX_VALUE;

                if(this.value == null) {
                    throw new ValidationError(this.field, message);
                } else if (value instanceof String) {
                    length = ((String) value).length();
                } else if (value instanceof Collection) {
                    length = ((Collection<?>) value).size();
                } else if (value.getClass().isArray()) {
                    length = Array.getLength(value);
                } else if (value instanceof Number) {
                    length = ((Number) value).intValue();
                };

                if (length > size) 
                    throw new ValidationError(this.field, message);
            });

            return this;
        };

        public Field<T> email(String message) {
            this.steps.add(() -> {
                if(!(this.value instanceof String) || this.value == null)
                    throw new ValidationError(this.field, message);
                else {
                    String email = (String) this.value;

                    if(!email.contains("@")) 
                        throw new ValidationError(this.field, message);
                };
            });

            return this;
        };

        public Field<T> pattern(String regex, String message) {
            this.steps.add(() -> {
                try {
                    Pattern pattern = Pattern.compile(regex);

                    if (!(this.value instanceof CharSequence) || this.value == null) {
                        throw new ValidationError(this.field, message);
                    };
                
                    CharSequence str = (CharSequence) value;
                    if (!pattern.matcher(str).matches()) {
                        throw new ValidationError(this.field, message);
                    };
                } catch (PatternSyntaxException e) {
                    throw new ValidationError(this.field, message);
                }
            });

            return this;
        };

        public Field<T> verify(boolean condition, String message) {
            this.steps.add(() -> {
                if(!condition)
                    throw new ValidationError(this.field, message);
            });

            return this;
        };
    };
};
