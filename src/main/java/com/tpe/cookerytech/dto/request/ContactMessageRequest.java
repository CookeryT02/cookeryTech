package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageRequest {

    private String name;

    private String company;

    @Column(nullable = false, unique = true)
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    @Column(length = 14,nullable = false)
    private String phone;

    private String message;


}
