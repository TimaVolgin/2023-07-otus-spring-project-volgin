package ru.otus.spring.volgin.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.volgin.domain.Product;

import java.util.List;

/**
 * Репозиторий для работы с продуктами
 */
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    /**
     * Возвращает все продукты
     * @return все продукты
     */
    @Override
    List<Product> findAll(Predicate predicate);

    /**
     * Возвращает все продукты, снятые с витрины
     * @return все продукты, снятые с витрины
     */
    Page<Product> findAllByDisabledTrue(Pageable pageable);

    /**
     * Снимает с витрины и возвращает на витрину продукты
     */
    @Modifying
    @Query("update Product p set p.disabled = :disabled where p.id in :ids")
    void modifyDisabled(@Param("ids") List<Long> ids, @Param("disabled") boolean disabled);

}
