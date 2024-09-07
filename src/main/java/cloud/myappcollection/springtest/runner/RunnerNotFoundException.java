package cloud.myappcollection.springtest.runner;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RunnerNotFoundException extends RuntimeException {
    RunnerNotFoundException() {
        super("Runner not found!");
    }
}
