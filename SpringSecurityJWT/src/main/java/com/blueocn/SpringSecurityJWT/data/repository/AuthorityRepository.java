package com.blueocn.SpringSecurityJWT.data.repository;

import com.blueocn.SpringSecurityJWT.data.entity.user.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
    Optional<AuthorityEntity> findByUsername(String username);
}
