package com.mito.sectask.services.page.impl;

import com.mito.sectask.dto.dto.InviteDto;
import com.mito.sectask.entities.Authority;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.QAuthority;
import com.mito.sectask.entities.QPage;
import com.mito.sectask.entities.QRole;
import com.mito.sectask.entities.QUser;
import com.mito.sectask.entities.Role;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.AuthorityRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.page.PageService;
import com.mito.sectask.services.role.RoleService;
import com.mito.sectask.utils.Util;
import com.mito.sectask.values.USER_ROLE;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final RoleService roleService;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final JPAQueryFactory query;

    @Override
    public List<Page> getUserPages(Long userId) {
        QPage pageTable = QPage.page;
        QUser userTable = QUser.user;
        QAuthority authorityTable = QAuthority.authority;
        QRole roleTable = QRole.role;

        return query
            .selectFrom(pageTable)
            .innerJoin(pageTable.authorities, authorityTable)
            .innerJoin(authorityTable.role, roleTable)
            .innerJoin(authorityTable.user, userTable)
            .where(authorityTable.isPending.eq(Boolean.FALSE))
            .where(userTable.isDeleted.eq(Boolean.FALSE))
            .where(userTable.id.eq(userId))
            .fetch();
    }

    @Override
    @Transactional
    public List<Page> getChildren(Long pageId) {
        Optional<Page> maybePage = pageRepository.findById(pageId);
        if (maybePage.isEmpty()) {
            return Collections.emptyList();
        }
        Page page = maybePage.get();
        page.getChildrens();
        return page.getChildrens();
    }

    @Override
    @Transactional
    public Optional<Page> getPageById(Long pageId) {
        if (pageId == null) return Optional.empty();
        Optional<Page> maybePage = pageRepository.findById(pageId);
        if (maybePage.isEmpty()) {
            return Optional.empty();
        }
        Page page = maybePage.get();
        page.getParent();
        page.getChildrens();
        page.getImage();
        return Optional.of(page);
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
        Role fullAccessRole = roleService.getRole(USER_ROLE.FULL_ACCESS);

        Page createdPage = pageRepository.save(page);
        Authority ownerAuthority = new Authority()
            .setUser(owner)
            .setPage(createdPage)
            .setRole(fullAccessRole)
            .setIsPending(false);

        authorityRepository.save(ownerAuthority);
        return Optional.of(createdPage);
    }

    @Override
    public Optional<Page> createSubPage(Page page) {
        Page createdPage = null;
        try {
            createdPage = pageRepository.save(page);
        } catch (Exception e) {
            Util.doNothing("createdPage is already null");
        }
        return Optional.ofNullable(createdPage);
    }
}
