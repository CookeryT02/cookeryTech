package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByResetPasswordCode(String code);


    //F08
    @Query("SELECT u FROM User u" + " WHERE (:q IS NULL OR " + "u.firstName LIKE %:q% OR "
            + "u.lastName LIKE %:q% OR " + "u.email LIKE %:q% OR " + "u.phone LIKE %:q%) ")
    Page<User> getUsers(@Param("q") String q, Pageable pageable);



    //F07
    void deleteByEmail(String email);


//    List<User> findByRoles(String customer);


    List<User> findByRoles(Role role);
}
