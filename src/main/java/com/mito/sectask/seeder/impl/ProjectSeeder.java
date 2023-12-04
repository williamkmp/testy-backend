package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.Project;
import com.mito.sectask.repositories.ProjectRepository;
import com.mito.sectask.seeder.Seeder;
import java.util.ArrayList;
import java.util.Calendar;
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
        List<Project> projects = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date endDate = calendar.getTime();

        projects.add(
            new Project()
                .setName("Bina Nusantara")
                .setDescription(
                    "Penetration testing for Bina Nusantara internal system code PC-1080"
                )
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setCreatedAt(new Date())
        );

        projects.add(
            new Project()
                .setName("Adicipta Inovasi")
                .setDescription(
                    "Mobilw application & cloud service penetration testing for P.T. Adicipta Inovasi PK-9087"
                )
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setCreatedAt(new Date())
        );

        projects.add(
            new Project()
                .setName("Dunia Indah")
                .setDescription(
                    "Internal Web application bug hunt and penetration testing for C.V. Dunia Indah"
                )
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setCreatedAt(new Date())
        );

        projectRepository.saveAllAndFlush(projects);
    }
}
