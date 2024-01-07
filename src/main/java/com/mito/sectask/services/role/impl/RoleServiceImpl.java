package com.mito.sectask.services.role.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mito.sectask.entities.Role;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.values.USER_ROLE;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(USER_ROLE roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            return null;
        }
        return role.get();
    }

    @Override
    public Optional<USER_ROLE> getPageAuthorityOfUser(
        Long userId,
        Long pageId
    ) {
        //TODO: fix me for nested page interaction
        Optional<Role> role = roleRepository.getPageAuthority(pageId, userId);
        if(role.isEmpty())
            return Optional.empty();
        else
            return Optional.of(role.get().getName()); 
    }
}
