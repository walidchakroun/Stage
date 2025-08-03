package tn.esprit.exam.coursclassroom.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import tn.esprit.exam.coursclassroom.services.IClasseService;
import tn.esprit.exam.coursclassroom.entities.Classe;

@RestController
@Tag(name = "Gestion des classes")
@AllArgsConstructor
@RequestMapping("/classe")
public class ClasseRestController {


    IClasseService classeService;

	// http://localhost:8089/cours-classroom/classe/retrieve-all-classes
	@Operation(description = "Récupérer la liste des classes")
	@GetMapping("/retrieve-all-classes")
	public List<Classe> getClasses() {
		List<Classe> listClasses = classeService.retrieveAllClasses();
		return listClasses;
	}

	// http://localhost:8089/cours-classroom/classe/retrieve-classe/8
	@Operation(description = "Récupérer un classe par Id")
	@GetMapping("/retrieve-classe/{classe-id}")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Success|OK"),
//			@ApiResponse(code = 401, message = "Not Authorized!"),
//			@ApiResponse(code = 403, message = "Forbidden!"),
//			@ApiResponse(code = 404, message = "Not Found!") })
	public Classe retrieveClasse(@PathVariable("classe-id") Integer classeId) {
		return classeService.retrieveClasse(classeId);
	}

	@PostMapping("/add-classe")
	public Classe addClasse(@RequestBody Classe c) {
		return classeService.ajouterClasse(c);
	}

	// http://localhost:8089/cours-classroom/classe/remove-classe/{classe-id}
	@DeleteMapping("/remove-classe/{classe-id}")
	public void removeClasse(@PathVariable("classe-id") Integer classeId) {
		classeService.deleteClasse(classeId);
	}

	// http://localhost:8089/cours-classroom/classe/modify-classe
	@PutMapping("/modify-classe")
	public Classe modifyClasse(@RequestBody Classe classe) {
		return classeService.updateClasse(classe);
	}

	// http://localhost:8089/cours-classroom/classe/add-classe-assign-coursclassroom/{coursclassroom-id}
	@PostMapping("/add-classe-assign-coursclassroom/{coursclassroom-id}")
	public Classe addClasse(@RequestBody Classe c, @PathVariable("coursclassroom-id") Integer coursClassroomId) {
		 return classeService.AjouterClasseEtAffecterCoursClasse(c, coursClassroomId);
	}
	
}
