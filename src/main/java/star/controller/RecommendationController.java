package star.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.model.Recommendation;
import star.model.Recommendations;
import star.model.RuleToRecommendation;
import star.service.RecommendationService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService service;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Recommendations> getRecommendation(@PathVariable UUID id){
        logger.info("Запрос рекомендации для {}", id);
        return ResponseEntity.ok(service.getRecommendations(id).orElse(new Recommendations(id, new Recommendation[0])));
    }

}
