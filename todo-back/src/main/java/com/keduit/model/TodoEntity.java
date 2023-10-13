package com.keduit.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name="todo")
public class TodoEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    //uuid 랜덤 데이터 넣어줌
    @GenericGenerator(name="system-uuid",strategy ="uuid")
    private String id;
    private String userId;
    private String title;
    private boolean done;
}
