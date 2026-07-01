package com.ufma.project_lp2.repository;

import com.ufma.project_lp2.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    List<Certificado> findByDiscenteId(Long discenteId);
    Optional<Certificado> findByUuidHash(String uuidHash);
}

