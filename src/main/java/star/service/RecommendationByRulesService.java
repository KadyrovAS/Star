package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.model.RuleToRecommendation;
import star.repository.RecommendationByRulesRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationByRulesService {
    private final RecommendationByRulesRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    public RecommendationByRulesService(RecommendationByRulesRepository repository) {
        this.repository = repository;
    }

    public List<RuleToRecommendation> getRules(){
        return repository.getRules();
    }

    public RuleToRecommendation addRule(RuleToRecommendation rule){
        return repository.addRule(rule);
    }

    public void deleteRule(UUID id){
        repository.delete(id);
    }
}
