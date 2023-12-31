package com.tpe.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_image_file")
public class ImageFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")

    private String id;

    @Column(nullable = false)
    private String name;

    private String type;

    private long length;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="model_id",referencedColumnName = "id")
    private Model model;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ImageData imageData;

    public ImageFile(String name, String type, ImageData imageData) {
        this.name = name;
        this.type = type;
        this.imageData = imageData;
        this.length=imageData.getData().length;
    }
}