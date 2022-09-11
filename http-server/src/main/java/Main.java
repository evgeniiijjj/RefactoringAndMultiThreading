import util.Method;
import util.ResponseStatus;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {

    public static void main(String[] args) {
        var server = new Server();
        server
                .addHandler(Method.GET, "/default-get.html", (request, responseStream) -> {
                    final var filePath = Path.of(".", "static", request.getPath());
                    final var mimeType = Files.probeContentType(filePath);
                    final var template = Files.readString(filePath);
                    final var content = template.getBytes();
                    responseStream.write((ResponseStatus.OK.getResponse(mimeType, String.valueOf(content.length))).getBytes());
                    responseStream.write(content);
                    responseStream.flush();
                })
                .addHandler(Method.POST, "/", ((request, responseStream) -> {
                    responseStream.write((ResponseStatus.OK.getResponse()).getBytes());
                    responseStream.flush();
                }));

        server.listen(9999);
    }
}
