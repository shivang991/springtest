package cloud.myappcollection.springtest.runner;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record Runner(
        @Positive int id,
        @NotBlank String name,
        @PastOrPresent LocalDate date) {
}
