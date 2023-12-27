package ca.cmpt213.as5courseplanner;

import ca.cmpt213.as5courseplanner.model.Model;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootApplication
public class Application {
    private File DATA_FILE = new File("data/course_data_2018.csv");
//    private File DATA_FILE = new File("data/course_data_2016.csv");

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Model getModel() throws FileNotFoundException {
        Model model = new Model(DATA_FILE);
        model.dumpModelToConsole();
        return model;
    }
}

