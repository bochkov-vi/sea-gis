package com.bochkov.sea.mvc;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

/**
 * Created by bochkov on 28.06.17.
 */
@Controller
public class ProxyController {
    HttpClient httpClient = HttpClients.createDefault();
    @Autowired
    GeoserverConfig geoserverConfig;

    @RequestMapping("proxy")
    public void proxyRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpUriRequest proxiedRequest = createHttpUriRequest(request);
            HttpResponse proxiedResponse = httpClient.execute(proxiedRequest);
            writeToResponse(proxiedResponse, response);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("downloadWms")
    public ModelAndView downloadWms(HttpServletRequest request, HttpServletResponse response){
        return new ModelAndView("wmsView");
    }

    private void writeToResponse(HttpResponse proxiedResponse, HttpServletResponse response) {
        for (Header header : proxiedResponse.getAllHeaders()) {
            response.addHeader(header.getName(), header.getValue());
        }
        OutputStream os = null;
        InputStream is = null;
        try {
            is = proxiedResponse.getEntity().getContent();
            os = response.getOutputStream();
            IOUtils.copy(is, os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private HttpUriRequest createHttpUriRequest(HttpServletRequest request) throws URISyntaxException {
        URI uri = new URI(geoserverConfig.getUrl() + "/wms?" + request.getQueryString());

        RequestBuilder rb = RequestBuilder.create(request.getMethod());
        rb.setUri(uri);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            rb.addHeader(headerName, headerValue);
        }

        HttpUriRequest proxiedRequest = rb.build();
        return proxiedRequest;
    }
}
