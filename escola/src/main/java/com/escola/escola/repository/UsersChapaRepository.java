package com.escola.escola.repository;

import com.escola.escola.models.UsersChapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersChapaRepository extends JpaRepository<UsersChapa, Long> {

    List<UsersChapa> findByChapaId(Long chapaId);

    @Modifying
    @Query("DELETE FROM UsersChapa uc WHERE uc.chapa.id = :chapaId")
    void deleteByChapaId(@Param("chapaId") Long chapaId);
}
