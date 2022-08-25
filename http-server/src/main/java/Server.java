import interfaces.RequestHandler;
import util.Method;
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
                final var socket = serverSocket.accept();
                executor.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public Server addHandler(Method method, String path, RequestHandler requestHandler) {
        method.addHandler(path, requestHandler);
        return this;
    }
}
