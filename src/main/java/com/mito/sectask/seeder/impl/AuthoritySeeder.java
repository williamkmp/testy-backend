package com.mito.sectask.seeder.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.QPage;
import com.mito.sectask.entities.QUser;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.values.USER_ROLE;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Component
@RequiredArgsConstructor
public class AuthoritySeeder implements Seeder {

    private final AuthorityRepository authorityRepository;
    private final RoleService roleService;
    private final JPAQueryFactory database;

    @Override
    @Transactional
    public void seed() throws Exception {
        ProjectConfiguration configuration1 = new ProjectConfiguration()
            .setProjectId(1L)
            .setOwnerEmail("aisyah@email.com")
            .addMember("william@email.com")
            .addMember("andre@email.com");

        ProjectConfiguration configuration2 = new ProjectConfiguration()
            .setProjectId(2L)
            .setOwnerEmail("william@email.com")
            .addMember("calvin@email.com")
            .addMember("stefan@email.com");

        ProjectConfiguration configuration3 = new ProjectConfiguration()
            .setProjectId(3L)
            .setOwnerEmail("william@email.com")
            .addMember("filipus@email.com")
            .addMember("owen@email.com");

        List<Authority> authorities1 = getAuthorities(configuration1);
        List<Authority> authorities2 = getAuthorities(configuration2);
        List<Authority> authorities3 = getAuthorities(configuration3);
        authorityRepository.saveAll(authorities1);
        authorityRepository.saveAll(authorities2);
        authorityRepository.saveAll(authorities3);
    }

    private List<Authority> getAuthorities(ProjectConfiguration configuration) {
        final QUser userTable = QUser.user;
        final QPage pageTable = QPage.page;
        final Role FULL_ACCESS = roleService.getRole(USER_ROLE.FULL_ACCESS);
        final Role COLLABORATORS = roleService.getRole(USER_ROLE.COLLABORATORS);

        List<Authority> authorities = new ArrayList<>();

        // Get owner
        User owner = Optional
            .ofNullable(
                database
                    .selectFrom(userTable)
                    .where(
                        userTable.email.equalsIgnoreCase(
                            configuration.getOwnerEmail()
                        )
                    )
                    .fetchOne()
            )
            .orElseThrow();

        // Get Page
        Page page = Optional
            .ofNullable(
                database
                    .selectFrom(pageTable)
                    .where(pageTable.id.eq(configuration.getProjectId()))
                    .fetchOne()
            )
            .orElseThrow();

        List<User> members = database
            .selectFrom(userTable)
            .where(userTable.email.in(configuration.getMemberEmail()))
            .fetch();

        authorities.add(
            new Authority()
                .setUser(owner)
                .setPage(page)
                .setRole(FULL_ACCESS)
                .setIsPending(false)
        );

        for (User member : members) {
            authorities.add(
                new Authority()
                    .setUser(member)
                    .setPage(page)
                    .setRole(COLLABORATORS)
                    .setIsPending(false)
            );
        }

        return authorities;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private class ProjectConfiguration {

        private Long projectId;
        private String ownerEmail;
        private Set<String> memberEmail = new HashSet<>();

        public ProjectConfiguration addMember(String email) {
            this.memberEmail.add(email);
            return this;
        }
    }
}
