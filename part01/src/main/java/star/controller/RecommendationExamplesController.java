package star.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import star.service.RecommendationExampleService;

@RestController
@RequestMapping("/example")
public class RecommendationExamplesController {
    private final RecommendationExampleService service;

    public RecommendationExamplesController(RecommendationExampleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> insertExampleRecords(){
        service.addExample();
        return ResponseEntity.ok("Записи добавлены");
    }

}
