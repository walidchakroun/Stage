package tn.esprit.exam.coursclassroom.services;

import java.util.List;

import tn.esprit.exam.coursclassroom.entities.Niveau;
import tn.esprit.exam.coursclassroom.entities.Specialite;
import tn.esprit.exam.coursclassroom.entities.CoursClassroom;


public interface ICoursClassroomService {

	List<CoursClassroom> retrieveAllCoursClassrooms();

	CoursClassroom ajouterCoursClassroom(CoursClassroom cc, Integer classeId);

	void deleteCoursClassroom(Integer id);

	CoursClassroom updateCoursClassroom(CoursClassroom cc);

	CoursClassroom retrieveCoursClassroom(Integer id);
	
	public void desaffecterCoursClassroomClasse(Integer idCours); 
		
	public void archiverCoursClassrooms(); 
	
	public Integer nbHeuresParSpecEtNiv(Specialite sp, Niveau nv);
	
}
