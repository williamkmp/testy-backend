package com.mito.sectask.repositories;

import com.mito.sectask.entities.Block;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, String> {
    /**
     * get list of block from a given page
     *
     * @param pageId {@link Long} page id
     * @return {@link List}<{@link Block}> blockList of a given page
     */
    @Query(
        nativeQuery = true,
        value = """
        WITH RECURSIVE cte AS
        (
        	SELECT * FROM blocks AS b WHERE b.page_id = :pageId AND b.prev_id is null
        	UNION
        	SELECT blocks.* FROM cte JOIN blocks ON cte.id = blocks.prev_id WHERE blocks.page_id = :pageId
        )
        SELECT * FROM cte AS blocks
        """
    )
    public List<Block> findAllByPageId(@Param("pageId") Long pageId);
}
