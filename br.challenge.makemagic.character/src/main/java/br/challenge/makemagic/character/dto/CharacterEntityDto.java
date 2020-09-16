//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.character.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class defines the character information.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CharacterEntityDto
{
    /**
     * Specifies the Id for the entity.
     */
    @Id
    @EqualsAndHashCode.Include
    private String id;

    /**
     * Specifies the name for the character.
     */
    @NotNull(message = "The field name is mandatory.")
    @Column(nullable = false)
    private String name;

    /**
     * Specifies the role for the character.
     */
    @NotNull(message = "The field role is mandatory.")
    @Column(nullable = false)
    private String role;

    /**
     * Specifies the name of school for the character.
     */
    @NotNull(message = "The field school is mandatory.")
    @Column(nullable = false)
    private String school;

    /**
     * Specifies the Id of the house for the character.
     */
    @NotNull(message = "The field house is mandatory.")
    @Column(nullable = false)
    private String house;

    /**
     * Specifies the patronus for the character.
     */
    @NotNull(message = "The field patronus is mandatory.")
    @Column(nullable = false)
    private String patronus;
}
