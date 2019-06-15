package com.nautilus.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "sourceWord")
@Table(name = "translates")
public class Translate {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column(nullable = false, unique = true, name = "source_word")
    @NotNull
    private String sourceWord;

    @Column(nullable = false, name = "translation_word")
    @NotNull
    private String translationWord;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;
}
