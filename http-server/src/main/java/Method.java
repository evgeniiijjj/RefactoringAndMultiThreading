import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public enum Method {

    GET (new HashMap<>()),
    POST (new HashMap<>());

    private final Map<String, Handler> map;

    Method(Map<String, Handler> map) {
        this.map = map;
        map.put("Not found", new Handler() {
            @Override
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                responseStream.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                responseStream.flush();
            }
        });
    }

    public Handler getHandler(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return map.get("Not found");
    }

    public void addHandler(String key, Handler handler) {
        map.put(key, handler);
    }
}