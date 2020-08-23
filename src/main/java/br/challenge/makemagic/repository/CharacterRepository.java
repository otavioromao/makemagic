//**********************************************************************
// **********************************************************************
package br.challenge.makemagic.repository;

import org.springframework.data.repository.CrudRepository;

import br.challenge.makemagic.model.CharacterModel;

public interface CharacterRepository extends CrudRepository<CharacterModel, Long> {

}
