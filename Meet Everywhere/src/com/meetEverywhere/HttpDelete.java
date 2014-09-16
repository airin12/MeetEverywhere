package com.meetEverywhere;

import org.apache.http.client.methods.HttpPost;

public class HttpDelete extends HttpPost {

    public static final String METHOD_DELETE = "DELETE";

    public HttpDelete(final String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return METHOD_DELETE;
    }
}
