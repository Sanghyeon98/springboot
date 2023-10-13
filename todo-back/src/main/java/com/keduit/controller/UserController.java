package com.keduit.controller;

import com.keduit.DTO.ResponseDTO;
import com.keduit.DTO.UserDTO;
import com.keduit.model.UserEntity;
import com.keduit.security.TokenProvider;
import com.keduit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){
        try{
            if(userDTO == null || userDTO.getPassword() == null){
                throw new RuntimeException("사용자 또는 비밀번호가 없습니다.!");
            }

            UserEntity user = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();

            UserEntity registerdUser = userService.create(user);

            UserDTO responseUserDTO =UserDTO.builder()
                    .id(registerdUser.getId())
                    .username(registerdUser.getUsername())
                    .password(registerdUser.getPassword())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> athunticate(@RequestBody UserDTO userDTO){
      UserEntity user = userService.getByCredentials(
              userDTO.getUsername(), userDTO.getPassword(),passwordEncoder);

      if(user != null){

          final String token = tokenProvider.create(user);
          log.info(token);
          final UserDTO responseUserDTO = UserDTO.builder()
                  .username(user.getUsername())
                  .token(token)
                  .build();
          return ResponseEntity.ok().body(responseUserDTO);
      }else {
          ResponseDTO responseDTO = ResponseDTO.builder()
                  .error("Login error")
                  .build();
          return ResponseEntity.badRequest().body(responseDTO);
      }

    }

}
