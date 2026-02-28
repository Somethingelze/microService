package com.some.micro.repository;

import com.some.micro.model.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    void deleteById(UUID id);
    Optional<OrderEntity> findById(UUID id);
    boolean existsById(UUID id);
}
