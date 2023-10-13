package com.keduit.service;


import com.keduit.model.TodoEntity;
import com.keduit.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public String testService(){
        TodoEntity todo = TodoEntity.builder()
                .title("My first Todo List !")
                .build();
        repository.save(todo);
        TodoEntity savedEntity =repository.findById(todo.getId()).get();
        return savedEntity.getTitle();
    }

    private void validate(final TodoEntity entity){
        if(entity == null){
            log.warn("Entity is null");
            throw new RuntimeException("Entity is null");
        }

        if(entity.getUserId() ==null){
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> create(TodoEntity entity) {

        validate(entity);
        repository.save(entity);
        log.info("Entity Id: {} is saved. ", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }
    public List<TodoEntity> read(String userId){
        return repository.findByUserId(userId);
    }
    
    public List<TodoEntity> update(final TodoEntity entity){
        validate(entity);
        
        final Optional<TodoEntity> original =repository.findById(entity.getId());
        original.ifPresent(todo ->{
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });
        return read(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity){
        validate(entity);

        try {
            repository.delete(entity);
        }catch(Exception e){
            log.error("delete error ", entity.getId(), e);
            throw new RuntimeException("delete error "+entity.getId());
        }

        return read(entity.getUserId());
    }

}
