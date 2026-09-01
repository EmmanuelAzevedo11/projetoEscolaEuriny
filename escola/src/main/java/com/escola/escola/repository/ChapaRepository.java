package com.escola.escola.repository;

import com.escola.escola.models.Chapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapaRepository extends JpaRepository<Chapa, Long> {
}
