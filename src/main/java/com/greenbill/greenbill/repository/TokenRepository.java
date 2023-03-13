package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    TokenEntity findByAccessToken(String accessToken);

    TokenEntity findByRefreshToken(String refreshToken);
}