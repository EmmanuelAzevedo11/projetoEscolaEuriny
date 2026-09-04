package com.escola.escola.repository;

import com.escola.escola.models.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    Voto findByUserId(Long userId);

    Voto findByChapaId(Long chapaId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.chapa.id = :chapa_id")
    Long countVotos(@Param("chapa_id") Long chapaId);
}
