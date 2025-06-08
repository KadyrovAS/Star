package star.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.model.Rule;
import star.model.RuleToRecommendation;
import star.service.RecommendationByRulesService;
import star.service.RuleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendationByRules")
public class RecommendationByRulesController {
    private final RecommendationByRulesService service;

    public RecommendationByRulesController(RecommendationByRulesService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<RuleToRecommendation>> getRules() {
        List<RuleToRecommendation> list = service.getRules();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public RuleToRecommendation addRule(@RequestBody RuleToRecommendation rule) {
        return service.addRule(rule);
    }

    @DeleteMapping
    public void delete(@RequestParam UUID id) {
        service.deleteRule(id);
    }
}
