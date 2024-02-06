package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.user.UserService;
import com.mito.sectask.values.USER_ROLE;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthoritySeeder implements Seeder {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final PageService pageService;
    private final UserService userService;

    @Override
    @Transactional
    public void seed() throws Exception {
        ProjectConfiguration configuration1 = new ProjectConfiguration()
            .setPageId(1L) // Testy
            .setOwnerEmail("william@email.com")
            .addMember("aisyah@email.com")
            .addMember("andre@email.com");

        ProjectConfiguration configuration2 = new ProjectConfiguration()
            .setPageId(2L) // Binusmaya Website
            .setOwnerEmail("william@email.com")
            .addMember("calvin@email.com")
            .addMember("stefan@email.com");

        ProjectConfiguration configuration3 = new ProjectConfiguration()
            .setPageId(3L) // Binus Mobile
            .setOwnerEmail("william@email.com")
            .addMember("filipus@email.com")
            .addMember("owen@email.com");

        List<Authority> authorities = Stream
            .of(
                construcAuthorities(configuration1),
                construcAuthorities(configuration2),
                construcAuthorities(configuration3)
            )
            .flatMap(Collection::stream)
            .toList();

        authorityRepository.saveAllAndFlush(authorities);
    }

    private List<Authority> construcAuthorities(
        ProjectConfiguration configuration
    ) {
        List<Authority> authorities = new ArrayList<>();
        final Role OWNER_ROLE = roleRepository.findByName(
            USER_ROLE.FULL_ACCESS
        );
        final Role MEMBER_ROLE = roleRepository.findByName(
            USER_ROLE.COLLABORATORS
        );

        // Getting the page
        Long pageId = configuration.getPageId();
        Page page = pageService
            .findById(pageId)
            .orElseThrow(() -> new Error("Page [" + pageId + "] not found"));

        // Adding owner authoritiy
        String ownerEmail = configuration.getOwnerEmail();
        User owner = userService
            .findByEmail(ownerEmail)
            .orElseThrow(() ->
                new Error("User with email [" + ownerEmail + "] not found")
            );

        authorities.add(
            new Authority()
                .setPage(page)
                .setRole(OWNER_ROLE)
                .setUser(owner)
                .setIsPending(false)
        );

        // Adding members authorities
        List<String> memberEmails = List.copyOf(configuration.getMemberEmail());
        List<User> members = userService.findAllByEmails(memberEmails);
        for (User member : members) {
            authorities.add(
                new Authority()
                    .setPage(page)
                    .setRole(MEMBER_ROLE)
                    .setUser(member)
                    .setIsPending(false)
            );
        }

        return authorities;
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private class ProjectConfiguration {

        private Long pageId;
        private String ownerEmail;
        private Set<String> memberEmail = new HashSet<>();

        public ProjectConfiguration addMember(String email) {
            this.memberEmail.add(email);
            return this;
        }
    }
}
