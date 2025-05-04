package app.netbooks.backend.models;

public enum Access {
    DEFAULT(0),
    ADMINISTRATOR(1),
    SUBSCRIBER(2);

    private int value;
    private Access(int value) {
        this.value = value;
    };

    public static Access fromValue(int value) {
        if(value <= 0 || value > 2) return Access.DEFAULT;
        return Access.values()[value];
    };

    public int toValue() {
        return this.value;
    };

    public String toRole() {
        return "ROLE_" + this.toString();
    };
};
