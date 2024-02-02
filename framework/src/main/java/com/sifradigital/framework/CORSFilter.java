package com.sifradigital.framework;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class CORSFilter implements ContainerResponseFilter {

    private String origin = "*";
    private String headers = "origin, content-type, accept, authorization";
    private String methods = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
  
    
    public CORSFilter() {
        
    }

    public CORSFilter(String origin) {
        this.origin = origin;
    }

    public CORSFilter(String origin, String headers, String methods) {
        this.origin = origin;
        this.headers = headers;
        this.methods = methods;
    }

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        response.getHeaders().add("Access-Control-Allow-Origin", origin);
        response.getHeaders().add("Access-Control-Allow-Headers", headers);
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", methods);
    }
}