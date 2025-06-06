package star.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import star.service.AmountByType;
import star.service.CheckService;

import java.util.List;
import java.util.UUID;

@RestController
public class CheckController{
    private final CheckService service;

    public CheckController(CheckService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}types")
    public ResponseEntity<List<String>> getProductTypes(@PathVariable UUID id){
        List<String>types = service.getTypes(id);
        return ResponseEntity.ok(types);
    }

    @GetMapping(value = "/getAmount")
    public ResponseEntity<Integer>getAmount(
            @RequestParam("id")UUID id,
            @RequestParam("type")String type
    ){
        Integer amount = service.getSum(id, type);
        return ResponseEntity.ok(amount);
    }

    @GetMapping(value = "/{id}AmountByTypes")
    public ResponseEntity<List<AmountByType>> getAmountsByTypes(@PathVariable UUID id){
        List<AmountByType>result = service.getAmountsByTypes(id);
        return ResponseEntity.ok(result);
    }
}
