package com.mito.sectask.services.auth;

import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.dto.dto.TokenDto;
import com.mito.sectask.dto.parameters.LoginParameter;
import com.mito.sectask.dto.response.auth.AuthLoginResponse;
import com.mito.sectask.entities.User;
import java.util.Optional;

/**
 * Service class for user authentication and authorization
 *
 * @author william.kmp
 */
public interface AuthService {
    /**
     * authenticate user based on credential
     *
     * @param   userCredential {@link AuthLoginResponse}:
     *          credential needed for authentication
     *
     * @return  {@link Optional}<{@link User}>
     *          containing the lgged in user data 
     */
    public Optional<User> loginUser(LoginParameter userCredential);

    /**
     * generate new refresh & access token for the
     * given user id
     * 
     * @param   userid {@link Long}
     *          user id
     * 
     * @return  {@link Optional}<{@link TokenDto}>
     *          containing the user tokens, else
     *          Optional.empty() id fails
     */
    public Optional<TokenDto> generateTokens(Long userid);

    /**
     * verify the given jwt access token
     *
     * @param   accessToken {@link String}:
     *          jwt access token to be verified
     *
     * @return  {@link Optional}<{@link JwtPayload}>
     *          containing caller information if success
     *          else, Optional.empty() if failed
     */
    public Optional<JwtPayload> verifyAccessToken(String accessToken);

    /**
     * verify the given jwt refresh token
     *
     * @param   refreshToken {@link String}:
     *          jwt refresh token to be verified
     *
     * @return  {@link Optional}<{@link JwtPayload}>
     *          containing caller information if success
     *          else, Optional.empty() if failed
     */
    public Optional<JwtPayload> verifyRefreshToken(String refreshToken);
}
