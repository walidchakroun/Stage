package tn.esprit.exam.coursclassroom.services;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.exam.coursclassroom.entities.Classe;
import tn.esprit.exam.coursclassroom.entities.Niveau;
import tn.esprit.exam.coursclassroom.entities.Utilisateur;
import tn.esprit.exam.coursclassroom.repository.ClasseRepository;
import tn.esprit.exam.coursclassroom.repository.UtilisateurRepository;

@Service
@AllArgsConstructor
@Slf4j
public class UtilisateurServiceImpl implements IUtilisateurService {
	

	UtilisateurRepository utilisateurRepository;
	ClasseRepository classeRepository;
	
	@Override
	public List<Utilisateur> retrieveAllUtilisateurs() {
		List<Utilisateur> listUtilisateur= utilisateurRepository.findAll();
		for(Utilisateur u:listUtilisateur)
		{
			log.info("Utilisateur:" + u.getNom()+ u.getPrenom());
		}
		return listUtilisateur;
	}

	@Override
	public Utilisateur ajouterUtilisateur(Utilisateur u) {
		utilisateurRepository.save(u);
		return u;
	}

	@Override
	public void deleteUtilisateur(Integer id) {
		utilisateurRepository.deleteById(id);

	}

	@Override
	public Utilisateur updateUtilisateur(Utilisateur u) {
		utilisateurRepository.save(u);
		return u;
	}

	@Override
	public Utilisateur retrieveUtilisateur(Integer id) {
		Utilisateur c= utilisateurRepository.findById(id).get();
		return c;
	}

	@Override
	public void affecterUtilisateurClasse(Integer idUtilisateur, Integer idClasse) {
		Classe c = classeRepository.findById(idClasse).get(); 
		Utilisateur u = utilisateurRepository.findById(idUtilisateur).get(); 
		 
		u.setClasse(c);
		utilisateurRepository.save(u); 
		
	}

	public Integer nbUtilisateursParNiveau(Niveau n)
	{
		return utilisateurRepository.nbUtilisateurParNiveau(n); 
	}


	
//	@Override
//	public void desaffecterUtilisateurCoursClassroom(Integer idUtilisateur, Integer idCoursClassroom) {
//		CoursClassroom cc = coursClassroomRepository.findById(idCoursClassroom).get(); 
//		Utilisateur u = utilisateurRepository.findById(idUtilisateur).get(); 
//		
//		Set<CoursClassroom> ccs = u.getCoursClassrooms(); 
//		ccs.remove(cc); 
//		u.setCoursClassrooms(ccs);
//		utilisateurRepository.save(u); 
//		
//	}
	
//	@Override
//	public Set<CoursClassroom> chercherCoursParUtilisateur(Integer idUtilisateur) {
//		Utilisateur u = utilisateurRepository.findById(idUtilisateur).get(); 
//		return u.getCoursClassrooms(); 
//	}

}
