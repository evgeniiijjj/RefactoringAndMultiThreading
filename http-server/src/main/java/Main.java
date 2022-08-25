import interfaces.RequestHandler;
import util.Method;
import util.Request;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        var textHandler = new RequestHandler() {
            @Override
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                final var filePath = Path.of(".", "public", request.getPath());
                final var mimeType = Files.probeContentType(filePath);
                final var template = Files.readString(filePath);
                final var content = template.replace(
                        "{time}",
                        LocalDateTime.now().toString()
                ).getBytes();
                responseStream.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + content.length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                responseStream.write(content);
                responseStream.flush();
            }
        };
        var fileHandler = new RequestHandler() {
            @Override
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                final var filePath = Path.of(".", "public", request.getPath());
                final var mimeType = Files.probeContentType(filePath);
                final var length = Files.size(filePath);
                responseStream.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, responseStream);
                responseStream.flush();
            }
        };
        var server = new Server();
        server
                .addHandler(Method.GET, "/index.html", textHandler)
                .addHandler(Method.GET, "/spring.svg", fileHandler)
                .addHandler(Method.GET, "/spring.png", fileHandler)
                .addHandler(Method.GET, "/resources.html", textHandler)
                .addHandler(Method.GET, "/styles.css", textHandler)
                .addHandler(Method.GET, "/app.js", textHandler)
                .addHandler(Method.GET, "/links.html", textHandler)
                .addHandler(Method.GET, "/forms.html", textHandler)
                .addHandler(Method.GET, "/classic.html", textHandler)
                .addHandler(Method.GET, "/events.html", textHandler)
                .addHandler(Method.GET, "/events.js", textHandler);

        server.listen(9999);
    }
}
