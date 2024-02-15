package ru.otus.spring.volgin.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.volgin.domain.Product;
import ru.otus.spring.volgin.domain.QProduct;
import ru.otus.spring.volgin.dto.mappers.ProductMapper;
import ru.otus.spring.volgin.dto.product.ProductFilter;
import ru.otus.spring.volgin.dto.product.ProductItem;
import ru.otus.spring.volgin.dto.product.ProductItemFull;
import ru.otus.spring.volgin.exceptions.ApplicationException;
import ru.otus.spring.volgin.repository.BookRepository;
import ru.otus.spring.volgin.repository.ProductRepository;

import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * Сервис для работы с продуктом
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    /** Конвертер продуктов */
    private final ProductMapper productMapper;
    /** Репозиторий по работе с продуктами */
    private final ProductRepository productRepository;
    /** Репозиторий по работе с категориями */
    private final BookRepository bookRepository;

    @Override
    public Page<ProductItem> getProducts(ProductFilter productFilter, Pageable pageable) {
        var products = productRepository.findAll(getProductPredicate(productFilter), pageable);
        return products.map(productMapper::productToProductItem);
    }

    @Override
    public ProductItemFull getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Товар не найден"));
        return productMapper.productToProductItemFull(product);
    }

    @Override
    public List<ProductItem> getActiveProductsByIds(List<Long> ids) {
        var qProduct = QProduct.product;
        var predicate = Objects.requireNonNull(new BooleanBuilder(qProduct.disabled.eq(false))
                .and(qProduct.id.in(ids))
                .getValue());
        return productMapper.productsToProductItems(productRepository.findAll(predicate));
    }

    @Override
    public Page<ProductItem> getDisabled(Pageable pageable) {
        return productRepository.findAllByDisabledTrue(pageable).map(productMapper::productToProductItem);
    }

    @Override
    @Transactional
    public void disableProducts(List<Long> ids) {
        productRepository.modifyDisabled(ids, true);
    }

    @Override
    @Transactional
    public void restoreDisabledProducts(List<Long> ids) {
        productRepository.modifyDisabled(ids, false);
    }

    @Override
    public void delete(List<Long> ids) {
        var products = productRepository.findAllById(ids);
        if (products.stream().anyMatch(product -> !product.isDisabled())) {
            throw new ApplicationException("Удалить можно только продукты, снятые с витрины");
        }
        productRepository.deleteAll(products);
    }

    @Override
    public ProductItemFull saveProduct(ProductItemFull saveProductRequest) {
        var product = ofNullable(saveProductRequest.getId())
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() ->
                                new ApplicationException("Вы пытаетесь изменить не существующую продукт с идентификатором %s", saveProductRequest.getId())))
                .orElseGet(Product::new);
        var books = ofNullable(saveProductRequest.getBooks())
                .map(bookRepository::findAllById)
                .orElse(null);
        productMapper.updateProductFromDto(saveProductRequest, books, product);
        return productMapper.productToProductItemFull(productRepository.save(product));
    }

    private Predicate getProductPredicate(ProductFilter productFilter) {
        var qProduct = QProduct.product;
        var booleanBuilder = new BooleanBuilder(Expressions.ONE.eq(1));
        if (productFilter.getDisabled() != null) {
            booleanBuilder.and(qProduct.disabled.eq(productFilter.getDisabled()));
        }
        if (productFilter.getBookId() != null) {
            var book = bookRepository.findById(productFilter.getBookId())
                    .orElseThrow(() -> new ApplicationException("Не найдена искомая книга"));
            booleanBuilder.and(qProduct.books.contains(book));
        }
        return booleanBuilder.getValue();
    }
}
