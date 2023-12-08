package com.mito.sectask.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "file", nullable = false)
    private byte[] bytes;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}
