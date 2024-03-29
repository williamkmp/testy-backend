package com.mito.sectask.services.page;

import com.mito.sectask.dto.dto.InviteDto;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Block;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.repositories.BlockRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.repositories.RoleRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.USER_ROLE;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    @Override
    public List<Page> getRootPages(Long userId) {
        return pageRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Optional<Page> findById(Long pageId) {
        if (pageId == null) return Optional.empty();
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
    public Optional<Page> delete(Long pageId) {
        //TODO: fix delete method, failed deleting root page
        try {
            Page page = pageRepository
                .findById(pageId)
                .orElseThrow(Exception::new);
            if (page.getImage() != null) {
                page.setImage(null);
            }
            page.getBlocks().clear();
            page.getAuthorities().clear();
            pageRepository.delete(page);
            return Optional.of(page);
        } catch (Exception e) {
            log.error("Error delteing page id:{}", pageId);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<Page> createRootPage(
        Page page,
        Long userId,
        List<InviteDto> inviteList
    ) {
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
            User member = userRepository
                .findByEmail(invite.getEmail())
                .orElse(null);
            if (member == null) continue;
            authorities.add(
                new Authority()
                    .setUser(member)
                    .setPage(createdPage)
                    .setRole(memberRole)
                    .setIsPending(false)
            );
        }
        authorityRepository.saveAll(authorities);

        return Optional.of(createdPage);
    }

    @Override
    @Transactional
    public Optional<Page> createSubPage(Page page, String collectionId) {
        try {
            Block collection = blockRepository
                .findById(collectionId)
                .orElseThrow(ResourceNotFoundException::new);
            page = pageRepository.saveAndFlush(page);
            page.setCollection(collection);
            return Optional.of(pageRepository.saveAndFlush(page));
        } catch (Exception e) {
            log.error(
                "Error creating sub page id:{} collectionId:{}",
                page.getId(),
                collectionId
            );
            e.printStackTrace();
            return Optional.empty();
        }
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
        if (maybePage.isEmpty()) return Optional.empty();
        Page page = maybePage.get();
        while (page.getCollection() != null) {
            Block collection = page.getCollection();
            page = collection.getPage();
        }

        return Optional.of(page);
    }

    @Override
    public List<Page> findByCollectionId(String collectionId) {
        return pageRepository.findAllByCollectionIdOrderByCreatedAtDesc(
            collectionId
        );
    }

    @Override
    public List<Page> searchPageByName(String searchText, Long userId)
        throws ResourceNotFoundException {
        List<Page> rootPages = getRootPages(userId);
        Set<Long> authorizedRootPageId = rootPages
            .stream()
            .map(Page::getId)
            .collect(Collectors.toSet());
        List<Page> searchResults = pageRepository.searchByTitle(
            "%" + searchText + "%"
        );

        List<Page> filteredResult = new ArrayList<>();
        for (Page page : searchResults) {
            Page rootPage = getRootOfPage(page.getId())
                .orElseThrow(ResourceNotFoundException::new);
            Boolean isAuthorized = authorizedRootPageId.contains(
                rootPage.getId()
            );
            if (Boolean.TRUE.equals(isAuthorized)) {
                filteredResult.add(page);
            }
        }

        return filteredResult;
    }
}
