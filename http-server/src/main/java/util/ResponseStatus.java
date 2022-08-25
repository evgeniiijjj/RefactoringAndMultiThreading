package util;

public enum ResponseStatus {

    BAD_REQUEST ("HTTP/1.1 400 Bad Request\r\n" +
            "Content-Length: 0\r\n" +
            "Connection: close\r\n" +
            "\r\n"),
    NOT_FOUND ("HTTP/1.1 404 Not Found\r\n" +
            "Content-Length: 0\r\n" +
            "Connection: close\r\n" +
            "\r\n"),
    OK ("HTTP/1.1 200 OK\r\n" +
            "Content-Type: %s\r\n" +
            "Content-Length: %s\r\n" +
            "Connection: close\r\n" +
            "\r\n");

    private final String response;

    ResponseStatus(String response) {
        this.response = response;
    }

    public String getResponse(String... strings) {
        return switch (strings.length) {
            case 1 -> String.format(response, strings[0]);
            case 2 -> String.format(response, strings[0], strings[1]);
            default -> response;
        };
    }
}
