package com.omeda;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testJaxRsIT {

    private static Client client;
    private static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    static void init() {
        client = ClientBuilder.newClient();
    }

    @AfterAll
    static void cleanup() {
        client.close();
    }

    @Test
    void testGetInternalSourceFileDetails() {
        Response response = client.target(BASE_URL + "/upload-file/internal/source-number/1")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }
}
