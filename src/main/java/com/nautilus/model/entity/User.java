package com.nautilus.model.entity;

import com.fasterxml.jackson.annotation.*;
import com.nautilus.model.RequestView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "login")
@Table(name = "users")
@ToString(of = {"id", "login", "group"})
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, unique = true)
    private String login;

    @NotNull(groups = RequestView.UserMarker.class)
    @Size(min = 1, max = 255)
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @Column(nullable = false)
    private String hashPassword;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinColumn(name = "group_id", nullable = false, updatable = false)
    // скрыто т.к. заигнорил вывод юзеров в группах
//    @JsonIdentityReference //
//    @JsonIdentityInfo(
//            property = "id",
//            generator = ObjectIdGenerators.PropertyGenerator.class
//    )
    private Group group;

    @JsonIgnore
    @Valid
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Translate> translates = new HashSet<>();
}
