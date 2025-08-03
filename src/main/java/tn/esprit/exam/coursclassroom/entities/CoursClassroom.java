package tn.esprit.exam.coursclassroom.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoursClassroom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCours;

	private String nom;
	private Integer nbHeures;
	Boolean archive; 
	
	@Enumerated(EnumType.STRING)
	private Specialite specialite;
	 
	@JsonIgnore
	@ManyToOne  
	private Classe classe; 
	 
}
