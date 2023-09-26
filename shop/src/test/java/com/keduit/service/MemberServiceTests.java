package com.keduit.service;

import com.keduit.dto.MemberFormDto;
import com.keduit.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.yml")
@Slf4j
public class MemberServiceTests {

    @Autowired
    MemberService service;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){

        MemberFormDto dto = new MemberFormDto();
        dto.setAddress("서울시 관악구 신림동");
        dto.setName("한정교");
        dto.setEmail("abc@abc.com");
        dto.setPassword("1111");
        return Member.createMember(dto , passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void testSaveMember(){
        Member member = createMember();
        Member savedMember = service.saveMember(member);

        assertEquals(member.getEmail(),savedMember.getEmail());
        assertEquals(member.getName(),savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(),savedMember.getPassword());
        assertEquals(member.getRole(),savedMember.getRole());
    }

    @Test
    @DisplayName("중복회원가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        service.saveMember(member1);

        Throwable e= assertThrows(IllegalStateException.class, ()->{
            service.saveMember(member2);
        });

        assertEquals("이미 가입된 회원입니다.",e.getMessage());
    }
}
