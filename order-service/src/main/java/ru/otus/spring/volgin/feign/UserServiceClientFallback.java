package ru.otus.spring.volgin.feign;

import org.springframework.stereotype.Component;
import ru.otus.spring.volgin.dto.user.UserInfoDto;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserInfoDto getUserInfo(String id) {
        return UserInfoDto.builder()
                .email("N/A")
                .firstName("N/A")
                .lastName("N/A")
                .username("N/A")
                .id(id)
                .build();
    }
}
