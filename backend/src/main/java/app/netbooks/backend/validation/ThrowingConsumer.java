package app.netbooks.backend.validation;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void apply(T t) throws Exception;
};