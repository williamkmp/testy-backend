package com.mito.sectask.entities;

import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "pages")
public class Page {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "icon_key", nullable = true)
    private String iconKey;

    @Column(name = "image_position", nullable = false)
    private Float imagePosition = (float) 0;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id", nullable = true)
    private File image;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
        name = "page_id",
        referencedColumnName = "id",
        nullable = true
    )
    private List<Block> blocks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "page")
    private List<Authority> authorities;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
