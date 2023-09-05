package com.tpe.cookerytech.repository;


import com.tpe.cookerytech.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

}
