package org.shop.com.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryCreateDTO {

    private String name;
    public CategoryCreateDTO() {}
    public CategoryCreateDTO(String name) {
        this.name = name;
    }

}