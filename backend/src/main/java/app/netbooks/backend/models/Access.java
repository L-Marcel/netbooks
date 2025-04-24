package app.netbooks.backend.models;

public enum Access {
    DEFAULT,
    ADMINISTRATOR,
    SUBSCRIBER;

    public static Access fromValue(int value) {
        if(value <= 0 || value > 2) return Access.DEFAULT;
        return Access.values()[value];
    };
};
