package util;

import java.util.Map;
import java.util.Set;

public class Request {

    private final Method method;
    private final String path;
    private final String protocolVersion;
    private final Map<String, String> requestParams;
    private final Map<String, String> requestHeaders;
    private final byte[] requestBody;

    public Request(Method method, String path, String protocolVersion, Map<String, String> requestParams, Map<String, String> requestHeaders, byte[] requestBody) {
        this.method = method;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.requestParams = requestParams;
        this.requestHeaders = requestHeaders;
        this.requestBody = requestBody;
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
    public String getRequestParam(String param) {
        return requestParams.get(param);
    }
    public Set<String> getRequestHeaders() {
        return requestHeaders.keySet();
    }
    public String getRequestHeader(String header) {
        return requestHeaders.get(header);
    }
    public byte[] getRequestBody() {
        return requestBody;
    }
}
