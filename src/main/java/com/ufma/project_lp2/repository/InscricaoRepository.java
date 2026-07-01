package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByOportunidadeId(Long oportunidadeId);
}

