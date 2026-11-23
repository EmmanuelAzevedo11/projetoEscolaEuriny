package com.escola.escola.repository;

import com.escola.escola.models.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsByUserId(Long userId);

    Voto findByUserId(Long userId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.chapa.id = :chapa_id")
    Long countVotos(@Param("chapa_id") Long chapaId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Voto v WHERE v.chapa.id = :chapaId")
    void deleteByChapaId(@Param("chapaId") Long chapaId);
}
