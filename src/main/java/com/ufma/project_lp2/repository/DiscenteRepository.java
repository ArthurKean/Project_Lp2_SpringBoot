package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Discente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscenteRepository extends JpaRepository<Discente, Long> {
}
