package tn.esprit.exam.coursclassroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.exam.coursclassroom.entities.Classe;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Integer> {


}
