package com.tpe.cookerytech.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import com.tpe.cookerytech.domain.enums.RoleType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "t_role")
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    @Override
    public String toString() {
        return "Role{" +
                "type=" + type +
                '}';
    }

    //    @ManyToMany(mappedBy = "roles")
//    private Set<User> users = new HashSet<>();

//    @Override
//    public String toString() {
//        return "Role{" +
//                "name='" + name + '\'' +
//                '}';
//    }


}
