package star.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import star.service.StarService;

import java.util.UUID;

@RestController
public class StarController{
    private final StarService service;

    public StarController(StarService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}/transaction")
    public ResponseEntity<Integer>getRandomTransactionAmount(@PathVariable UUID id){
        Integer amount = service.getRandomTransactionAmount(id);
        return ResponseEntity.ok(amount);
    }
}
