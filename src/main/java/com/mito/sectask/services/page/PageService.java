package com.mito.sectask.services.page;

import java.util.List;
import java.util.Optional;
import com.mito.sectask.dto.dto.InviteDto;
import com.mito.sectask.entities.Page;

public interface PageService {

    /**
     * get list of root pages where the user have authority
     * over, the retrieved pages did not include their
     * children 
     * 
     * @param   userId {@link Long}
     *          user id
     * 
     * @return  {@link List}<{@link Page}>
     *          associated with the user
     *          (user have authority over these pages)
     */
    public List<Page> getUserPages(Long userId);

    /**
     * get the children of a given Page. 
     * the returned list of children doesn't contain the 
     * parent-children relationship
     * 
     * @param   pageId {@link Long}
     *          page id
     * 
     * @return  {@link List}<{@link Page}>
     *          list of page's childrens
     */
    public List<Page> getChildren(Long pageId);

    /**
     * get page data by id
     * 
     * @param   pageId {@link Long}
     *          page id
     * 
     * @return  {@link Optional}<{@link Page}>
     *          containing Page, else Optional.empty()
     *          if fails ofr not found
     */
    public Optional<Page> getPageById(Long pageId);

    /**
     * create a new root page and register it to the database. 
     * Returns the created root Page data.
     * 
     * @param   page {@link Page}
     *          the new page data.
     * 
     * @return  {@link Optional}<{@link Page}>
     *          created page data
     */
    public Optional<Page> createRootPage(Page page, Long userId, List<InviteDto> inviteList);

    /**
     * save a page data to the database, can be used 
     * to create a new page or update an existing one
     * 
     * @param   page {@link Page}
     *          page data, set <i>id</i> to <code>null</code> 
     *          to create new page
     *  
     * @return  {@link Optional}<{@link Page}>
     *          operated page page
     */
    public Optional<Page> save(Page page);
}
