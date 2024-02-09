package com.mito.sectask.services.user;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * register new user account to the database
     *
     * @param newUserData {@link RegisterUserParameter} new account data
     * @return {@link Optional}<{@link User}> containing created user data, else optional.empty() if
     *     failed to save
     */
    public Optional<User> registerUser(RegisterUserParameter newUserData);

    /**
     * update user data, by overwriting existing user based on a given user param
     *
     * @param userData {@link User} user data, must have id
     * @return {@link Optional}<{@link User}> containing the updated user data, else Optional.empty()
     *     if update fails
     */
    public Optional<User> updateUser(User userData);

    /**
     * find all member of a given page by id, if the given page is not a root
     * then it will traverse the page-collection relation to get the root and
     * return it's members
     *
     * @param pageId {@link Long} page id
     * @return  {@link List}<{@link User}> members of a given page
     */
    public List<User> findMembersOfPage(Long pageId);

    /**
     * find all member of a given collection by id, this method will traverse the page-collection relation to get the root and
     * return it's members
     *
     * @param collectionId {@link Long} block id
     * @return  {@link List}<{@link User}> members of a given collection
     */
    public List<User> findMembersOfCollection(String collectionId);

    /**
     * find user by id
     *
     * @param userId {@link Long} user id
     * @return {@link Optional}<{@link User}> containing user data, otherwise Optional.empty() if not
     *     found.
     */
    public Optional<User> findById(Long userId);

    /**
     * find user by email
     *
     * @param email {@link String} user email
     * @return {@link Optional}<{@link User}> containing user data, otherwise Optional.empty() if not
     *     found.
     */
    public Optional<User> findByEmail(String email);

    /**
     * find all user by email
     *
     * @param emails {@link List}<{@link User}> user email
     * @return {@link List}<{@link User}> list of all user with the email, not sorted
     */
    public List<User> findAllByEmails(List<String> emails);

    /**
     * check if no other user has this email
     *
     * @param email {@link String} email to be checked
     * @return {@link Boolean} true if no other user have the same email, else false
     */
    public Boolean checkEmailIsAvailable(String email);

    /**
     * check if no other user has this email, excluding a certain user
     *
     * @param email {@link String} email to be checked
     * @param userId {@link Long} excluding user with this id
     * @return {@link Boolean} true if no other user have the same email, else false
     */
    public Boolean checkEmailIsAvailable(String email, Long userId);

    /**
     * check if no other user has this tagname
     *
     * @param tagName {@link String} tagname to be checked
     * @return true if no other user have the same tagname, else false
     */
    public Boolean checkTagNameIsAvailable(String tagName);

    /**
     * check if no other user has this tagname, excluding a certain user
     *
     * @param tagName {@link String} tagname to be checked
     * @param userId {@link String} excluding this user
     * @return true if no other user have the same tagname, else false
     */
    public Boolean checkTagNameIsAvailable(String tagName, Long userId);

    /**
     * validate password against a registered user
     *
     * @param userId {@link Long} user's id tobe validated against
     * @param password {@link String} raw not encoded password string
     * @return boolean is password match and valid
     */
    public Boolean validatePassword(Long userId, String password);

    /**
     * search a list of user based of a given eamil query string
     * using fuzzy search
     *
     * @param emailQuery {@link String} email
     * @return {@link List}<{@link User}> result based off fuzzy comparison
     */
    public List<User> searchByEmail(String emailQuery);
}
