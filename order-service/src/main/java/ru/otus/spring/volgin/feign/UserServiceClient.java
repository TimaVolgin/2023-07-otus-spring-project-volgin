package ru.otus.spring.volgin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.spring.volgin.dto.user.UserInfoDto;

/**
 * Клиент для работы с продуктовым сервисом
 */
@FeignClient(name = "user-service",
        fallback = UserServiceClientFallback.class,
        configuration = UserServiceClientConfiguration.class)
public interface UserServiceClient {

    /**
     * Возвращает пользователя по идентификатору
     * @param id идентификатор
     * @return пользователь
     */
    @GetMapping("/admin/users/{id}")
    UserInfoDto getUserInfo(@PathVariable String id);
}
