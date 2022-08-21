import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private ExecutorService executor;

    public Server() {
        executor = Executors.newFixedThreadPool(64);
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(9999)) {
            while (true) {
                try (
                        final var socket = serverSocket.accept();
                ) {
                    executor.execute(new Processor(socket));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public Server addHandler(Method method, String path, Handler handler) {
        method.addHandler(path, handler);
        return this;
    }
}
