package util;

public enum ResponseStatus {

    BAD_REQUEST ("HTTP/1.1 400 Bad Request\r\n" +
            "Content-Length: 0\r\n" +
            "Connection: close\r\n" +
            "\r\n"),
    METHOD_NOT_ALLOWED ("HTTP/1.1 405 Method not allowed\r\n" +
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
        if (strings.length == 2) {
            return String.format(response, strings[0], strings[1]);
        } else {
            return String.format(response.replace("Content-Type: %s\r\n", ""), 0);
        }
    }
}
