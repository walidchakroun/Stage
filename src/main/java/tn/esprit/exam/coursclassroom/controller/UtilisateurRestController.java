package tn.esprit.exam.coursclassroom.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.exam.coursclassroom.entities.Niveau;
import tn.esprit.exam.coursclassroom.entities.Utilisateur;
import tn.esprit.exam.coursclassroom.services.IUtilisateurService;

@RestController
@Tag(name = "Gestion des utilisateurs")
@AllArgsConstructor
@RequestMapping("/utilisateur")
public class UtilisateurRestController {


	IUtilisateurService utilisateurService;

	// http://localhost:8089/cours-classroom/utilisateur/retrieve-all-utilisateurs
	@Operation(description = "Récupérer la liste des utilisateurs")
	@GetMapping("/retrieve-all-utilisateurs")
	public List<Utilisateur> getUtilisateurs() {
		List<Utilisateur> listUtilisateurs = utilisateurService.retrieveAllUtilisateurs();
		return listUtilisateurs;
	}

	// http://localhost:8089/cours-classroom/utilisateur/retrieve-utilisateur/8
	@Operation(description = "Récupérer un utilisateur par Id")
	@GetMapping("/retrieve-utilisateur/{utilisateur-id}")
	//	@ApiResponses(value = {
	//			@ApiResponse(code = 200, message = "Success|OK"),
	//			@ApiResponse(code = 401, message = "Not Authorized!"),
	//			@ApiResponse(code = 403, message = "Forbidden!"),
	//			@ApiResponse(code = 404, message = "Not Found!") })
	public Utilisateur retrieveUtilisateur(@PathVariable("utilisateur-id") Integer utilisateurId) {
		return utilisateurService.retrieveUtilisateur(utilisateurId);
	}

	@PostMapping("/add-utilisateur")
	public Utilisateur addUtilisateur(@RequestBody Utilisateur u) {
		return utilisateurService.ajouterUtilisateur(u);
	}

	// http://localhost:8089/cours-classroom/utilisateur/remove-utilisateur/{utilisateur-id}
	@DeleteMapping("/remove-utilisateur/{utilisateur-id}")
	public void removeUtilisateur(@PathVariable("utilisateur-id") Integer utilisateurId) {
		utilisateurService.deleteUtilisateur(utilisateurId);
	}

	// http://localhost:8089/cours-classroom/utilisateur/modify-utilisateur
	@PutMapping("/modify-utilisateur")
	public Utilisateur modifyUtilisateur(@RequestBody Utilisateur utilisateur) {
		return utilisateurService.updateUtilisateur(utilisateur);
	}
 
	// http://localhost:8089/cours-classroom/utilisateur/affecter-utilisateur-classe/{utilisateur-id}/{classe-id} 
	@PutMapping("/affecter-utilisateur-coursclassroom/{utilisateur-id}/{classe-id}")
	public void affecterUtilisateurCoursClassroom(@PathVariable("utilisateur-id") Integer utilisateurId, @PathVariable("classe-id") Integer classeId) {
		utilisateurService.affecterUtilisateurClasse(utilisateurId, classeId); 
	}

	// http://localhost:8089/cours-classroom/utilisateur/affecter-utilisateur-classe/{utilisateur-id}/{classe-id} 
	@GetMapping("/nb-utilisateur-par-niveau/{niveau}")
	public Integer nbUtilisateurParNiveau(@PathVariable("niveau") Niveau niveau) {
		return utilisateurService.nbUtilisateursParNiveau(niveau); 
	}
	
	//	// http://localhost:8089/cours-classroom/utilisateur/affecter-utilisateur-coursclassroom/{utilisateur-id}/{cc-id} 
	//	@PutMapping("/desaffecter-utilisateur-coursclassroom/{utilisateur-id}/{cc-id}")
	//	public void desaffecterUtilisateurCoursClassroom(@PathVariable("utilisateur-id") Integer utilisateurId, @PathVariable("cc-id") Integer ccId) {
	//		utilisateurService.desaffecterUtilisateurCoursClassroom(utilisateurId, ccId); 
	//	}
	//	 
	//	// http://localhost:8089/cours-classroom/utilisateur//chercher-cours-utilisateur/{utilisateur-id} 
	//	@GetMapping("/chercher-cours-utilisateur/{utilisateur-id}")
	//	public Set<CoursClassroom> chercherCoursParUtilisateur(@PathVariable("utilisateur-id") Integer utilisateurId) {
	//		return utilisateurService.chercherCoursParUtilisateur(utilisateurId); 
	//	}


}
