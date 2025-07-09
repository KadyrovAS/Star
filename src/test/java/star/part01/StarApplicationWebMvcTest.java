package star.part01;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import star.part01.controller.RecommendationControllerPart01;
import star.part01.model.Recommendation;
import star.part01.model.Recommendations;
import star.part01.service.RecommendationRuleSet;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RecommendationControllerPart01.class)
@Import(RecommendationControllerPart01.class)
public class StarApplicationWebMvcTest{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @Qualifier("recommendationsChoice")
    private RecommendationRuleSet service;

    private UUID id = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    public void getRecommendationsTest_ShouldReturnRecommendations() throws Exception {
        Recommendation[] testRecommendations = {
                new Recommendation(UUID.randomUUID(), "Type1", "Annotation1"),
                new Recommendation(UUID.randomUUID(), "Type2", "Annotation2")
        };
        Recommendations expectedResponse = new Recommendations(id, testRecommendations);

        Mockito.when(service.getRecommendation(any(UUID.class)))
                .thenReturn(Optional.of(expectedResponse));

        mockMvc.perform(get("/part01/recommendation/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(id.toString()))
                .andExpect(jsonPath("$.recommendations.length()").value(2))
                .andExpect(jsonPath("$.recommendations[0].name").value("Type1"))
                .andExpect(jsonPath("$.recommendations[1].name").value("Type2"));
    }

    @Test
    public void getRecommendationsTest_ShouldReturnEmptyArrayWhenNoRecommendations() throws Exception {
        Mockito.when(service.getRecommendation(any(UUID.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/part01/recommendation/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(id.toString()))
                .andExpect(jsonPath("$.recommendations.length()").value(0));
    }

    @Test
    public void getRecommendationsTest_ShouldHandleInvalidIdFormat() throws Exception {
        mockMvc.perform(get("/part01/recommendation/{id}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
