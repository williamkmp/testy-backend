package com.mito.sectask.seeder;

import com.mito.sectask.seeder.impl.AuthoritySeeder;
import com.mito.sectask.seeder.impl.BlockSeeder;
import com.mito.sectask.seeder.impl.CollectionSeeder;
import com.mito.sectask.seeder.impl.ImageSeeder;
import com.mito.sectask.seeder.impl.PageSeeder;
import com.mito.sectask.seeder.impl.RoleSeeder;
import com.mito.sectask.seeder.impl.UserSeeder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeedRunner implements CommandLineRunner {

    @Value("${app.database.seed:true}")
    private Boolean IS_RUN_SEEDER = true;

    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;
    private final ImageSeeder imageSeeder;
    private final PageSeeder pageSeeder;
    private final BlockSeeder blockSeeder;
    private final CollectionSeeder collectionSeeder;
    private final AuthoritySeeder authoritySeeder;

    @Override
    public void run(String... args) throws Exception {
        if (Boolean.FALSE.equals(IS_RUN_SEEDER)) return;
        try {
            final List<Seeder> seeders = new ArrayList<>();
            log.info("Start seeding");

            // Registering seeder
            seeders.add(imageSeeder);
            seeders.add(roleSeeder);
            seeders.add(userSeeder);
            seeders.add(pageSeeder);
            seeders.add(authoritySeeder);
            seeders.add(blockSeeder);
            seeders.add(collectionSeeder);

            // Running seeder
            for (Seeder seeder : seeders) {
                Instant start = Instant.now();
                String seederName = seeder.getClass().getSimpleName().split("\\$\\$")[0];
                seeder.seed();
                Instant finish = Instant.now();
                long elapsedTime = Duration.between(start, finish).toMillis();

                log.atInfo()
                        .setMessage("Execute [{}]: {}ms")
                        .addArgument(seederName)
                        .addArgument(elapsedTime)
                        .log();
            }

            log.info("Done seeding");
        } catch (Exception e) {
            log.error("Error when seeding", e);
        }
    }
}
