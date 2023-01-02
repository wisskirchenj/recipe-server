package de.cofinpro.recipeserver.web.mapper;

import de.cofinpro.recipeserver.entities.User;
import de.cofinpro.recipeserver.web.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * mapper to map received UserDto on register to a User entity, hereby encoding the raw password.
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * map the Dto to the entity and encode the password hereby.
     */
    public User toEntity(UserDto dto) {
        return new User().setUsername(dto.email())
                .setPassword(passwordEncoder.encode(dto.password()));
    }
}
