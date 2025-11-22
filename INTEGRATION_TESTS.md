# Integration Tests for JAX-RS Resources

This repository contains integration tests for Jakarta JAX-RS endpoints.

## Generated Integration Tests

### 1. OdsElementServiceIT.java
Located at: `/OdsElementServiceIT.java`

This integration test class covers the `OdsElementService` JAX-RS resource (from `testfile.java`), which includes:

**Test Coverage (48 test methods):**
- Audience management endpoints (GET, PATCH)
- Email element operations (GET, PUT, POST)
- SMS element operations (GET, PUT, POST)
- Tester and seed management
- Link tracking configuration
- File upload operations
- Bee plugin integration
- Fatigue filter operations
- Web tracking configuration

**Base URL:** `http://localhost:8080/api/element`

### 2. testJaxRsIT.java  
Located at: `/testJaxRsIT.java`

This integration test class covers the `testJaxRs` JAX-RS resource from `untitled/src/main/java/com/omeda/testJaxRs.java`.

**Test Coverage (1 test method):**
- GET endpoint for internal source file details

**Base URL:** `http://localhost:8080`

## Test Framework

All integration tests use:
- **JUnit 5** (`org.junit.jupiter.api.*`)
- **Jakarta JAX-RS Client API** for HTTP testing
- **Jersey Client** implementation

## Dependencies Added

The following dependencies were added to `untitled/pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>jakarta.ws.rs</groupId>
    <artifactId>jakarta.ws.rs-api</artifactId>
    <version>3.1.0</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-client</artifactId>
    <version>3.1.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.inject</groupId>
    <artifactId>jersey-hk2</artifactId>
    <version>3.1.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-jackson</artifactId>
    <version>3.1.3</version>
    <scope>test</scope>
</dependency>
```

## Test Class Structure

Each integration test class follows this pattern:

```java
public class ResourceNameIT {
    private static Client client;
    private static final String BASE_URL = "http://localhost:8080/api/resource";

    @BeforeAll
    static void init() {
        client = ClientBuilder.newClient();
    }

    @AfterAll
    static void cleanup() {
        client.close();
    }

    @Test
    void testEndpointName() {
        Response response = client.target(BASE_URL + "/endpoint")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }
}
```

## Test Methods

Test methods are named after the endpoint and HTTP method they test:
- `testGetAudience()` - Tests GET request
- `testSaveAudienceElement()` - Tests PATCH request
- `testSendToOne()` - Tests POST request
- etc.

## Running the Tests

### Prerequisites
1. Ensure the JAX-RS application is running on `http://localhost:8080`
2. Ensure all dependencies are installed

### Execute Tests
```bash
cd untitled
mvn test
```

## Notes

- All test methods include at least one assertion validating the HTTP response status
- Sample payloads are provided for POST/PUT endpoints
- Tests are syntactically correct and can compile without manual modification
- Tests follow Jakarta JAX-RS Client API patterns
- Path parameters and query parameters are properly configured
