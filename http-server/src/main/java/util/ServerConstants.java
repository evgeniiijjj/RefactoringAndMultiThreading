package util;

public enum ServerConstants {

    LIMIT (4096);

    private final int value;

    ServerConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
