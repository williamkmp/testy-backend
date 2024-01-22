package com.mito.sectask.services.auth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.mito.sectask.dto.dto.JwtPayload;
import com.mito.sectask.dto.dto.TokenDto;
import com.mito.sectask.dto.parameters.LoginParameter;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.auth.AuthService;
import com.mito.sectask.services.encoder.PasswordEncocder;
import jakarta.transaction.Transactional;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${app.security.jwt.access_token.duration:300000}")
    Long ACCESS_TOKEN_DURATION_MS;

    @Value("${app.security.jwt.refresh_token.duration:86400000}")
    Long REFRESH_TOKEN_DURATION_MS;

    @Value("${app.security.jwt.access_token.secret:SECRET_123}")
    String ACCESS_TOKEN_SECRET;

    @Value("${app.security.jwt.refresh_token.secret:SECRET_456}")
    String REFRESH_TOKEN_SECRET;

    private final Gson gson;
    private final UserRepository userRepository;
    private final PasswordEncocder password;

    @Override
    public Optional<User> loginUser(LoginParameter userCredential) {
        Optional<User> maybeUser = userRepository.findByEmail(
            userCredential.getEmail()
        );
        if (maybeUser.isEmpty()) return Optional.empty();
        User registeredUser = maybeUser.get();
        Boolean isPasswordMatch = password.matches(
            userCredential.getPassword(),
            registeredUser.getPassword()
        );
        if (Boolean.FALSE.equals(isPasswordMatch)) return Optional.empty();
        return Optional.of(registeredUser);
    }

    @Override
    public Optional<TokenDto> generateTokens(Long userid) {
        Optional<String> maybeAccessToken = generateAccessToken(userid);
        Optional<String> maybeRefreshtoken = generateRefreshToken(userid);

        if (maybeAccessToken.isEmpty() || maybeRefreshtoken.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
            new TokenDto()
                .setAccessToken(maybeAccessToken.get())
                .setRefreshToken(maybeRefreshtoken.get())
        );
    }

    @Override
    public Optional<JwtPayload> verifyAccessToken(String accessToken) {
        return verifyToken(ACCESS_TOKEN_SECRET, accessToken);
    }

    @Override
    public Optional<JwtPayload> verifyRefreshToken(String refreshToken) {
        Optional<JwtPayload> maybePayload = verifyToken(
            REFRESH_TOKEN_SECRET,
            refreshToken
        );

        if (maybePayload.isEmpty()) return Optional.empty();
        JwtPayload payload = maybePayload.get();

        Optional<User> maybeUser = userRepository.findById(payload.getId());

        if (maybeUser.isEmpty()) return Optional.empty();
        User user = maybeUser.get();

        if (
            user.getRefreshToken() == null ||
            !user.getRefreshToken().equals(refreshToken)
        ) return Optional.empty();

        return Optional.of(payload);
    }

    /**
     * generate access token froma given user
     *
     * @param userId {@link Long} user id
     * @return {@link Optional}<{@link String}> containing access token, else Optional.empty() if
     *     generation failed
     */
    private Optional<String> generateAccessToken(Long userId) {
        Optional<User> maybeUser = userRepository.findById(userId);

        if (maybeUser.isEmpty()) return Optional.empty();

        User user = maybeUser.get();
        JwtPayload tokenPayload = JwtPayload
            .builder()
            .id(user.getId())
            .email(user.getEmail())
            .tagName(user.getTagName())
            .build();

        return signPayload(
            ACCESS_TOKEN_SECRET,
            ACCESS_TOKEN_DURATION_MS,
            tokenPayload
        );
    }

    /**
     * generate refresh token and update the user record inside the database.
     *
     * @param userId {@link Long} user id
     * @return {@link Optional}<{@link String}> containing refresh token, else Optional.empty() if
     *     generation failed
     */
    @Transactional
    private Optional<String> generateRefreshToken(Long userId) {
        Optional<User> maybeUser = userRepository.findById(userId);

        if (maybeUser.isEmpty()) return Optional.empty();

        User user = maybeUser.get();
        JwtPayload tokenPayload = JwtPayload
            .builder()
            .id(user.getId())
            .email(user.getEmail())
            .tagName(user.getTagName())
            .build();

        Optional<String> maybeToken = signPayload(
            REFRESH_TOKEN_SECRET,
            REFRESH_TOKEN_DURATION_MS,
            tokenPayload
        );

        if (maybeToken.isEmpty()) return Optional.empty();

        user.setRefreshToken(maybeToken.get());
        userRepository.save(user);

        return maybeToken;
    }

    /**
     * sign payload to create the json webtoken
     *
     * @param secret {@link String} secret string to verify and sign the json web token
     * @param duration {@link Long} token validity duration is miliseconds
     * @param payload {@link JwtPayload} the payload for the token
     * @return {@link Optional}<{@link String}> conatining the generated token signed using the
     *     secret, else Optional.empty() if generation process failed
     */
    public Optional<String> signPayload(
        String secret,
        Long duration,
        JwtPayload payload
    ) {
        Optional<String> token;
        try {
            String payloadJsonString = gson.toJson(payload);

            Long systemTime = System.currentTimeMillis();
            Date now = new Date(systemTime);
            Date expiredAt = new Date(systemTime + duration);

            String tokenString = JWT
                .create()
                .withPayload(payloadJsonString)
                .withIssuedAt(now)
                .withExpiresAt(expiredAt)
                .sign(Algorithm.HMAC256(secret));

            token = Optional.of(tokenString);
        } catch (Exception e) {
            token = Optional.empty();
        }
        return token;
    }

    /**
     * verify a JSON Webtoken using a scret string
     *
     * @param secret {@link String} secret string used to verify the given token
     * @param token {@link String} token validity duration is miliseconds
     * @return {@link Optional}<{@link JwtPayload}> containing the payload of the token , else
     *     Optional.empty() if failed
     */
    public Optional<JwtPayload> verifyToken(String secret, String token) {
        Optional<JwtPayload> maybePayload;
        try {
            JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .build();
            String encodedPayload = verifier.verify(token).getPayload();
            String payloadJsonString = new String(
                Base64.getDecoder().decode(encodedPayload)
            );
            JwtPayload payloadData = gson.fromJson(
                payloadJsonString,
                JwtPayload.class
            );
            maybePayload = Optional.of(payloadData);
        } catch (Exception e) {
            maybePayload = Optional.empty();
        }
        return maybePayload;
    }
}
