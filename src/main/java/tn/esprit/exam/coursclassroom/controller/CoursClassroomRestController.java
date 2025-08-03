package tn.esprit.exam.coursclassroom.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import tn.esprit.exam.coursclassroom.entities.*;
import tn.esprit.exam.coursclassroom.services.ICoursClassroomService;

@RestController
@Tag(name = "Gestion des coursClassrooms")
@AllArgsConstructor
@RequestMapping("/coursClassroom")
public class CoursClassroomRestController {


    ICoursClassroomService coursClassroomService;

	// http://localhost:8089/cours-classroom/coursClassroom/retrieve-all-coursClassrooms
	@Operation(description = "Récupérer la liste des coursClassrooms")
	@GetMapping("/retrieve-all-coursClassrooms")
	public List<CoursClassroom> getCoursClassrooms() {
		List<CoursClassroom> listCoursClassrooms = coursClassroomService.retrieveAllCoursClassrooms();
		return listCoursClassrooms;
	}

	// http://localhost:8089/cours-classroom/coursClassroom/retrieve-coursClassroom/8
	@Operation(description = "Récupérer un coursClassroom par Id")
	@GetMapping("/retrieve-coursClassroom/{coursClassroom-id}")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Success|OK"),
//			@ApiResponse(code = 401, message = "Not Authorized!"),
//			@ApiResponse(code = 403, message = "Forbidden!"),
//			@ApiResponse(code = 404, message = "Not Found!") })
	public CoursClassroom retrieveCoursClassroom(@PathVariable("coursClassroom-id") Integer coursClassroomId) {
		return coursClassroomService.retrieveCoursClassroom(coursClassroomId);
	}

	@PostMapping("/add-coursClassroom/{classe-id}")
	public CoursClassroom addCoursClassroom(@RequestBody CoursClassroom cc, @PathVariable("classe-id") Integer classeId) {
		return coursClassroomService.ajouterCoursClassroom(cc, classeId);
	}

	// http://localhost:8089/cours-classroom/coursClassroom/remove-coursClassroom/{coursClassroom-id}
	@DeleteMapping("/remove-coursClassroom/{coursClassroom-id}")
	public void removeCoursClassroom(@PathVariable("coursClassroom-id") Integer coursClassroomId) {
		coursClassroomService.deleteCoursClassroom(coursClassroomId);
	}

	// http://localhost:8089/cours-classroom/coursClassroom/modify-coursClassroom
	@PutMapping("/modify-coursClassroom")
	public CoursClassroom modifyCoursClassroom(@RequestBody CoursClassroom coursClassroom) {
		return coursClassroomService.updateCoursClassroom(coursClassroom);
	}

	// http://localhost:8089/cours-classroom/coursClassroom//desaffecter-coursClassroom-classe/{coursClassroom-id}/{classe-id}
	@DeleteMapping("/desaffecter-coursClassroom-classe/{coursClassroom-id}")
	public void desaffecterCoursClassroomCLasse(@PathVariable("coursClassroom-id") Integer ccId) {
		coursClassroomService.desaffecterCoursClassroomClasse(ccId);
	}

	// http://localhost:8089/cours-classroom/coursClassroom/nb-heures-par-spec-niv/{spec}/{niv}
	@DeleteMapping("/nb-heures-par-spec-niv/{spec}/{niv}")
	public Integer nbHeuresParSpecEtNiv(@PathVariable("spec") Specialite spec, @PathVariable("niv") Niveau niv) {
		return coursClassroomService.nbHeuresParSpecEtNiv(spec, niv);
	}

	
}
