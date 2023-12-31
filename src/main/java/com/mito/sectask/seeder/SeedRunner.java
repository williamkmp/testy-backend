package com.mito.sectask.seeder;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mito.sectask.seeder.impl.ImageSeeder;
import com.mito.sectask.seeder.impl.RoleSeeder;
import com.mito.sectask.seeder.impl.UserSeeder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(SeedRunner.class);
    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final ImageSeeder imageSeeder;

    @Override
    public void run(String... args) throws Exception {
        try {
            final List<Seeder> seeders = new ArrayList<>();
            log.info("RUN SEEDER");

            // Registering seeder
            seeders.add(imageSeeder);
            seeders.add(roleSeeder);
            seeders.add(userSeeder);

            for (Seeder seeder : seeders) {
                seeder.seed();
            }
        } catch (Exception e) {
            log.error("Error when seeding", e);
        }
    }
}
