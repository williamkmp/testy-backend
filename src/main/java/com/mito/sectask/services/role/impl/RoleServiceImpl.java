package com.mito.sectask.services.role.impl;

import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PageService pageService;

    @Override
    @Transactional
    public Optional<Role> getUserPageAuthority(Long userId, Long pageId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        Optional<Page> maybePage = pageService.getRootOfPage(pageId);
        if (maybePage.isEmpty() || maybeUser.isEmpty()) {
            return Optional.empty();
        }

        User user = maybeUser.get();
        Page rootPage = maybePage.get();
        return roleRepository.findByRootPageId(rootPage.getId(), user.getId());
    }
}
