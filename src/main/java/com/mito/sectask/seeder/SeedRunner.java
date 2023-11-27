package com.mito.sectask.seeder;

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

    private final UserSeeder userSeeder;
    private Logger log = LoggerFactory.getLogger(SeedRunner.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("RUN SEEDER");
        final List<Seeder> seeders = new ArrayList<>();
        seeders.add(userSeeder);

        for (Seeder seeder : seeders) {
            String seederName = seeder.getClass().getName();
            log.info(String.format("    [%s]: Started", seederName));
            seeder.seed();
            log.info(String.format("    [%s]: Completed", seederName));
        }
    }
}
