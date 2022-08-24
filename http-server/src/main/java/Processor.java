import exceptions.BadRequestException;
import org.apache.hc.core5.net.URLEncodedUtils;
import util.Method;
import util.Request;
import util.ResponseStatus;
import util.ServerConstants;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Processor implements Runnable {
    private final Socket socket;

    public Processor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            final var in = new BufferedInputStream(socket.getInputStream());
            final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            try {
                Request request = parseRequest(in);
                request.getMethod().getHandler(request.getPath()).handle(request, out);
            } catch (BadRequestException e) {
                out.write((ResponseStatus.BAD_REQUEST.getResponse()).getBytes());
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Request parseRequest(InputStream in) throws IOException, BadRequestException {

        in.mark(ServerConstants.LIMIT.getValue());
        byte[] buffer = new byte[ServerConstants.LIMIT.getValue()];
        final var read = in.read(buffer);
        final var requestLineDelimiter = new byte[]{'\r', '\n'};
        final var requestLineEnd = indexOf(buffer, requestLineDelimiter, 0, read);
        if (requestLineEnd == -1) {
            throw new BadRequestException();
        }
        final var requestLine = new String(Arrays.copyOf(buffer, requestLineEnd)).split(" ");
        if (requestLine.length != 3) {
            throw new BadRequestException();
        }
        Method method = null;
        try {
            method = Method.valueOf(requestLine[0]);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException();
        }
        String path = requestLine[1];
        if (!path.startsWith("/")) {
            throw new BadRequestException();
        }
        var index = path.indexOf('?');
        Map<String, String> requestParams = null;
        if (index > -1) {
            requestParams = parseRequestParams(path);
            path = path.substring(0, index);
        }
        final String protocol = requestLine[2];
        final var requestHeadersDelimiter = new byte[]{'\r', '\n', '\r', '\n'};
        final var requestHeadersStart = requestLineEnd + requestLineDelimiter.length;
        final var requestHeadersEnd = indexOf(buffer, requestHeadersDelimiter, requestHeadersStart, read);
        if (requestHeadersEnd == -1) {
            throw new BadRequestException();
        }
        in.reset();
        in.skip(requestHeadersStart);
        final var headersBytes = in.readNBytes(requestHeadersEnd - requestHeadersStart);
        String headers = new String(headersBytes);
        var requestHeaders = parseHeaders(headers);
        byte[] requestBody = null;
        if (method == Method.POST) {
            String contentLengthParam = "Content-Length";
            if (requestHeaders.containsKey(contentLengthParam)) {
                final var length = Integer.parseInt(requestHeaders.get(contentLengthParam));
                in.skip(requestHeadersDelimiter.length);
                requestBody = in.readNBytes(length);
            }
        }
        return new Request(method, path, protocol, requestParams, requestHeaders, requestBody);
    }

    private int indexOf(byte[] buffer, byte[] delimiter, int start, int limit) {
        int result = -1;
        for (int i = start; i < limit; i++){
            if (buffer[i] == delimiter[0]) {
                result = i;
                for (int j = 1; j < delimiter.length; j++) {
                    i++;
                    if (buffer[i] != delimiter[j]) {
                        result = -1;
                        break;
                    }
                }
            }
            if (result > -1) break;
        }
        return result;
    }

    private Map<String, String> parseHeaders(String headers) {
        Map<String, String> result = new HashMap<>();
        Arrays.asList(headers.split("\r\n")).forEach(s -> result.put(s.substring(0, s.indexOf(": ")), s.substring(s.indexOf(": ") + 2)));
        return result;
    }

    private Map<String, String> parseRequestParams(String requestParams) {
        Map<String, String> result = new HashMap<>();
        URLEncodedUtils.parse(requestParams, StandardCharsets.UTF_8).forEach(nameValuePair -> result.put(nameValuePair.getName(), nameValuePair.getValue()));
        return result;
    }
}
