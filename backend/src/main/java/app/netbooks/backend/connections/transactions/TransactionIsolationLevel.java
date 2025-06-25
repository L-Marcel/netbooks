package app.netbooks.backend.connections.transactions;

import lombok.Getter;

public enum TransactionIsolationLevel {
    NONE(0),
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    @Getter
    private int value;

    private TransactionIsolationLevel(int value) {
        this.value = value;
    };
};
