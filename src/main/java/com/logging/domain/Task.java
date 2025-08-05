package com.logging.domain;

import com.logging.exception.NotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.logging.enumeration.ErrorCode.ALREADY_DELETED;

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

    private Boolean deleted;

    @Builder
    private Task(Long id, String title, String description, Boolean completed, Boolean deleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.deleted = deleted;
    }

    public void update(String title, String description, Boolean completed) {
        this.title = title != null ? title : this.title;
        this.description = description != null ? description : this.description;
        this.completed = completed != null ? completed : this.completed;
    }

    public void delete() {
        if (this.deleted) {
            throw new NotFoundException(ALREADY_DELETED);
        }
        this.deleted = true;
    }

}
