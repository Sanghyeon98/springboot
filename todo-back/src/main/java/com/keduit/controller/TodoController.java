package com.keduit.controller;

import com.keduit.DTO.ResponseDTO;
import com.keduit.DTO.TodoDTO;
import com.keduit.model.TodoEntity;
import com.keduit.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){
        String str = service.testService();
        List<String> list = new ArrayList<>();

        list.add(str);

        ResponseDTO<String> response =
                ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);

    }
    @GetMapping
    public ResponseEntity<?> get(@AuthenticationPrincipal String userId){
        try{
            String tempUserId = "temporal-user";

            List<TodoEntity> entities = service.read(userId);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO :: new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto , @AuthenticationPrincipal String userId){

        try {
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userId);

            //서비스로 부터 엔티티List를 가져옴.
            List<TodoEntity> entities = service.create(entity);

            //엔티티List를 TodoDTO리스트로 변환(스트림 활용)
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //ResponesDTO를 반환
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
           //예외 발생 시 DTO대신 error에 메시지를 넣어서 리턴

           String error = e.getMessage();
           ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
        return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto, @AuthenticationPrincipal String userId){
        String tempUserId = "temporal-user";

        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(userId);

        List<TodoEntity> entities =service.update(entity);
        List<TodoDTO> dtos = entities.stream()
                .map(TodoDTO :: new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto , @AuthenticationPrincipal String userId){
        try{
            String tempUserId = "temporal-user";

            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.delete(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO :: new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }

    }
}
