package com.mito.sectask.services.role;

import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.exceptions.UserNotFoundException;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.values.USER_ROLE;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PageService pageService;

    @Override
    @Transactional
    public Optional<Role> getUserPageAuthority(Long userId, Long pageId)
        throws UserNotFoundException, ResourceNotFoundException {
        User user = userRepository
            .findById(userId)
            .orElseThrow(UserNotFoundException::new);
        Page rootPage = pageService
            .getRootOfPage(pageId)
            .orElseThrow(ResourceNotFoundException::new);
        return roleRepository.findByRootPageId(rootPage.getId(), user.getId());
    }

    @Override
    public Role find(USER_ROLE role) {
        return roleRepository.findByName(role);
    }

    @Override
    public Optional<Authority> save(Authority authority) {
        try {
            Authority savedAuthority = authorityRepository.save(authority);
            return Optional.of(savedAuthority);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Authority> findAuthority(Long userId, Long pageId) {
        try {
            Page page = pageService
                .getRootOfPage(pageId)
                .orElseThrow(ResourceNotFoundException::new);
            User user = userRepository
                .findById(userId)
                .orElseThrow(ResourceNotFoundException::new);
            Authority authority = authorityRepository
                .findByUserAndPage(user.getId(), page.getId())
                .orElse(null);
            return Optional.of(authority);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Authority> updateAuthority(
        Long userId,
        Long pageId,
        USER_ROLE role
    ) {
        try {
            Role userRole = find(role);
            Authority authority = findAuthority(userId, pageId)
                .orElseThrow(ResourceNotFoundException::new);
            authority.setRole(userRole);
            Authority updatedAuthority = authorityRepository.save(authority);
            return Optional.of(updatedAuthority);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void deleteAuthority(Long userId, Long pageId) {
        try {
            Authority authority = findAuthority(userId, pageId)
                .orElseThrow(ResourceNotFoundException::new);
            authorityRepository.delete(authority);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
