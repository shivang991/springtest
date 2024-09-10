package cloud.myappcollection.springtest.security.model;

import jakarta.validation.constraints.NotBlank;

public record UserCredentials(@NotBlank String name, @NotBlank String password) {
}
