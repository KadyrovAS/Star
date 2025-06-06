package star.service;

import org.springframework.stereotype.Service;
import star.repository.RulesRepository;

import java.util.List;
import java.util.UUID;


@Service
public class RulesService{
    private final RulesRepository repository;

    public RulesService(RulesRepository repository) {
        this.repository = repository;
    }

    public List<String> getProductsType(UUID id){

    }
}
