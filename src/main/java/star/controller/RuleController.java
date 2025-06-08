package star.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import star.model.Rule;
import star.service.RuleService;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleController {
    private final RuleService service;

    public RuleController(RuleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Rule>>getRules(){
        List<Rule>list = service.getRules();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public Rule addRule(@RequestBody Rule rule){
        return service.addRule(rule);
    }

    @DeleteMapping
    public void delete(@RequestParam String ruleName){
        service.deleteRule(ruleName);
    }
}
