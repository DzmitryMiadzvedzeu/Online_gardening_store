package org.shop.com.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDTO {
    // Getters and setters
    private Long categoryId;
    private String name;

    // Constructors, getters, setters
    public CategoryDTO() {}

    public CategoryDTO(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

}