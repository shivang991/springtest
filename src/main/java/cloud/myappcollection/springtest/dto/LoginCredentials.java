package cloud.myappcollection.springtest.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginCredentials(@NotBlank String name, @NotBlank String password) {
}
