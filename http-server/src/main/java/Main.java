import interfaces.RequestHandler;
import util.Method;
import util.ResponseStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {
        RequestHandler textRequestHandler = (request, responseStream) -> {
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
        RequestHandler fileRequestHandler = (request, responseStream) -> {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var length = Files.size(filePath);
            responseStream.write((ResponseStatus.OK.getResponse(mimeType, String.valueOf(length))).getBytes());
            Files.copy(filePath, responseStream);
            responseStream.flush();
        };
        var server = new Server();
        server
                .addHandler(Method.GET, "/index.html", textRequestHandler)
                .addHandler(Method.GET, "/spring.svg", fileRequestHandler)
                .addHandler(Method.GET, "/spring.png", fileRequestHandler)
                .addHandler(Method.GET, "/resources.html", textRequestHandler)
                .addHandler(Method.GET, "/styles.css", textRequestHandler)
                .addHandler(Method.GET, "/app.js", textRequestHandler)
                .addHandler(Method.GET, "/links.html", textRequestHandler)
                .addHandler(Method.GET, "/forms.html", textRequestHandler)
                .addHandler(Method.GET, "/classic.html", textRequestHandler)
                .addHandler(Method.GET, "/events.html", textRequestHandler)
                .addHandler(Method.GET, "/events.js", textRequestHandler)
                .addHandler(Method.GET, "/default-get.html", textRequestHandler);

        server.listen(9999);
    }
}
