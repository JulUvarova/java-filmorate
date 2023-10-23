package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User extends BaseModel {
    private String name;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    @Pattern(regexp = "\\S+", message = "Логин не может быть пустым и содержать пробелы")//мб null
    private String login;

    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта должна быть оформлена по правилам")
    private String email;

    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")//мб null
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long user) {
        friends.add(user);
    }

    public void deleteFriend(Long user) {
        friends.remove(user);
    }
}
