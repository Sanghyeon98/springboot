package com.keduit.DTO;

import com.keduit.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {
    private String id;
    private String title;
    private boolean done;

    public TodoDTO(final TodoEntity entity){
        this.id=entity.getId();
        this.title=entity.getTitle();
        this.done=entity.isDone();
    }

    // 화면에서 전달받은 DTO를 ENTITY 타입으로 바로 바꿔주는 메소드
    public static TodoEntity toEntity(final TodoDTO dto){
        return TodoEntity.builder()
                .title(dto.getTitle())
                .id(dto.getId())
                .done(dto.isDone())
                .build();
    }
}
