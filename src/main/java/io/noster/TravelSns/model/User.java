package io.noster.TravelSns.model;

import io.noster.TravelSns.payload.LoginMethodEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="demo_user")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String email;

    @NonNull
    private String password;

    private String userRole;

    @NonNull
    @Enumerated(EnumType.STRING)
    private LoginMethodEnum loginMethodEnum;

    public User(String email, String password, LoginMethodEnum loginMethodEnum, String role_user) {
        this.email = email;
        this.password = password;
        this.loginMethodEnum = loginMethodEnum;
        this.userRole = role_user;
    }
}
