package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: ")
    @Size(min = 2, max = 250)
    private String name;
    @Email
    @Size(min = 6, max = 254)
    @NotBlank(message = "Field: email. Error: must not be blank. Value: ")
    private String email;
}
