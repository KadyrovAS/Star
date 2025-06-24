package star.part02;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import star.part02.controller.RecommendationControllerPart02;
import star.part02.model.Recommendation;
import star.part02.model.Rule;
import star.part02.service.RecommendationRuleSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RecommendationControllerPart02.class)
@Import(RecommendationControllerPart02.class)
public class StarApplicationWebMvcTest{
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @Qualifier("servicePart02")
    private RecommendationRuleSet service;

    private UUID id = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

    @Test
    public void getRecommendationsTest_ShouldReturnRecommendations() throws Exception {
        List<Recommendation> expectedResponse = List.of(
                new Recommendation(UUID.randomUUID(), "Type1", "Annotation1", new Rule[3]),
                new Recommendation(UUID.randomUUID(), "Type2", "Annotation2", new Rule[3])
        );

        Mockito.when(service.findRecommendationById(any(UUID.class)))
                .thenReturn(Optional.of(expectedResponse));

        mockMvc.perform(get("/part02/recommendation/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Type1"))
                .andExpect(jsonPath("$[1].name").value("Type2"));
    }

    @Test
    public void getRecommendationsTest_ShouldReturnEmptyArrayWhenNoRecommendations() throws Exception {
        Mockito.when(service.findRecommendationById(any(UUID.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/part02/recommendation/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void getRecommendationsTest_ShouldHandleInvalidIdFormat() throws Exception {
        mockMvc.perform(get("/part02/recommendation/{id}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
