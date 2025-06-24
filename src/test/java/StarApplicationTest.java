import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import star.part01.controller.RecommendationControllerPart01;
import star.part01.controller.RecommendationExamplesController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StarApplicationTest{
    @Autowired
    private RecommendationControllerPart01 recommendationController;

    @Autowired
    private RecommendationExamplesController recommendationExamplesController;

    @Test
    void contextRecommendationLoads() {
        assertThat(recommendationController).isNotNull();
    }

    @Test
    void contextRecommendationExamplesController(){
        assertThat(recommendationExamplesController).isNotNull();
    }
}
