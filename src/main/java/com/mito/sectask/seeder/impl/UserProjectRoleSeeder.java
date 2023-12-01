package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.entities.QProjectEntity;
import com.mito.sectask.entities.QRoleEntity;
import com.mito.sectask.entities.QUserEntity;
import com.mito.sectask.entities.RoleEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.entities.UserProjectRoleEntity;
import com.mito.sectask.repositories.ProjectRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserProjectRoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.values.USER_ROLE;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProjectRoleSeeder implements Seeder {

    private final JPAQueryFactory query;
    private final UserProjectRoleRepository authorityRepository;
    private QRoleEntity roleTable = QRoleEntity.roleEntity;
    private QUserEntity userTable = QUserEntity.userEntity;
    private QProjectEntity projectTable = QProjectEntity.projectEntity;

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
        final RoleEntity fullAccessRole = query
            .selectFrom(roleTable)
            .where(roleTable.name.eq(USER_ROLE.FULL_ACCESS))
            .fetchFirst();

        final RoleEntity collaboratorRole = query
            .selectFrom(roleTable)
            .where(roleTable.name.eq(USER_ROLE.COLLABORATORS))
            .fetchFirst();

        final ProjectEntity project = query
            .selectFrom(projectTable)
            .where(
                projectTable.name.equalsIgnoreCase(
                    projectConfiguration.getProjectName()
                )
            )
            .fetchFirst();

        UserEntity owner = query
            .selectFrom(userTable)
            .where(userTable.email.eq(projectConfiguration.getOwnerEmail()))
            .fetchOne();

        List<UserEntity> members = query
            .selectFrom(userTable)
            .where(userTable.email.in(projectConfiguration.getMemberEmails()))
            .fetch();

        List<UserProjectRoleEntity> authorities = members
            .stream()
            .map(member ->
                new UserProjectRoleEntity()
                    .setProject(project)
                    .setUser(member)
                    .setRole(collaboratorRole)
                    .setIsPending(false)
            )
            .toList();

        authorities.add(
            new UserProjectRoleEntity()
                .setProject(project)
                .setUser(owner)
                .setRole(fullAccessRole)
                .setIsPending(false)
        );

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
