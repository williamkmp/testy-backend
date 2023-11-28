package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.RoleEntity;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.values.USER_ROLE;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Seeder class for table 'projects'
 */
@Component
@RequiredArgsConstructor
public class RoleSeeder implements Seeder {

    private final RoleRepository roleRepository;

    @Override
    public void seed() {
        List<RoleEntity> roles = new ArrayList<>();

        roles.add(
            new RoleEntity()
                .setName(USER_ROLE.FULL_ACCESS)
                .setDescription(
                    "Can view, edit, configure, and add collaborators to the project"
                )
        );

        roles.add(
            new RoleEntity()
                .setName(USER_ROLE.COLLABORATORS)
                .setDescription(
                    "Can access, view, and add/edit project items but cannot configure project"
                )
        );

        roles.add(
            new RoleEntity()
                .setName(USER_ROLE.VIEWERS)
                .setDescription(
                    "Can only view and comment, but cannot add or edit project"
                )
        );

        roleRepository.saveAllAndFlush(roles);
    }
}