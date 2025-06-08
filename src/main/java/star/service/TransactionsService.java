package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.model.UserTransaction;
import star.repository.TransactionsRepository;

import java.util.List;
import java.util.UUID;


@Service
public class TransactionsService {
    private final TransactionsRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(TransactionsService.class);

    public TransactionsService(TransactionsRepository repository) {
        this.repository = repository;
    }


    public List<String> getTypes(UUID id) {
        logger.info("getTypes: id = {}", id);
        return repository.getTypes(id);
    }

    public Integer getSum(UUID id, String type) {
        logger.info("id = {}, type = '{}'", id, type);
        return repository.getSum(id, type);
    }

    public List<UserTransaction> getAmountsByTypes(UUID id) {
        logger.info("id = {}", id);
        return repository.getAmountsByTypes(id);
    }

    public List<UUID> getUsers() {
        return repository.getUsers();
    }

}
