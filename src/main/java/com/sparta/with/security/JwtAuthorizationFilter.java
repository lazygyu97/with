package com.sparta.with.security;

import com.sparta.with.entity.redishash.RefreshToken;
import com.sparta.with.jwt.JwtUtil;
import com.sparta.with.repository.BlacklistRepository;
import com.sparta.with.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistRepository blacklistRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository, BlacklistRepository blacklistRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getJwtFromHeader(req);
        String refreshToken = req.getHeader("RefreshToken");

        if (StringUtils.hasText(tokenValue)) {
            checkBlacklist(tokenValue);
            boolean isValid = false;
            try {
                isValid = jwtUtil.validateToken(tokenValue);
            } catch (ExpiredJwtException e){
                log.error("토큰 만료");
                System.out.println(tokenValue);
                RefreshToken refToken = refreshTokenRepository.findById(refreshToken)
                        .orElseThrow(() -> new IllegalArgumentException("리프레시 실패"));
                isValid = true;
                Long id = refToken.getMemberId();
                UserDetails userDetails = userDetailsService.loadUserById(id);

                String refreshTokenVal = UUID.randomUUID().toString();
                refreshTokenRepository.delete(refToken);
                refreshTokenRepository.save(new RefreshToken(refreshTokenVal, id));
                tokenValue = jwtUtil.createToken(userDetails.getUsername(), ((UserDetailsImpl) userDetails).getRole())
                        .substring(7);
                res.addHeader("RefreshToken", refreshTokenVal);
                res.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenValue);
            }
            if (!isValid) {
                log.error("Token Error");
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    private void checkBlacklist(String tokenValue) {
        if(blacklistRepository.existsById(tokenValue)) throw new IllegalArgumentException("로그아웃한 유저입니다.");
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);//admin2
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}