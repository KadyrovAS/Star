package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.repository.CheckRepository;

import java.util.List;
import java.util.UUID;


@Service
public class CheckService{
    private final CheckRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CheckService.class);

    public CheckService(CheckRepository repository) {
        this.repository = repository;
    }

    public List<String> getTypes(UUID id){
        logger.info("getTypes: id = " + id);
        return repository.getTypes(id);
    }

    public Integer getSum(UUID id, String type){
        logger.info("id = ?, type = '?'", id, type);
        return repository.getSum(id, type);
    }

    public List<AmountByType>getAmountsByTypes(UUID id){
        logger.info("id = " + id);
        return repository.getAmountsByTypes(id);
    }
}
