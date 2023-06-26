package de.cofinpro.recipeserver.service.impl;

import de.cofinpro.recipeserver.entities.User;
import de.cofinpro.recipeserver.repository.UserRepository;
import de.cofinpro.recipeserver.service.RegisterService;
import de.cofinpro.recipeserver.service.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService  {

    private final UserRepository repository;

    @Autowired
    public RegisterServiceImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    /**
     * method receives and saves the User entity with data mapped from the UserDto (name and encrypted password),
     * @param user the prepared User entity to save to the database.
     * @throws UserAlreadyExistsException if user already exists.
     */
    @Override
    public void registerUser(User user) throws UserAlreadyExistsException {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        repository.save(user);
    }
}
