package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: ")
    private String name;
    @Email
    @NotBlank(message = "Field: email. Error: must not be blank. Value: ")
    private String email;
}
