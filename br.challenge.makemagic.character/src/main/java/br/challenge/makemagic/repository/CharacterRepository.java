//**********************************************************************
// This is intended to solve a challenge to create a REST API to offers
// CRUD functionalities for Harry Potter's characters.
// **********************************************************************
package br.challenge.makemagic.repository;

import org.springframework.data.repository.CrudRepository;

import br.challenge.makemagic.model.CharacterModel;

public interface CharacterRepository extends CrudRepository<CharacterModel, Long> {

}
