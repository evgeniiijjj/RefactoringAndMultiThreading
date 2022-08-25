package interfaces;

import util.Request;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface RequestHandler {
    void handle(Request request, BufferedOutputStream responseStream) throws IOException;
}
