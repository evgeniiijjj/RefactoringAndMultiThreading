import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        var server = new Server();
        server
                .addHandler(Method.GET, "/index.html", new Handler() {
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
                })
                .addHandler(Method.GET, "/spring.svg", new Handler() {
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
                })
                .addHandler(Method.GET, "/spring.png", new Handler() {
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
                })
                .addHandler(Method.GET, "/resources.html", new Handler() {
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
                })
                .addHandler(Method.GET, "/styles.css", new Handler() {
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
                })
                .addHandler(Method.GET, "/app.js", new Handler() {
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
                })
                .addHandler(Method.GET, "/links.html", new Handler() {
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
                })
                .addHandler(Method.GET, "/forms.html", new Handler() {
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
                })
                .addHandler(Method.GET, "/classic.html", new Handler() {
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
                })
                .addHandler(Method.GET, "/events.html", new Handler() {
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
                })
                .addHandler(Method.GET, "/events.js", new Handler() {
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
                });

        server.listen(9999);
    }
}
