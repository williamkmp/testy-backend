package com.mito.sectask.services.user;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.UserEntity;
import java.util.Optional;

public interface UserService {
    /**
     * register new user account to the application
     * @param newUserData new account data
     * @return created user data
     */
    public Optional<UserEntity> registerUser(RegisterUserParameter newUserData);

    /**
     * find user information by it's id
     * @param userId user id
     * @return user data, otherwise <code>null</code> if not found.
     */
    public Optional<UserEntity> findById(Long userId);

    /**
     * check if no other user has this email
     * @param email email email to be checked
     * @return true if exist another user with same email, else false
     */
    public Boolean checkEmailIsTaken(String email);

    /**
     * check if no other user has this email, excluding current user email
     * @param email email to be checked
     * @param userId excluding this user
     * @return true if exist another user with same email, else false
     */
    public Boolean checkEmailIsTakenExcept(String email, Long userId);

    /**
     * check if no other user has this email
     * @param username username to be checked
     * @return true if exist another user with same email, else false
     */
    public Boolean checkUsernameIsTaken(String username);

    /**
     * check if no other user has this username, excluding current user username
     * @param username username to be checked
     * @param userId excluding this user
     * @return true if exist another user with same username, else false
     */
    public Boolean checkUsernameIsTakenExcept(String username, Long userId);
}
