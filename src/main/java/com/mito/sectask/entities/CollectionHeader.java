package com.mito.sectask.entities;

import com.mito.sectask.values.PROPERTIES_TYPE;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "collection_headers")
public class CollectionHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "properties_type", nullable = false)
    private PROPERTIES_TYPE propertiesType;

    @Column(name = "options", nullable = true)
    private List<String> options;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
        name = "block_id",
        referencedColumnName = "uuid",
        nullable = false
    )
    private Block block;
}
