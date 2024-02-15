package ru.otus.spring.volgin.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Заказ
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Время создания */
    @Column(name = "created")
    private LocalDateTime created;

    /** Email указанный при заказе */
    @Column(name = "email")
    private String email;

    /** Телефон указанный для связи */
    @Column(name = "phone", nullable = false)
    private String phone;

    /** Заметка */
    @Column(name = "note")
    private String note;

    /** Адрес */
    @Column(name = "address", nullable = false)
    private String address;

    /** Идентификатор пользователя из keycloak */
    @Column(name = "user_id")
    private String userId;

    /** Имя контактного лица */
    @Column(name = "username")
    private String username;

    /** Продукты в заказе */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
