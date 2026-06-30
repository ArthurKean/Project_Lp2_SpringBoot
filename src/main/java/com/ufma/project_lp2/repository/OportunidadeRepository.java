package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Oportunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
}
