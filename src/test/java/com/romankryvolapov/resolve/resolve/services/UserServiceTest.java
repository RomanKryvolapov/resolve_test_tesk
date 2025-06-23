package com.romankryvolapov.resolve.resolve.services;

import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import com.romankryvolapov.resolve.resolve.models.exceptions.NotFoundServiceException;
import com.romankryvolapov.resolve.resolve.models.network.UserDTO;
import com.romankryvolapov.resolve.resolve.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("create should save and return dto with id")
    void create_ShouldSaveAndReturnDtoWithId() {
        UserDTO dto = new UserDTO(null, "Alice", "alice@example.com");
        UserEntity saved = UserEntity.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .build();

        when(userRepo.save(any(UserEntity.class))).thenReturn(saved);

        UserDTO result = userService.create(dto);

        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        verify(userRepo).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("update should throw NotFoundServiceException when user not found")
    void update_ShouldThrow_WhenUserNotFound() {
        UserDTO dto = new UserDTO(null, "Bob", "bob@example.com");
        when(userRepo.findById(42L)).thenReturn(Optional.empty());

        assertThrows(NotFoundServiceException.class, () -> userService.update(42L, dto));
        verify(userRepo, never()).save(any());
    }

    @Test
    @DisplayName("update should modify existing user")
    void update_ShouldModifyExistingUser() {
        UserEntity existing = UserEntity.builder()
                .id(2L)
                .name("Old")
                .email("old@example.com")
                .build();
        when(userRepo.findById(2L)).thenReturn(Optional.of(existing));
        UserDTO dto = new UserDTO(null, "New", "new@example.com");

        UserDTO result = userService.update(2L, dto);

        assertEquals(2L, result.getId());
        assertEquals("New", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepo).save(existing);
    }

    @Test
    @DisplayName("listAll should return list of dtos")
    void listAll_ShouldReturnListOfDto() {
        UserEntity u1 = UserEntity.builder().id(1L).name("A").email("a@e.com").build();
        UserEntity u2 = UserEntity.builder().id(2L).name("B").email("b@e.com").build();
        when(userRepo.findAll()).thenReturn(Arrays.asList(u1, u2));

        var list = userService.listAll();

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getName());
        assertEquals("B", list.get(1).getName());
    }
}
