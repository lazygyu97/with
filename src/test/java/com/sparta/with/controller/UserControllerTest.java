package com.sparta.with.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.with.dto.EmailRequestDto;
import com.sparta.with.dto.EmailVerificationRequestDto;
import com.sparta.with.dto.LoginRequestDto;
import com.sparta.with.dto.SignupRequestDto;
import com.sparta.with.entity.EmailVerification;
import com.sparta.with.repository.EmailVerificationRepository;
import com.sparta.with.service.EmailService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;
    @Autowired
    EmailService emailService;

    String email = "test@example.com";

    @Test
    @DisplayName("이메일 인증코드 전송 및 확인 테스트")
    @Order(1)
    public void emailVerification() throws Exception {
        EmailRequestDto emailRequestDto = new EmailRequestDto(email);
        MvcResult result = mockMvc.perform(post("/api/users/login/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        String code = result.getResponse().getContentAsString();
    }

    @Test
    @DisplayName("이메일 인증 확인 테스트")
    @Order(2)
    public void verificationEmail() throws Exception {
        EmailVerification emailVerification = new EmailVerification("test@example.com", "authcode");
        emailVerificationRepository.save(emailVerification);
        String code = "authcode";
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(email, code);
        mockMvc.perform(get("/api/users/login/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailVerificationRequestDto)))
                .andDo(print())
                .andExpect(status().isOk());
        emailVerificationRepository.delete(emailVerification);
    }

    @Test
    @DisplayName("회원가입")
    @Order(3)
    public void signup() throws Exception {
        EmailVerification emailVerification = new EmailVerification(email, "authcode");
        emailVerification.setVerificated();
        emailVerificationRepository.save(emailVerification);

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .username("username")
                .password("password")
                .nickname("nickname")
                .email(email)
                .build();
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated());
        emailVerificationRepository.delete(emailVerification);
    }

    @Test
    @DisplayName("로그인")
    @Order(3)
    public void login() throws Exception {
        LoginRequestDto loginRequestDto = LoginRequestDto
                .builder()
                .username("username")
                .password("password")
                .build();
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}