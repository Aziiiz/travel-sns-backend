package io.noster.TravelSns.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="user")
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

    @NonNull
    @Enumerated(EnumType.STRING)
    private LoginMethodEnum loginMethodEnum;
}
