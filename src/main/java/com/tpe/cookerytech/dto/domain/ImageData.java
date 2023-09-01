package com.tpe.cookerytech.dto.domain;

import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "t_image_data")
public class ImageData  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    /* @Column(name="data",columnDefinition = "BLOB")*/
    private byte[] data;

    public ImageData(byte[] data) {
        this.data = data;
    }

    public ImageData(Long id) {

        this.id = id;

    }

}
