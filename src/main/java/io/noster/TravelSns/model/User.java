package io.noster.TravelSns.model;

import io.noster.TravelSns.payload.LoginMethodEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tb_user")
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

    private String userRole;

    @NonNull
    private String userName;


    @NonNull
    @Enumerated(EnumType.STRING)
    private LoginMethodEnum loginMethodEnum;

    public User(String email, LoginMethodEnum loginMethodEnum, String role_user, String userName) {
        this.email = email;
        this.loginMethodEnum = loginMethodEnum;
        this.userRole = role_user;
        this.userName = userName;
    }
}
