package star.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import star.repository.RecommendationsRepository;
import org.slf4j.LoggerFactory;


import java.util.UUID;

@Service
public class StarService{
    private final RecommendationsRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(StarService.class);

    public StarService(RecommendationsRepository repository) {
        this.repository = repository;
    }

    public Integer getRandomTransactionAmount(UUID uuid){
        logger.info("getRandomTransactionAmount: " + uuid);
        return repository.getRandomTransactionAmount(uuid);
    }

}
