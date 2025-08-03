package tn.esprit.exam.coursclassroom.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.exam.coursclassroom.entities.Classe;
import tn.esprit.exam.coursclassroom.entities.CoursClassroom;
import tn.esprit.exam.coursclassroom.repository.ClasseRepository;
import tn.esprit.exam.coursclassroom.repository.CoursClassroomRepository;

@Service
@AllArgsConstructor
@Slf4j
public class ClasseServiceImpl implements IClasseService {

	ClasseRepository classeRepository;
	CoursClassroomRepository coursClassroomRepository;
	

	@Override
	public List<Classe> retrieveAllClasses() {
		List<Classe> listClasse= classeRepository.findAll();
		for(Classe c:listClasse)
		{
			log.info("Classe:" + c.getTitre()+ " " + c.getNiveau() + " " + c.getCodeClasse());
		}
		return listClasse;
	}

	@Override
	public Classe ajouterClasse(Classe c) {
		classeRepository.save(c);
		return c;
	}

	@Override
	public void deleteClasse(Integer id) {
		classeRepository.deleteById(id);

	}

	@Override
	public Classe updateClasse(Classe c) {
		classeRepository.save(c);
		return c;
	}

	@Override
	public Classe retrieveClasse(Integer id) {
		Classe c= classeRepository.findById(id).get();
		return c;
	}

	@Override
	public Classe AjouterClasseEtAffecterCoursClasse(Classe c, Integer idCours) {
		CoursClassroom cc= coursClassroomRepository.findById(idCours).get();
		
		Set<CoursClassroom> ccs = new HashSet<CoursClassroom>();
		ccs.add(cc); 
		c.setCoursClassrooms(ccs);
		
		return classeRepository.save(c); 
		
	}

}
