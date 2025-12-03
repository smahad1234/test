package test;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.http.HttpRequest;

public class AudienceElementServiceTest {

    @Test
    public void test_loadAudience_valid() throws IOException, InterruptedException {
        HttpRequest request = newBuilderBase(baseUrl + "/" + elementId + "/audience?environmentId=" +
                environmentId).GET().build();

        HttpResponse<Supplier<AudienceElementSchema>> response =
                sendRequest("audienceLoad 200", request, JsonBodyHandler.of(AudienceElementSchema.class));
        Assert.assertEquals("HTTP response 200", 200, response.statusCode());

        AudienceElementSchema element = response.body().get();
        validateAudienceElementBasics(element, true, true, true);
        Assert.assertTrue("element count missing", element.getCombinedAudienceCount()>0);
        Assert.assertFalse("element desc missing", element.getElementDescription().isBlank());
        Assert.assertNotNull("element hardBounce missing", element.getInternalHardBounceFilter());
        Assert.assertNotNull("element reenterAudience missing", element.getReenterAudience());
        Assert.assertNotNull("element reenterLimit missing", element.getReenterLimit());
        Assert.assertNotNull("element reenterMonth missing", element.getReenterMonth());
        Assert.assertTrue("element reenterWeek missing", element.getReenterWeek()>0);
        Assert.assertNotNull("element statusCode missing", element.getStatusCode());
        Assert.assertFalse("element statusCodeDesc missing", element.getStatusCodeDescription().isBlank());
        Assert.assertFalse("element voyageType missing", element.getAudienceType().isBlank());
        Assert.assertFalse("element weekDays missing", element.getWeekdays().isEmpty());

        AudienceSourceSchema source = null;
        for (AudienceSourceSchema s : element.getAudienceSources()) {
            if (s.getSourceNumber()==1) {
                source = s;
                break;
            }
        }
        Assert.assertNotNull("source null", source);
        Assert.assertTrue("source nth missing", source.getAudienceNthCount()>0);
        Assert.assertNotNull("source outputAllowed missing", source.getIsOutputCriteriaAllowed());
        Assert.assertTrue("source outputId missing", source.getOutputCriteriaId()>0);
        Assert.assertFalse("source outputName missing", source.getOutputCriteriaName().isBlank());
        Assert.assertFalse("source outputHeaders missing", source.getOutputHeaders().isBlank());
        Assert.assertTrue("source selectionCount missing", source.getSelectionCriteriaCount()>0);
        Assert.assertTrue("source selectionId missing", source.getSelectionCriteriaId()>0);
        Assert.assertFalse("source selectionName missing", source.getSelectionCriteriaName().isBlank());
        Assert.assertTrue("source selectionDataView missing", source.getSelectionCriteriaDataViewId()>0);

        for (SuppressionSourceSchema supp : element.getSuppressionSources()) {
            Assert.assertTrue("suppressionCount Wrong", supp.getSuppressionCount()>0);

            if (supp.getSourceNumber()==1) {
                Assert.assertFalse("suppression filename missing", supp.getSuppressionFileName().isBlank());
                Assert.assertEquals("suppression type wrong", 2, (int) supp.getSuppressionType());
            } else {
                Assert.assertTrue("suppression dataViewId missing", supp.getSuppressionDataViewId()>0);
                Assert.assertEquals("suppression type wrong", 0, (int) supp.getSuppressionType());
                Assert.assertTrue("suppression selectionId missing", supp.getSuppressionSelectionCriteriaId()>0);
                Assert.assertFalse("suppression selectionName missing", supp.getSuppressionSelectionCriteriaName().isBlank());
            }
        }
    }

    @Test
    public void test_getAudience_valid() throws IOException, InterruptedException {
        // Test for GET endpoint at {elementId}/test
        Integer elementId = 1;
        String baseUrl = "http://localhost:8080/api";

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + "/" + elementId + "/test"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());

        Assert.assertNotNull("Response should not be null", response);
        Assert.assertEquals("HTTP response 200", 200, response.statusCode());
        Assert.assertNotNull("Response body should not be null", response.body());
    }

    @Test
    public void test_getAudience_invalidElementId() throws IOException, InterruptedException {
        // Test for GET endpoint with invalid elementId
        Integer invalidElementId = -1;
        String baseUrl = "http://localhost:8080/api";

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + "/" + invalidElementId + "/test"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());

        Assert.assertNotNull("Response should not be null", response);
        // Expecting either 400 Bad Request or 404 Not Found for invalid element ID
        Assert.assertTrue("HTTP response should indicate error", 
                response.statusCode() == 400 || response.statusCode() == 404);
    }

    @Test
    public void test_getAudience_withDifferentElementId() throws IOException, InterruptedException {
        // Test for GET endpoint with different valid elementId
        Integer elementId = 100;
        String baseUrl = "http://localhost:8080/api";

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + "/" + elementId + "/test"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());

        Assert.assertNotNull("Response should not be null", response);
        Assert.assertNotNull("Response body should not be null", response.body());
    }
}
