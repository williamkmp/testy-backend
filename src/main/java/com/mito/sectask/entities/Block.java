package com.mito.sectask.entities;

import com.mito.sectask.values.BLOCK_TYPE;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BLOCK_TYPE blockType;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content = "<p></p>";

    @Column(name = "icon_key", nullable = false)
    private String iconKey = "emoji-1215";

    @Column(name = "width", nullable = false)
    private Float width = 100f;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "page_id", referencedColumnName = "id", nullable = true)
    private Page page;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = true)
    private File file;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "collection"
    )
    private List<Page> pages;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_id", referencedColumnName = "id", nullable = true)
    private Block prev;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "next_id", referencedColumnName = "id", nullable = true)
    private Block next;
}
