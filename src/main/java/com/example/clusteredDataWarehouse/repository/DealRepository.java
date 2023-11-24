package com.example.clusteredDataWarehouse.repository;

import com.example.clusteredDataWarehouse.entities.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    boolean existsByUniqueId(String uniqueId);
}
