package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Transactional
    @Modifying
    @Query("update TokenEntity t set t.accessToken = ?1, t.refreshToken = ?2")
    int updateAccessTokenAndRefreshTokenBy(String accessToken, String refreshToken);

    long deleteByIdAndAccessTokenAndRefreshToken(Long id, String accessToken, String refreshToken);

    TokenEntity findByAccessToken(String accessToken);

    long deleteByRefreshToken(String refreshToken);

    TokenEntity findByRefreshToken(String refreshToken);
}