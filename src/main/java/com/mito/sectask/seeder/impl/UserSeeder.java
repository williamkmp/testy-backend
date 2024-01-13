package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.services.encoder.PasswordEncocder;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Seeder class for table 'users' */
@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;
    private final PasswordEncocder password;

    @Override
    @Transactional
    public void seed() {
        final String DEFAULT_APSSWORD = password.encode("password");

        List<User> users = new ArrayList<>();

        users.add(new User()
                .setFullName("William Kurnia Mulyadi Putra")
                .setEmail("william@email.com")
                .setTagName("william.kmp")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        users.add(new User()
                .setFullName("Andre Wijaya")
                .setEmail("andre@email.com")
                .setTagName("andre.w")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        users.add(new User()
                .setFullName("Aisyah Poetri Rahmadhania")
                .setEmail("aisyah@email.com")
                .setTagName("aisyah.poetri")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        users.add(new User()
                .setFullName("Calvin Kurnia Mulyadi")
                .setEmail("calvin@email.com")
                .setTagName("calvin.kmp")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        users.add(new User()
                .setFullName("Stefan Kurnia")
                .setEmail("stefan@email.com")
                .setTagName("stefan.km")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        users.add(new User()
                .setFullName("Filipus Bramantyo Meridivitanto")
                .setEmail("filipus@email.com")
                .setTagName("filipus.bm")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        users.add(new User()
                .setFullName("Guido Owen Lwiantoro")
                .setEmail("owen@email.com")
                .setTagName("guido.owen")
                .setPassword(DEFAULT_APSSWORD)
                .setIsDeleted(false));

        userRepository.saveAllAndFlush(users);
    }
}
