package star.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.model.Recommendation;
import star.model.RuleToRecommendation;
import star.service.RecommendationService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
    private final RecommendationService service;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }


    @PostMapping
    public Recommendation addRecommendation(@RequestBody Recommendation recommendation){
        return service.addRecommendation(recommendation);
    }

    @DeleteMapping
    public void delete(@RequestParam UUID id){
        service.deleteRecommendation(id);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Collection<Recommendation>> getRecommendation(@PathVariable UUID id){
        logger.info("Запрос рекомендации для {}", id);
        return ResponseEntity.ok(service.getRecommendation(id));
    }

}
