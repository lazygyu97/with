package com.sparta.with.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.sparta.with.dto.SignupRequestDto;
import com.sparta.with.dto.UserResponseDto;
import com.sparta.with.entity.redishash.Blacklist;
import com.sparta.with.entity.User;
import com.sparta.with.entity.UserRoleEnum;
import com.sparta.with.entity.redishash.EmailVerification;
import com.sparta.with.entity.redishash.RefreshToken;
import com.sparta.with.file.S3Uploader;
import com.sparta.with.jwt.JwtUtil;
import com.sparta.with.repository.BlacklistRepository;
import com.sparta.with.repository.EmailVerificationRepository;
import com.sparta.with.repository.RefreshTokenRepository;
import com.sparta.with.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistRepository blacklistRepository;
    private final JwtUtil jwtUtil;
    private final S3Uploader s3Uploader;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signUp(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        EmailVerification emailVerification = emailVerificationRepository.findById(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일 인증 코드를 발송하지 않았습니다."));
        if (!emailVerification.isVerificated()) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }
        // 회원 아이디 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        // 회원 닉네임 중복 확인
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        // email 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = User.builder()
            .username(username)
            .password(password)
            .nickname(nickname)
            .email(email)
            .role(role)
            .build();

        userRepository.save(user);
    }

    public void logout(User user, HttpServletRequest request) {
        String refreshTokenVal = request.getHeader("RefreshToken").substring(7);
        String accessTokenVal = jwtUtil.getJwtFromHeader(request);

        try {
            Key key = jwtUtil.getKey();
            Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessTokenVal)
                .getBody()
                .getExpiration();
            long expireTime = expiration.getTime() - System.currentTimeMillis();
            blacklistRepository.save(new Blacklist(accessTokenVal, expireTime / 1000));
            RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenVal)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 없습니다."));
            refreshTokenRepository.delete(refreshToken);
            System.out.println(expiration.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //BoardService 협업자 등록에서 현재 사용중
    public User findUserByUserid(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    public UserResponseDto getUserInfo(User user) {
        return UserResponseDto.of(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::of).toList();
    }

    public UserResponseDto updateProfile(Long id, MultipartFile image) {

        User user = findUserByUserid(id);

        if (image != null) {
            try {
                // 프로필 사진 S3 업로드
                String imageUrl = s3Uploader.upload(image, "image");
                user.updateImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return UserResponseDto.of(user);
    }
}