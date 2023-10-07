package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User extends BaseModel {
    private String name;

    @Pattern(regexp = "\\S+",  message = "Логин не может быть пустым и содержать пробелы")
    private String login;

    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть оформлена по правилам")
    private String email;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
