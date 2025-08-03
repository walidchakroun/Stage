package tn.esprit.exam.coursclassroom.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUtilisateur;

	private String nom;
	private String prenom;
	private String password;
	
	@ManyToOne
	@JsonIgnore
	private Classe classe;
	 
}
