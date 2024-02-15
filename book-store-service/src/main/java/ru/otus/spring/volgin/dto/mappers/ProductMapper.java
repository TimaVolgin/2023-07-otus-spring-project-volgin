package ru.otus.spring.volgin.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.otus.spring.volgin.domain.Book;
import ru.otus.spring.volgin.domain.Product;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.product.ProductItemFull;

import java.util.List;

/**
 * Конвертер продуктов
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Конвертирует продукт в dto
     * @param products продукты
     * @return продукт dto
     */
    ProductItem productToProductItem(Product products);

    /**
     * Конвертирует продукты в dto
     * @param products продукты
     * @return продукты dto
     */
    List<ProductItem> productsToProductItems(List<Product> products);

    /**
     * Конвертирует продукт в dto максимально полное для отображения
     * @param product продукт
     * @return продукт dto
     */
    @Mapping(target = "books", qualifiedByName = "booksToBookIds")
    ProductItemFull productToProductItemFull(Product product);

    /**
     * Обновляет сущность на основе DTO
     * @param source     dto
     * @param books родительские категории
     * @param product    обновляемая сущность
     */
    @Mapping(target = "books", source = "books")
    void updateProductFromDto(ProductItemFull source, List<Book> books, @MappingTarget Product product);

    /**
     * Конвертирует книги в идентификаторы книг
     * @param books категории
     * @return идентификаторы категорий
     */
    @Named("booksToBookIds")
    default List<Long> booksToBookIds(List<Book> books) {
        return books.stream().map(Book::getId).toList();
    }
}
