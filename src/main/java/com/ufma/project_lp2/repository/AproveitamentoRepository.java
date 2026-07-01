package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Aproveitamento;
import com.ufma.project_lp2.model.enums.StatusAproveitamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AproveitamentoRepository extends JpaRepository<Aproveitamento, Long> {
    List<Aproveitamento> findByStatus(StatusAproveitamento status);
    List<Aproveitamento> findByDiscenteIdAndStatus(Long discenteId, StatusAproveitamento status);
}

