package io.springbatch.springbatch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "push_agree", nullable = false)
    private Boolean pushAgree;

    @Builder
    private Member(String name, String email, Boolean pushAgree) {
        this.name = name;
        this.email = email;
        this.pushAgree = pushAgree;
    }
}
