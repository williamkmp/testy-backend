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
     * get page data by id
     * 
     * @param   pageId {@link Long}
     *          page id
     * 
     * @return  {@link Optional}<{@link Page}>
     *          containing Page, else Optional.empty()
     *          if fails ofr not found
     */
    public Optional<Page> findById(Long pageId);

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
     * create a page that bleongs to a certain collection
     * 
     * @param   page {@link Page}
     *          new page data
     * 
     * @param   collectionId {@link String}
     *          Block uuid with type COLLECTION which the 
     *          created page belongs to
     *  
     * @return  {@link Optional}<{@link Page}>
     *          created page data  
     */
    public Optional<Page> createSubPage(Page page, String collectionId);

    /**
     * update an existing page data
     *  
     * @param   page {@link Page} 
     *          new page data, <code>id</code> must bot be null
     *  
     * @return  {@link Optional}<{@link Page}>
     *          updated page data 
     */
    public Optional<Page> update(Page page);
}
