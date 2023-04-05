package com.codecool.CodeCoolProjectGrande.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;
import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID userId = UUID.randomUUID();
    @NotNull
    private String name;
    private int age;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    private String imgUrl;
    private String location;
    @JoinColumn(name = "tokenId")
    @OneToOne(cascade=CascadeType.ALL)
    private ResetPasswordToken resetPasswordToken;

    public User(@NotNull String name, @NotNull String password, @NotNull String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.userType = UserType.USER;
    }

    //    @JsonIgnore
//    @ManyToMany(mappedBy = "assignedUsers", fetch = FetchType.EAGER)
//    private Set<Event> events = new HashSet<>();

//    public void addEvent(Event event) {
//        events.add(event);
//    }
}
