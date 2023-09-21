package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByType(RoleType type);

}
