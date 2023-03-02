package com.greenbill.greenbill.repository;

import com.greenbill.greenbill.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {


    TokenEntity findByAccessToken(String accessToken);


    TokenEntity findByRefreshToken(String refreshToken);
}