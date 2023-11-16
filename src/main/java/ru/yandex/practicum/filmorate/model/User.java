package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    private List<Long> friends = new ArrayList<>();

    public User(long id, String name, String login, String email, LocalDate birthday, List<Long> friends) {
        super(id);
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.friends = friends;
    }

    public void addFriend(Long user) {
        friends.add(user);
    }

    public void deleteFriend(Long user) {
        friends.remove(user);
    }
}
