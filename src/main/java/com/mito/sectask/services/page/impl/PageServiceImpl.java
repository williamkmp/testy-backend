package com.mito.sectask.services.page.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.QAuthority;
import com.mito.sectask.entities.QPage;
import com.mito.sectask.entities.QRole;
import com.mito.sectask.entities.QUser;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.services.page.PageService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService{
    
    private final PageRepository pageRepository;
    private final JPAQueryFactory query;

    @Override
    public List<Page> getUserPages(Long userId) {
        QPage pageTable = QPage.page;
        QUser userTable = QUser.user;
        QAuthority authorityTable = QAuthority.authority;
        QRole roleTable = QRole.role;

        return query.selectFrom(pageTable)
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
        Optional<Page> maybePage = pageRepository.findById(pageId);
        if(maybePage.isEmpty()) {
            return Optional.empty();
        }
        Page page = maybePage.get();
        page.getParent();
        page.getChildrens();
        page.getImage();
        return Optional.of(page);
    }
}
