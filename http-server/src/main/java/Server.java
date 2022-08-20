import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private List<String> validPaths;
    private ExecutorService executor;

    public Server(List<String> validPaths) {
        this.validPaths = validPaths;
        executor = Executors.newFixedThreadPool(64);
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(9999)) {
            while (true) {
                try (
                        final var socket = serverSocket.accept();
                ) {
                    executor.execute(new Handler(socket, validPaths));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
