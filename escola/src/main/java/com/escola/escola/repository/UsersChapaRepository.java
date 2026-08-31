package com.escola.escola.repository;

import com.escola.escola.models.UsersChapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersChapaRepository extends JpaRepository<UsersChapa, Long> {
}
