package com.mito.sectask.services.page.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mito.sectask.dto.dto.InviteDto;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.USER_ROLE;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Override
    public List<Page> getUserPages(Long userId) {
        return pageRepository.getUserPages(userId);
    }

    @Override
    @Transactional
    public Optional<Page> findById(Long pageId) {
        if (pageId == null)
            return Optional.empty();
        Optional<Page> maybePage = pageRepository.findById(pageId);
        if (maybePage.isEmpty()) {
            return Optional.empty();
        }
        Page page = maybePage.get();
        page.getImage();
        return Optional.of(page);
    }

    @Override
    @Transactional
    public Optional<Page> createRootPage(Page page, Long userId, List<InviteDto> inviteList) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }
        User owner = maybeUser.get(); 
        Role fullAccessRole = roleRepository.findByName(USER_ROLE.FULL_ACCESS);
        Page createdPage = pageRepository.save(page);

        // Insert owner(full_access) authority
        Authority ownerAuthority = new Authority()
                .setUser(owner)
                .setPage(createdPage)
                .setRole(fullAccessRole)
                .setIsPending(false);
        authorityRepository.save(ownerAuthority);

        // Inserting members
        List<Authority> authorities = new ArrayList<>();
        for (InviteDto invite : inviteList) {
            Role memberRole = roleRepository.findByName(invite.getAuthority());
            User member = userRepository.findByEmail(invite.getEmail()).orElse(null);
            if (member == null)
                continue;
            authorities.add(new Authority()
                    .setUser(member)
                    .setPage(createdPage)
                    .setRole(memberRole)
                    .setIsPending(false));
        }
        authorityRepository.saveAll(authorities);

        return Optional.of(createdPage);
    }

    @Override
    public Optional<Page> createSubPage(Page page, String collectionId) {
        // TODO implement craete subPage
        throw new UnsupportedOperationException("Unimplemented method 'createSubPage'");
    }

    @Override
    @Transactional
    public Optional<Page> update(Page page) {
        Page newPageData = null;
        try {
            newPageData = pageRepository.save(page);
        } catch (Exception e) {
            Util.doNothing("createdPage is already null");
        }
        return Optional.ofNullable(newPageData);
    }

    @Override
    @Transactional
    public Optional<Page> getRootOfPage(Long pageId) {
        Optional<Page> maybePage = pageRepository.findById(pageId);
        
        if(maybePage.isEmpty()) 
            return Optional.empty();
        
        Page page = maybePage.get();
        while (page.getCollection() != null) {
            Block collection = page.getCollection();
            page = collection.getPage();
        }

        return Optional.of(page);
    }
}
