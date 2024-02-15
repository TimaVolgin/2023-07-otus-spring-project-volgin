package ru.otus.spring.volgin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.volgin.domain.Order;
import ru.otus.spring.volgin.dto.order.OrderShortDto;

import java.util.Optional;

/**
 * Репозиторий по работе с заказами
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Возвращает все заказы пользователя
     * @param userId   идентификатор пользователя
     * @param pageable пагинация
     * @return все заказы пользователя
     */
    @Query("SELECT new ru.otus.spring.volgin.dto.order.OrderShortDto(o.id, o.created, o.email, o.phone, " +
            "o.note, o.address, o.userId, o.username, SUM(oi.price * oi.count)) " +
            "FROM Order AS o join o.items AS oi where o.userId = :userId GROUP BY o.id")
    Page<OrderShortDto> findAllByUserId(String userId, Pageable pageable);

    /**
     * Возвращает заказ пользователя
     * @param userId  идентификатор пользователя
     * @param orderId идентификатор заказа
     * @return заказ пользователя
     */
    Optional<Order> findByUserIdAndId(String userId, long orderId);

    /**
     * Возвращает все заказы
     * @param pageable пагинация
     * @return все заказы
     */
    @Query("SELECT new ru.otus.spring.volgin.dto.order.OrderShortDto(o.id, o.created, o.email, o.phone, " +
            "o.note, o.address, o.userId, o.username, SUM(oi.price * oi.count)) " +
            "FROM Order AS o join o.items AS oi GROUP BY o.id")
    Page<OrderShortDto> findAllWithTotalSum(Pageable pageable);
}
