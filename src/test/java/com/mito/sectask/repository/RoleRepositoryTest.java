package com.mito.sectask.repository;

import com.mito.sectask.entities.Role;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.values.USER_ROLE;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Test findByName()")
    void testFindByName() {
        final Role fullAccess = roleRepository.findByName(
            USER_ROLE.FULL_ACCESS
        );
        final Role collaborator = roleRepository.findByName(
            USER_ROLE.COLLABORATORS
        );
        final Role viewer = roleRepository.findByName(USER_ROLE.VIEWERS);

        Assertions.assertThat(fullAccess).isNotNull();
        Assertions.assertThat(collaborator).isNotNull();
        Assertions.assertThat(viewer).isNotNull();

        Assertions
            .assertThat(fullAccess.getName())
            .isEqualTo(USER_ROLE.FULL_ACCESS);
        Assertions
            .assertThat(collaborator.getName())
            .isEqualTo(USER_ROLE.COLLABORATORS);
        Assertions.assertThat(viewer.getName()).isEqualTo(USER_ROLE.VIEWERS);
    }

    @Test
    @DisplayName("Test findByRootPageId")
    void testFindByRootPageId() {
        Long testyPageId = 1l; // Page: Testy
        Long bimayPageId = 2l; // Page: Testy
        Long williamId = 1L; // User: william.kmp
        Long aisyahId = 3L; // User: aisyah

        Role williamTestyRole = roleRepository
            .findByRootPageId(testyPageId, williamId)
            .orElse(null);
        Assertions
            .assertThat(williamTestyRole)
            .as("User role is present")
            .isNotNull();
        Assertions
            .assertThat(williamTestyRole.getName())
            .as("User role is correct")
            .isEqualTo(USER_ROLE.COLLABORATORS);

        Role williamBimayRole = roleRepository
            .findByRootPageId(bimayPageId, williamId)
            .orElse(null);
        Assertions
            .assertThat(williamBimayRole)
            .as("User role is present")
            .isNotNull();
        Assertions
            .assertThat(williamBimayRole.getName())
            .as("User role is correct")
            .isEqualTo(USER_ROLE.FULL_ACCESS);

        Role aisyahTestyRole = roleRepository
            .findByRootPageId(testyPageId, aisyahId)
            .orElse(null);
        Assertions
            .assertThat(aisyahTestyRole)
            .as("User role is present")
            .isNotNull();
        Assertions
            .assertThat(aisyahTestyRole.getName())
            .as("User role is correct")
            .isEqualTo(USER_ROLE.FULL_ACCESS);

        Role aisyahBimayRole = roleRepository
            .findByRootPageId(bimayPageId, aisyahId)
            .orElse(null);
        Assertions
            .assertThat(aisyahBimayRole)
            .as("User role is not present")
            .isNull();
    }
}
