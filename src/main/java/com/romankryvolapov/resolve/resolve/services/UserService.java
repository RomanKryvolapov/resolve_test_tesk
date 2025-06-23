package com.romankryvolapov.resolve.resolve.services;

import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import com.romankryvolapov.resolve.resolve.models.exceptions.NotFoundServiceException;
import com.romankryvolapov.resolve.resolve.models.network.UserDTO;
import com.romankryvolapov.resolve.resolve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public UserDTO create(UserDTO dto) {
        UserEntity saved = userRepo.save(UserEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build());
        dto.setId(saved.getId());
        return dto;
    }

    public UserDTO update(Long id, UserDTO dto) {
        UserEntity u = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundServiceException("User", id));
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        userRepo.save(u);
        return UserDTO.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .build();
    }

    public List<UserDTO> listAll() {
        return userRepo.findAll().stream()
                .map(u -> UserDTO.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public UserEntity findEntity(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundServiceException("User", id));
    }
}