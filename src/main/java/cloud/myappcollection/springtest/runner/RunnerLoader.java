package cloud.myappcollection.springtest.runner;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RunnerLoader implements CommandLineRunner {

    private final RunnerRepository runnerRepository;
    private final ObjectMapper objectMapper;

    public RunnerLoader(RunnerRepository runnerRepository, ObjectMapper objectMapper) {
        this.runnerRepository = runnerRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        if (runnerRepository.count() > 0)
            return;
        try (InputStream stream = TypeReference.class.getResourceAsStream("/data/runners.json")) {
            Runner[] runners = objectMapper.readValue(stream.readAllBytes(), Runner[].class);
            runnerRepository.insert(runners);
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
