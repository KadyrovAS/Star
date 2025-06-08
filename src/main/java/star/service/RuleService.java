package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.model.Rule;
import star.repository.RulesRepository;

import java.util.List;

@Service
public class RuleService {
    private final RulesRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    public RuleService(RulesRepository repository) {
        this.repository = repository;
    }

    public List<Rule> getRules(){
        return repository.getRules();
    }

    public Rule addRule(Rule rule){

        return repository.addRule(rule);
    }

    public void deleteRule(String ruleName){
        logger.info(ruleName);
        repository.delete(ruleName);
    }
}
