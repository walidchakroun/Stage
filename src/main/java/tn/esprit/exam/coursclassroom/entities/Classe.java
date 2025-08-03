package tn.esprit.exam.coursclassroom.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Classe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codeClasse;

	private String titre;

	@Enumerated(EnumType.STRING)
	private Niveau niveau;
	
	@JsonIgnore
	@OneToMany(mappedBy="classe", fetch = FetchType.EAGER)
	private Set<CoursClassroom> coursClassrooms;
	 
}
