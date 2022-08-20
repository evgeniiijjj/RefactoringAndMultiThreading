import java.util.List;

public class Main {

    static final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html"
            , "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public static void main(String[] args) {
        var server = new Server(validPaths);
        server.listen(9999);
    }
}
