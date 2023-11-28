package com.mito.sectask.seeder.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.services.encoder.PasswordEncocder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Seeder class for table 'users'
 */
@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;
    private final PasswordEncocder password;

    @Override
    @Transactional
    public void seed() {
        final String DEFAULT_APSSWORD = password.encode("password");

        List<UserEntity> users = new ArrayList<>();

        users.add(
            new UserEntity()
                .setFullName("William Kurnia Mulyadi Putra")
                .setEmail("william@email.com")
                .setTagName("william.kmp")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Andre Wijaya")
                .setEmail("andre@email.com")
                .setTagName("andre.w")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Aisyah Poetri Rahmadhania")
                .setEmail("aisyah@email.com")
                .setTagName("aisyah.poetri")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Calvin Kurnia Mulyadi")
                .setEmail("calvin@email.com")
                .setTagName("calvin.kmp")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Stefan Kurnia")
                .setEmail("stefan@email.com")
                .setTagName("stefan.km")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Filipus Bramantyo Meridivitanto")
                .setEmail("filipus@email.com")
                .setTagName("filipus.bm")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Guido Owen Lwiantoro")
                .setEmail("owen@email.com")
                .setTagName("guido.owen")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Josephine Gunawan")
                .setEmail("josephine@email.com")
                .setTagName("ipin")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Hendry Gunawan")
                .setEmail("hendry@email.com")
                .setTagName("igun")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        users.add(
            new UserEntity()
                .setFullName("Blisstina Merada")
                .setEmail("blisstine@email.com")
                .setTagName("itin")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false)
        );

        userRepository.saveAllAndFlush(users);
    }
}
