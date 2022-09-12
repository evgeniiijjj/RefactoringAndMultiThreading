import exceptions.BadRequestException;
import exceptions.MethodNotAllowedException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.hc.core5.net.URLEncodedUtils;
import util.Method;
import util.Request;
import util.ResponseStatus;
import util.ServerConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class SocketHandler implements Runnable {
    private final Socket socket;

    public SocketHandler(Socket socket) {
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
                for (String param : request.getRequestParams()) {
                    List<String> values = request.getRequestParam(param);
                    for (String value : values) {
                        System.out.println(param + " - " + value);
                    }
                }
                System.out.println();
                for (String fileName : request.getFilesName()) {
                    FileItem item = request.getFile(fileName);
                    System.out.println(fileName);
                    System.out.println();
                    InputStream input = item.getInputStream();
                    System.out.println(new String(input.readNBytes(input.available())));
                }
            } catch (BadRequestException e) {
                out.write((ResponseStatus.BAD_REQUEST.getResponse()).getBytes());
                out.flush();
            } catch (MethodNotAllowedException e) {
                out.write((ResponseStatus.METHOD_NOT_ALLOWED.getResponse()).getBytes());
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Request parseRequest(InputStream in) throws IOException, BadRequestException, MethodNotAllowedException, FileUploadException {

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
        Method method;
        try {
            method = Method.valueOf(requestLine[0]);
        } catch (IllegalArgumentException e) {
            throw new MethodNotAllowedException();
        }
        var path = requestLine[1];
        if (!path.startsWith("/")) {
            throw new BadRequestException();
        }
        var index = path.indexOf('?');
        String params = "";
        if (index > -1) {
            params = path.substring(index + 1);
            path = path.substring(0, index);
        }
        final var protocol = requestLine[2];
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
        String charset = StandardCharsets.UTF_8.toString();
        String contentType = requestHeaders.get("Content-Type");
        String enctype = contentType;
        if (method == Method.POST) {
            params = "";
            final var length = Integer.parseInt(requestHeaders.get("Content-Length"));
            in.skip(requestHeadersDelimiter.length);
            if (contentType.contains("; ")) {
                String[] contents = contentType.split("; ");
                enctype = contents[0];
                if (contentType.contains("charset")) charset = contents[1].split("=")[1];
            }
            if (("application/x-www-form-urlencoded").equals(enctype)) {
                params = new String(in.readNBytes(length));
            }
        }
        var requestParams = parseRequestParams(params);
        var request = new Request(method, path, protocol, requestParams, requestHeaders, in, charset);
        if ("multipart/form-data".equals(enctype)) {
            parseMultipart(request);
        }
        return request;
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
        Arrays.asList(headers.split("\r\n")).forEach(s -> {
            String[] pair = s.split(":", 2);
            result.put(pair[0], pair[1].trim());
        });
        return result;
    }

    private Map<String, List<String>> parseRequestParams(String requestParams) {
        Map<String, List<String>> result = new HashMap<>();
        if (!requestParams.isEmpty()) {
            URLEncodedUtils.parse(requestParams, StandardCharsets.UTF_8)
                    .forEach(nameValuePair -> {
                        if (result.containsKey(nameValuePair.getName())) {
                            result.get(nameValuePair.getName()).add(nameValuePair.getValue());
                        } else {
                            List<String> list = new LinkedList<>();
                            list.add(nameValuePair.getValue());
                            result.put(nameValuePair.getName(), list);
                        }
                    });
        }
        return result;
    }

    private void parseMultipart(Request request) throws FileUploadException {
        var list = new FileUpload(new DiskFileItemFactory()).parseRequest(request);
        for (FileItem item : list) {
            if (item.getContentType() == null) {
                request.putRequestParam(item.getFieldName(), item.getString());
            } else {
                request.putFile(item.getName(), item);
            }
        }
    }
}
