package org.shop.com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryCreateDTO {

    @NotBlank(message = "Name must not be blank")
    private String name;
    public CategoryCreateDTO() {}
    public CategoryCreateDTO(String name) {
        this.name = name;
    }

}