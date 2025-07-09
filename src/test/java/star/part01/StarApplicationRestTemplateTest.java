package star.part01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import star.part01.model.Recommendations;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StarApplicationRestTemplateTest{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID id = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    private String getUrl() {
        return "http://localhost:" + port + "/part01/recommendation/" + id;
    }

    @Test
    public void testRecommendationControllerIsOk() {
        ResponseEntity<Recommendations> response = restTemplate.getForEntity(
                getUrl(),
                Recommendations.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRecommendationControllerNotNull() {
        ResponseEntity<Recommendations> response = restTemplate.getForEntity(
                getUrl(),
                Recommendations.class
        );
        assertNotNull(response.getBody());
    }

    @Test
    public void testRecommendationControllerEqualsId() {
        ResponseEntity<Recommendations> response = restTemplate.getForEntity(
                getUrl(),
                Recommendations.class
        );
        assertEquals(id, response.getBody().getUser_id());
    }

    @Test
    public void testRecommendationControllerGetAllRecommendations() {
        ResponseEntity<Recommendations> response = restTemplate.getForEntity(
                getUrl(),
                Recommendations.class
        );
        assertEquals(2, response.getBody().getRecommendations().length);
    }

}
