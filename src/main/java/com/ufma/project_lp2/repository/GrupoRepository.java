package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Grupo;
import com.ufma.project_lp2.model.enums.StatusGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByStatus(StatusGrupo status);
    Optional<Grupo> findByNomeIgnoreCase(String nome);
}

