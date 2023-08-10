package com.sparta.with.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.with.dto.LoginRequestDto;
import com.sparta.with.entity.redishash.RefreshToken;
import com.sparta.with.entity.User;
import com.sparta.with.entity.UserRoleEnum;
import com.sparta.with.jwt.JwtUtil;
import com.sparta.with.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        log.info("로그인 성공 및 JWT 생성");
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        String username = user.getUsername();
        UserRoleEnum role = user.getRole();
        Long id = user.getId();

        System.out.println(id);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(id);
        refreshToken.ifPresent(token -> System.out.println(token.getRefreshToken()));
        refreshToken.ifPresent(token -> refreshTokenRepository.deleteByMemberId(id));

        String refreshTokenVal = UUID.randomUUID().toString();
        refreshTokenRepository.save(new RefreshToken(refreshTokenVal, id));
        String token = jwtUtil.createToken(username, role);

        response.addHeader("RefreshToken", refreshTokenVal);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException {
        log.info("로그인 실패");

        // 응답에 성공 메시지를 추가하여 클라이언트에 전달
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("메세지 : 로그인 실패\nstatus : 401");
        response.getWriter().flush();

    }

}