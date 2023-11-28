package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.repositories.ProjectRepository;
import com.mito.sectask.seeder.Seeder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Seeder class for table 'projects'
 */
@Component
@RequiredArgsConstructor
public class ProjectSeeder implements Seeder {

    private final ProjectRepository projectRepository;

    @Override
    public void seed() {
        List<ProjectEntity> projects = new ArrayList<>();
        Date now = new Date();

        projects.add(
            new ProjectEntity()
                .setName("Bina Nusantara")
                .setDescription(null)
                .setCreatedAt(now)
        );

        projects.add(
            new ProjectEntity()
                .setName("Adicipta Inovasi")
                .setDescription(null)
                .setCreatedAt(now)
        );

        projects.add(
            new ProjectEntity()
                .setName("Dunia Indah")
                .setDescription(null)
                .setCreatedAt(now)
        );

        projectRepository.saveAllAndFlush(projects);
    }
}
