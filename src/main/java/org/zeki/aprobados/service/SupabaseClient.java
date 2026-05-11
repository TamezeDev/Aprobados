package org.zeki.aprobados.service;

import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Getter
public class SupabaseClient {

    protected static final String SUPABASE_BASE = "https://okylxchuzbmuanmmxjcf.supabase.co";
    protected static final String AUTH_URL = SUPABASE_BASE + "/auth/v1/";
    protected static final String RPC_URL = SUPABASE_BASE + "/rest/v1/rpc/";
    private final String ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9reWx4Y2h1emJtdWFubW14amNmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzc4MTgyNjYsImV4cCI6MjA5MzM5NDI2Nn0.89vgwhuTLIIwSBmrjAiZMfFBiEj2cQZFmmjLqhPwkBw";

    private final HttpClient client = HttpClient.newHttpClient();

    protected HttpResponse<String> getJson(String url, String jwt) throws Exception {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .uri(URI.create(url))
                .header("apikey", ANON_KEY)
                .header("Authorization", "Bearer " + jwt)
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> postJsonPublic(String url, String body) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", ANON_KEY)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> postJson(String url, String body, String jwt) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", ANON_KEY)
                .header("Authorization", "Bearer " + jwt)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> postJson(String url, String json) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", ANON_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

//    public HttpResponse<String> delete(String url) throws Exception {
//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().build();
//        return client.send(request, HttpResponse.BodyHandlers.ofString());
//    }
}
