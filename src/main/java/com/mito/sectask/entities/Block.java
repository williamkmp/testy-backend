package com.mito.sectask.entities;

import java.util.List;
import com.mito.sectask.values.BLOCK_TYPE;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "blocks")
public class Block {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "block_type", nullable = false)
    private BLOCK_TYPE blockType;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "icon_key", nullable = true)
    private String iconKey;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = true)
    private File file;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", referencedColumnName = "id", nullable = false)
    private List<Page> pages;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "prev_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Block prev;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "next_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Block next;

    @Column(name = "width", nullable = true)
    private Integer width;
}
