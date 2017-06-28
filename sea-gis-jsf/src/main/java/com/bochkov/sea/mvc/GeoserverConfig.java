package com.bochkov.sea.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by bochkov on 28.06.17.
 */
@Component
public class GeoserverConfig implements Serializable {
    @Value("http://localhost:8080/geoserver")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
