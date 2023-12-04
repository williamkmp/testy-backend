package com.mito.sectask.seeder;

import com.mito.sectask.seeder.impl.ProjectSeeder;
import com.mito.sectask.seeder.impl.RoleSeeder;
import com.mito.sectask.seeder.impl.UserSeeder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(SeedRunner.class);
    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final ProjectSeeder projectSeeder;

    @Override
    public void run(String... args) throws Exception {
        try {
            final List<Seeder> seeders = new ArrayList<>();
            log.info("RUN SEEDER");

            // Registering seeder
            seeders.add(roleSeeder);
            seeders.add(userSeeder);
            seeders.add(projectSeeder);

            for (Seeder seeder : seeders) {
                String seederName = seeder.getClass().getSimpleName();
                log.info(String.format("    [%s]: Started", seederName));
                seeder.seed();
                log.info(String.format("    [%s]: Completed", seederName));
            }
        } catch (Exception e) {
            log.error("Error when seeding", e);
        }
    }
}
