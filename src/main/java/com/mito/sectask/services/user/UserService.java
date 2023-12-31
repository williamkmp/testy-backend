package com.mito.sectask.services.user;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.User;
import java.util.Optional;

public interface UserService {
    /**
     * register new user account to the database
     *
     * @param   newUserData {@link RegisterUserParameter}
     *          new account data
     *
     * @return  {@link Optional}<{@link User}>
     *          containing created user data, else
     *          optional.empty() if failed to save
     */
    public Optional<User> registerUser(RegisterUserParameter newUserData);

    /**
     * update user data, by overwriting existing user
     * based on a given user param
     *
     * @param   userData {@link User}
     *          user data, must have id
     *
     * @return  {@link Optional}<{@link User}>
     *          conatining the updated user data, else
     *          Optional.empty() if update fails
     */
    public Optional<User> updateUser(User userData);

    /**
     * find user by id
     *
     * @param   userId {@link Long}
     *          user id
     *
     * @return  {@link Optional}<{@link User}>
     *          containing searcher user data,
     *          otherwise Optional.empty() if not found.
     */
    public Optional<User> findById(Long userId);

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

    /**
     * validate password against a registered user
     *
     * @param   userId {@link Long}
     *          user's id tobe validated against
     *
     * @param   password {@link String}
     *          raw not encoded password string
     *
     * @return boolean is password match and valid
     */
    public Boolean validatePassword(Long userId, String password);
}
