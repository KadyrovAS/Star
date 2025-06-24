package star.part02;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import star.part02.model.Recommendation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StarApplicationRestTemplateTest{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private UUID id = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
    private UUID expectedId1 = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private UUID expectedId2 = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");

    private String getUrl(){
        return "http://localhost:" + port + "/part02/recommendation/" + id;
    }

    @Test
    public void testRecommendationControllerIsOk(){
        ResponseEntity<List<Recommendation>> response = restTemplate.exchange(
                getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRecommendationControllerNotNull(){
        ResponseEntity<List<Recommendation>> response = restTemplate.exchange(
                getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertNotNull(response.getBody());
    }

    @Test
    public void testRecommendationControllerEqualsId(){
        ResponseEntity<List<Recommendation>> response = restTemplate.exchange(
                getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        List<Recommendation> recommendations = response.getBody();
        List<UUID> receivedIds = recommendations.stream()
                .map(Recommendation::getId)
                .collect(Collectors.toList());


        assertTrue(receivedIds.contains(expectedId1));
        assertTrue(receivedIds.contains(expectedId2));
    }

    @Test
    public void testRecommendationControllerGetAllRecommendations(){
        ResponseEntity<List<Recommendation>> response = restTemplate.exchange(
                getUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertEquals(2, response.getBody().size());
    }

}
