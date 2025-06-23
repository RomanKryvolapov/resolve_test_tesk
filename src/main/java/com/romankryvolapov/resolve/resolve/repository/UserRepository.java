package com.romankryvolapov.resolve.resolve.repository;

import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}