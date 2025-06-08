package star.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import star.repository.RecommendationExampleRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class RecommendationExampleService {
    private final RecommendationExampleRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(RecommendationExampleService.class);

    public RecommendationExampleService(RecommendationExampleRepository repository) {
        this.repository = repository;
    }


    public void addRecommendation(){
        logger.info("addRecommendation");
        try(FileReader fileReader = new FileReader("src/main/resources/example.sql");
            BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String line = bufferedReader.readLine();
            while (line != null){
                repository.addRecommendationExample(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

}
