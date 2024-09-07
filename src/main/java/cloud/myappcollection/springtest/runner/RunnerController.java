package cloud.myappcollection.springtest.runner;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/run")
public class RunnerController {

    private final RunnerRepository runnerRepository;

    RunnerController(
            RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    @GetMapping("/")
    public List<Runner> getRunners() {
        return runnerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Runner getRunner(@PathVariable int id) {
        return runnerRepository.findOne(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public void createRunner(@Valid @RequestBody Runner runner) {
        runnerRepository.create(runner);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateRunner(@PathVariable int id, @RequestBody Runner runner) {
        runnerRepository.update(id, runner);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRunner(@PathVariable int id) {
        runnerRepository.delete(id);
    }
}
