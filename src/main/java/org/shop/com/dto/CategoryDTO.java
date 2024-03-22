package org.shop.com.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryDTO {

    private Long categoryId;
    private String name;


    public CategoryDTO(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

}