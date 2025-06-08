package star.service;

import org.springframework.stereotype.Service;
import star.model.UserTransaction;
import star.repository.TransactionsRepository;

import java.util.List;

@Service
public class UserTransactions {
    private final TransactionsRepository transactionsRepository;
    private List<UserTransaction> listTransactions;

    public UserTransactions(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }


    private void initListTransactions(){

    }
}
