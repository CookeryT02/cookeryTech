package com.tpe.cookerytech.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false)
    @Size(min = 5, max = 150, message = "Title must be beetwen min 5 and max 150 characters")
    private String title;

    @Column(name = "short_desc", length = 300)
    private String shortDescription;

    @Column(name = "long_desc")
    private String longDescription;

    @Column(name = "slug", nullable = false, length = 200)
    @Size
    private String slug;

    @Column(name = "seq", nullable = false)
    private int sequence = 0;

    @Column(name = "is_new", nullable = false)
    private boolean isNew = false;

    @Column(name = "is_featured", nullable = false)
    private boolean isFeatured = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Set<ImageFile> image;

    @Column(name = "built_in", nullable = false)
    private boolean builtIn = false;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;


}
