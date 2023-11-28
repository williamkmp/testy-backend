package com.mito.sectask.seeder.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.entities.ProjectEntityField;
import com.mito.sectask.entities.RoleEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.entities.UserEntityField;
import com.mito.sectask.entities.UserProjectRoleEntity;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserProjectRoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.values.USER_ROLE;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Component
@RequiredArgsConstructor
public class UserProjectRoleSeeder implements Seeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JPAStreamer database;
    private final UserProjectRoleRepository authorityRepository;

    @Override
    @Transactional
    public void seed() throws Exception {
        ProjectConfiguration project1 = new ProjectConfiguration();
        ProjectConfiguration project2 = new ProjectConfiguration();
        ProjectConfiguration project3 = new ProjectConfiguration();

        project1
            .setProjectName("Bina Nusantara")
            .setOwnerEmail("aisyah@email.com")
            .addMember("william@email.com")
            .addMember("andre@email.com");

        project2
            .setProjectName("Adicipta Inovasi")
            .setOwnerEmail("william@email.com")
            .addMember("hendry@email.com")
            .addMember("josephine@email.com")
            .addMember("owen@email.com")
            .addMember("blisstine@email.com")
            .addMember("filipus@email.com");

        project3
            .setProjectName("Dunia Indah")
            .setOwnerEmail("william@email.com")
            .addMember("calvin@email.com")
            .addMember("stefan@email.com");

        List<UserProjectRoleEntity> proj1Authorities = getAuthorities(project1);
        List<UserProjectRoleEntity> proj2Authorities = getAuthorities(project2);
        List<UserProjectRoleEntity> proj3Authorities = getAuthorities(project3);

        authorityRepository.saveAll(proj1Authorities);
        authorityRepository.saveAll(proj2Authorities);
        authorityRepository.saveAll(proj3Authorities);
    }

    private List<UserProjectRoleEntity> getAuthorities(
        ProjectConfiguration projectConfiguration
    ) throws Exception {
        ProjectEntity project = database
            .stream(ProjectEntity.class)
            .filter(
                ProjectEntityField.name.equal(
                    projectConfiguration.getProjectName()
                )
            )
            .findFirst()
            .orElseThrow(() ->
                new Exception(
                    String.format(
                        "Project with name: %s not found",
                        projectConfiguration.getProjectName()
                    )
                )
            );

        UserEntity owner = userRepository
            .findByEmail(projectConfiguration.getOwnerEmail())
            .orElseThrow(() ->
                new Exception(
                    String.format(
                        "User as owner with email: '%s' not found",
                        projectConfiguration.getOwnerEmail()
                    )
                )
            );

        List<UserEntity> members = database
            .stream(UserEntity.class)
            .filter(
                UserEntityField.email.in(projectConfiguration.getMemberEmails())
            )
            .collect(Collectors.toList());

        RoleEntity fullAccessRole = roleRepository
            .findByName(USER_ROLE.FULL_ACCESS)
            .orElseThrow(() ->
                new Exception(
                    String.format(
                        "USER_ROLE: [%s] not found",
                        USER_ROLE.FULL_ACCESS
                    )
                )
            );

        RoleEntity collaboratorRole = roleRepository
            .findByName(USER_ROLE.COLLABORATORS)
            .orElseThrow(() ->
                new Exception(
                    String.format(
                        "USER_ROLE: [%s] not found",
                        USER_ROLE.COLLABORATORS
                    )
                )
            );

        List<UserProjectRoleEntity> authorities = new ArrayList<>();

        authorities.add(
            new UserProjectRoleEntity()
                .setProject(project)
                .setUser(owner)
                .setRole(fullAccessRole)
        );

        for (UserEntity member : members) {
            authorities.add(
                new UserProjectRoleEntity()
                    .setProject(project)
                    .setUser(member)
                    .setRole(collaboratorRole)
            );
        }

        return authorities;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    private class ProjectConfiguration {

        private String projectName;
        private String ownerEmail;
        private Set<String> memberEmails = new HashSet<>();

        public ProjectConfiguration addMember(String memberEmail) {
            this.memberEmails.add(memberEmail);
            return this;
        }
    }
}