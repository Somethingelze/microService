package com.some.micro.model.entities;

import com.some.micro.model.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    UUID id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;  /*связь с пользователем (Foreign Key)*/

    String description;
    //описание заказа

    @Enumerated(EnumType.STRING)
    Status status;

    @CreationTimestamp
    LocalDateTime created_at;
}
