package com.romankryvolapov.resolve.resolve.repository;


import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Test
    void saveAndFindById() {
        UserEntity u = UserEntity.builder()
                .name("Test")
                .email("t@e")
                .build();
        UserEntity saved = userRepo.save(u);
        Optional<UserEntity> found = userRepo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getName());
    }

    @Test
    void findAll() {
        userRepo.save(UserEntity.builder().name("A").email("a").build());
        userRepo.save(UserEntity.builder().name("B").email("b").build());

        var list = userRepo.findAll();
        assertEquals(2, list.size());
    }
}