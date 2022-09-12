package util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class Request implements RequestContext {

    private final Method method;
    private final String path;
    private final String protocolVersion;
    private final Map<String, List<String>> requestParams;
    private final Map<String, String> requestHeaders;
    private final Map<String, FileItem> files;
    private final InputStream in;
    private final String charset;

    public Request(Method method, String path, String protocolVersion, Map<String, List<String>> requestParams, Map<String, String> requestHeaders, InputStream in, String charset) throws FileUploadException {
        files = new HashMap<>();
        this.method = method;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.requestParams = requestParams;
        this.requestHeaders = requestHeaders;
        this.in = in;
        this.charset = charset;
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Set<String> getRequestParams() {
        return requestParams.keySet();
    }

    public List<String> getRequestParam(String param) {
        return requestParams.get(param);
    }

    public void putRequestParam(String name, String value) {
        if (requestParams.containsKey(name)) {
            requestParams.get(name).add(value);
        } else {
            List<String> values = new LinkedList<>();
            values.add(value);
            requestParams.put(name, values);
        }
    }

    public Set<String> getRequestHeaders() {
        return requestHeaders.keySet();
    }

    public String getRequestHeader(String header) {
        return requestHeaders.get(header);
    }

    public Set<String> getFilesName() {
        return files.keySet();
    }

    public FileItem getFile(String name) {
        return files.get(name);
    }

    public void putFile(String name, FileItem item) {
        files.put(name, item);
    }

    @Override
    public String getCharacterEncoding() {
        return charset;
    }

    @Override
    public String getContentType() {
        return requestHeaders.get("Content-Type");
    }

    @Override
    public int getContentLength() {
        return Integer.parseInt(requestHeaders.get("Content-Length"));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return in;
    }
}