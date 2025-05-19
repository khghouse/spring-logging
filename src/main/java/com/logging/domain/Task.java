package com.logging.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String description;

    private Boolean completed;

    @Builder
    private Task(Long id, String title, String description, Boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public void update(String title, String description, Boolean completed) {
        this.title = title != null ? title : this.title;
        this.description = description != null ? description : this.description;
        this.completed = completed != null ? completed : this.completed;
    }

}
