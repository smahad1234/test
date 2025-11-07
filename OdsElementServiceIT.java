package com.omeda;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OdsElementServiceIT {

    private static Client client;
    private static final String BASE_URL = "http://localhost:8080/api/element";

    @BeforeAll
    static void init() {
        client = ClientBuilder.newClient();
    }

    @AfterAll
    static void cleanup() {
        client.close();
    }

    @Test
    void testGetAudience() {
        Response response = client.target(BASE_URL + "/1/audience")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveAudienceElement() {
        Entity<String> payload = Entity.json("{\"elementId\":1}");
        Response response = client.target(BASE_URL + "/1/audience")
                .request()
                .method("PATCH", payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetOutputCriteriaList() {
        Response response = client.target(BASE_URL + "/output-criterias")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testRefreshAudienceCount() {
        Response response = client.target(BASE_URL + "/selection-criteria/1/count")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetTestHistory() {
        Response response = client.target(BASE_URL + "/test-history")
                .queryParam("trackingNumber", "TEST123")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetDeploymentTypes() {
        Response response = client.target(BASE_URL + "/email/deployment-types")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetEmailDefaultDetails() {
        Response response = client.target(BASE_URL + "/email/email-default")
                .queryParam("deploymentTypeId", "1")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetEmailElementDetails() {
        Response response = client.target(BASE_URL + "/1/email")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetLinkTrackingDetails() {
        Response response = client.target(BASE_URL + "/1/email/link-tracking")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetTesterSeedDetails() {
        Response response = client.target(BASE_URL + "/1/email/tester-seed")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveEmailElementDetails() {
        Entity<String> payload = Entity.json("{\"elementId\":1,\"subject\":\"Test\"}");
        Response response = client.target(BASE_URL + "/1/email")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testRetrieveContentViaUrl() {
        Entity<String> payload = Entity.json("{\"url\":\"http://example.com/content\"}");
        Response response = client.target(BASE_URL + "/1/email/message-content/url")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveLinkTrackingDetails() {
        Entity<String> payload = Entity.json("{\"trackingEnabled\":true}");
        Response response = client.target(BASE_URL + "/1/email/link-tracking")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveTesterSeedDetails() {
        Entity<String> payload = Entity.json("{\"testers\":[]}");
        Response response = client.target(BASE_URL + "/1/email/tester-seed")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testContactSearchTestSeed() {
        Entity<String> payload = Entity.json("{\"searchCriteria\":\"test\"}");
        Response response = client.target(BASE_URL + "/1/email/tester-seed/contact-search")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testAddContactSearchTestSeed() {
        Entity<String> payload = Entity.json("{\"seedId\":123}");
        Response response = client.target(BASE_URL + "/1/email/tester-seed/contact-search/add-seed")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testAddNewTestSeedFromForm() {
        Entity<String> payload = Entity.json("{\"email\":\"test@example.com\",\"name\":\"Test User\"}");
        Response response = client.target(BASE_URL + "/1/email/tester-seed/add-seed-from-form")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSendToOne() {
        Entity<String> payload = Entity.json("{\"email\":\"test@example.com\"}");
        Response response = client.target(BASE_URL + "/1/email/send-to-one")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSendTest() {
        Entity<String> payload = Entity.json("{\"testMode\":true}");
        Response response = client.target(BASE_URL + "/1/email/send-test")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testContinueSendTest() {
        Response response = client.target(BASE_URL + "/1/email/send-test/continue")
                .queryParam("isUnsubscribe", "false")
                .queryParam("isNonMergeValueForLinkPara", "false")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetLinkTrackingDefaultCategories() {
        Response response = client.target(BASE_URL + "/1/email/link-tracking/default-category")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testRetrieveLinkNames() {
        Response response = client.target(BASE_URL + "/1/email/link-tracking/retrieve-link-names")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testChangeStatusCode() {
        Response response = client.target(BASE_URL + "/1/status-code/2")
                .request()
                .put(Entity.json(""));
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetBeeAuthToken() {
        Response response = client.target(BASE_URL + "/bee/auth-token")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetInternalSourceFileList() {
        Response response = client.target(BASE_URL + "/1/upload-files/internal")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetInternalSourceFileDetails() {
        Response response = client.target(BASE_URL + "/1/upload-file/internal/source-number/1")
                .queryParam("filename", "test.csv")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetSmsMessageTypes() {
        Response response = client.target(BASE_URL + "/sms/message-types")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetSmsElementStandardMergeVars() {
        Response response = client.target(BASE_URL + "/sms/merge-variables")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetSmsElementDetails() {
        Response response = client.target(BASE_URL + "/1/sms")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetSmsLinkTrackingDetails() {
        Response response = client.target(BASE_URL + "/1/sms/link-tracking")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetWebTrackingParameters() {
        Response response = client.target(BASE_URL + "/sms/web-tracking/parameters")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetAllWebTrackingConfigurations() {
        Response response = client.target(BASE_URL + "/sms/web-tracking")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetWebTrackingConfiguration() {
        Response response = client.target(BASE_URL + "/sms/web-tracking/config/1")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetSmsTesterSeedDetails() {
        Response response = client.target(BASE_URL + "/1/sms/tester-seed")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetTesters() {
        Response response = client.target(BASE_URL + "/sms/testers")
                .queryParam("firstName", "John")
                .queryParam("lastName", "Doe")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveSmsElementDetails() {
        Entity<String> payload = Entity.json("{\"elementId\":1,\"message\":\"Test SMS\"}");
        Response response = client.target(BASE_URL + "/1/sms")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveSmsLinkTrackingDetails() {
        Entity<String> payload = Entity.json("{\"trackingEnabled\":true}");
        Response response = client.target(BASE_URL + "/1/sms/link-tracking")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveWebTrackingConfiguration() {
        Entity<String> payload = Entity.json("{\"configName\":\"Test Config\"}");
        Response response = client.target(BASE_URL + "/sms/web-tracking/config")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveSmsTester() {
        Entity<String> payload = Entity.json("{\"firstName\":\"John\",\"phoneNumber\":\"+1234567890\"}");
        Response response = client.target(BASE_URL + "/1/sms/tester-seed/tester")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testAssignExistingTester() {
        Response response = client.target(BASE_URL + "/1/sms/tester-seed/tester/123")
                .request()
                .put(Entity.json(""));
        assertEquals(200, response.getStatus());
    }

    @Test
    void testUpdateSmsTester() {
        Entity<String> payload = Entity.json("{\"firstName\":\"John\",\"phoneNumber\":\"+1234567890\"}");
        Response response = client.target(BASE_URL + "/sms/tester-seed/tester/123")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveSmsTesterSeedDetails() {
        Entity<String> payload = Entity.json("{\"testers\":[]}");
        Response response = client.target(BASE_URL + "/1/sms/tester-seed")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSmsSendToOne() {
        Entity<String> payload = Entity.json("{\"phoneNumber\":\"+1234567890\"}");
        Response response = client.target(BASE_URL + "/1/sms/send-to-one")
                .request()
                .put(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSmsSendTest() {
        Response response = client.target(BASE_URL + "/1/sms/send-test")
                .request()
                .put(Entity.json(""));
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetFatigueFilter() {
        Response response = client.target(BASE_URL + "/1/fatigue-filter")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveFatigueFilter() {
        Entity<String> payload = Entity.json("{\"filterType\":\"daily\"}");
        Response response = client.target(BASE_URL + "/1/fatigue-filter")
                .request()
                .method("PATCH", payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetBeeRows() {
        Response response = client.target(BASE_URL + "/bee/rows")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetBeeRow() {
        Response response = client.target(BASE_URL + "/bee/row/test-guid-123")
                .request()
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void testSaveBeeRow() {
        Entity<String> payload = Entity.json("{\"guid\":\"test-guid-123\",\"content\":\"row content\"}");
        Response response = client.target(BASE_URL + "/bee/row")
                .request()
                .post(payload);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testDeleteBeeRow() {
        Response response = client.target(BASE_URL + "/bee/row/test-guid-123")
                .request()
                .delete();
        assertEquals(200, response.getStatus());
    }
}
