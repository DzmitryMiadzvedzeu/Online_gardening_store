package org.shop.com.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.com.entity.ProductEntity;
import org.shop.com.repository.ProductJpaRepository;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductJpaRepository repository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        List<ProductEntity> testProducts = new ArrayList<>();
        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("293"));
        testProducts.add(product);
        when(repository.findAll(Sort.unsorted())).thenReturn(testProducts);
    }

    @Test
    void whenGetAllCalledWithoutFilters_thenReturnAllProducts() {
        List<ProductEntity> products = productService.getAll(null, null, null, null, null);

        assertThat(products).isNotNull().hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Test Product");
        assertThat(products.get(0).getPrice()).isEqualTo(new BigDecimal("293"));

        verify(repository).findAll(Sort.unsorted());
    }
    private static Stream<String> generateSortFields() {
        String[] fields = {"name", "price", "createdAt", "updatedAt", "discountPrice"};
        return generatePermutations(fields, fields.length).stream()
                .map(arr -> String.join(",", arr));
    }

    private static Collection<String[]> generatePermutations(String[] arr, int length) {
        if (length == 1) {
            return Arrays.stream(arr)
                    .map(e -> new String[]{e})
                    .collect(Collectors.toList());
        }
        return Arrays.stream(arr)
                .flatMap(e -> generatePermutations(arr, length - 1).stream()
                        .map(p -> {
                            String[] newArr = new String[length];
                            newArr[0] = e;
                            System.arraycopy(p, 0, newArr, 1, length - 1);
                            return newArr;
                        }))
                .collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource("generateSortFields")
    void whenGetAllWithSort_thenCorrectlySorted(String sortFields) {

        productService.getAll(null, null, null, null, sortFields);

        String[] fields = sortFields.split(",");


        Sort sort = Sort.by(fields);
        verify(repository, Mockito.times(1)).findAll(sort);
    }

}