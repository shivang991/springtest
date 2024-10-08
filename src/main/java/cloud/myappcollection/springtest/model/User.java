package cloud.myappcollection.springtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record User(
                int id,
                String name,
                @JsonIgnore String password) {

}
