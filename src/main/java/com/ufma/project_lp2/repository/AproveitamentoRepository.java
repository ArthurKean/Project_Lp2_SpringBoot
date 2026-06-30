package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Aproveitamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AproveitamentoRepository extends JpaRepository<Aproveitamento, Long> {
}
