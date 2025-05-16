package app.netbooks.backend.models;

public enum Role {
    ADMINISTRATOR,
    SUBSCRIBER,
    UNKNOWN;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            return Role.UNKNOWN;
        }
    };
};
