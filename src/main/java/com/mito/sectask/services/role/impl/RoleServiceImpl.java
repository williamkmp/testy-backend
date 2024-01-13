package com.mito.sectask.services.role.impl;

import com.mito.sectask.entities.Role;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.values.USER_ROLE;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Optional<USER_ROLE> getUserPageAuthority(Long userId, Long pageId) {
        // TODO: implement me
        throw new UnsupportedOperationException("unimplemented method getUserPageAuthority()");
    }
}
