import interfaces.Handler;
import util.Method;
import util.ResponseStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {
        Handler textHandler = (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var template = Files.readString(filePath);
            final var content = template.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            responseStream.write((ResponseStatus.OK.getResponse(mimeType, String.valueOf(content.length))).getBytes());
            responseStream.write(content);
            responseStream.flush();
        };
        Handler fileHandler = (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var length = Files.size(filePath);
            responseStream.write((ResponseStatus.OK.getResponse(mimeType, String.valueOf(length))).getBytes());
            Files.copy(filePath, responseStream);
            responseStream.flush();
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
                .addHandler(Method.GET, "/events.js", textHandler)
                .addHandler(Method.GET, "/default-get.html", textHandler);

        server.listen(9999);
    }
}
