package com.mito.sectask.services.user;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.UserEntity;
import java.util.Optional;

public interface UserService {
    /**
     * register new user account to the database
     *
     * @param   newUserData {@link RegisterUserParameter}
     *          new account data
     *
     * @return  {@link Optional}<{@link UserEntity}>
     *          containing created user data, else 
     *          optional.empty() if failed to save
     */
    public Optional<UserEntity> registerUser(RegisterUserParameter newUserData);

    /**
     * find user by id
     *
     * @param   userId {@link Long}
     *          user id
     *
     * @return  {@link Optional}<{@link UserEntity}>
     *          containing searcher user data, 
     *          otherwise Optional.empty() if not found.
     */
    public Optional<UserEntity> findById(Long userId);

    /**
     * check if no other user has this email
     *
     * @param   email {@link String}
     *          email to be checked
     *
     * @return  {@link Boolean} true if no other user
     *          have the same email, else false
     */
    public Boolean checkEmailIsAvailable(String email);

    /**
     * check if no other user has this email,
     * excluding a certain user
     *
     * @param   email {@link String}
     *          email to be checked
     *
     * @param   userId {@link Long}
     *          excluding user with this id
     *
     * @return  {@link Boolean} true if no other user
     *          have the same email, else false
     */
    public Boolean checkEmailIsAvailable(String email, Long userId);

    /**
     * check if no other user has this tagname
     *
     * @param       tagName {@link String}
     *              tagname to be checked
     *
     * @return      true if no other user
     *              have the same tagname,
     *              else false
     */
    public Boolean checkTagNameIsAvailable(String tagName);

    /**
     * check if no other user has this tagname,
     * excluding a certain user
     *
     * @param   tagName {@link String}
     *          tagname to be checked
     *
     * @param   userId {@link String}
     *          excluding this user
     *
     * @return  true if no other user
     *          have the same tagname,
     *          else false
     */
    public Boolean checkTagNameIsAvailable(String tagName, Long userId);
}
