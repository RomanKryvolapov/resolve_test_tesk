package com.romankryvolapov.resolve.resolve.models.network;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "name must not be empty")
    private String name;

    @NotBlank(message = "email must not be empty")
    @Email(message = "incorrect email format")
    private String email;

}