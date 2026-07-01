package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.enums.StatusOportunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    Optional<Oportunidade> findByTituloIgnoreCase(String titulo);
    List<Oportunidade> findByStatusIn(List<StatusOportunidade> statuses);
}

