//**********************************************************************
// **********************************************************************
package br.challenge.makemagic.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.challenge.makemagic.model.CharacterModel;
import br.challenge.makemagic.repository.CharacterRepository;

@Controller
@RequestMapping(path = "/character")
public class CharacterController {

    @Autowired
    private CharacterRepository repository;

    @RequestMapping(path = "*")
    public ResponseEntity<Object> methodsNotAllowed() {
	return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<CharacterModel>> getCharacterModel() {
	Iterable<CharacterModel> characters = repository.findAll();

	return new ResponseEntity<>(characters, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CharacterModel> createCharacter(@RequestBody CharacterModel character) {

	repository.save(character);

	return new ResponseEntity<CharacterModel>(character, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CharacterModel> patchCharacter(@RequestBody CharacterModel character, @PathVariable Long id) {
	character.setId(id);

	repository.save(character);

	return new ResponseEntity<CharacterModel>(character, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CharacterModel> putCharacter(@RequestBody CharacterModel character, @PathVariable Long id) {
	character.setId(id);

	repository.save(character);

	return new ResponseEntity<CharacterModel>(character, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CharacterModel> deleteCharacter(@PathVariable Long id) {
	repository.deleteById(id);

	return new ResponseEntity<CharacterModel>(HttpStatus.NO_CONTENT);
    }
}
