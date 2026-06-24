package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.StatusAssinatura;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuidHash;

    @ManyToOne
    @JoinColumn(name = "discente_id")
    private Discente discente;

    @ManyToOne
    @JoinColumn(name = "oportunidade_id")
    private Oportunidade oportunidade;

    private LocalDate dataEmissao;
    private int horas;
    private String certificadoPath;

    @Enumerated(EnumType.STRING)
    private StatusAssinatura statusAssinatura;

    public Certificado() {
    }

    public Certificado(Discente discente, Oportunidade oportunidade,
                       int horas, String certificadoPath) {
        this.uuidHash = UUID.randomUUID().toString();
        this.discente = discente;
        this.oportunidade = oportunidade;
        this.dataEmissao = LocalDate.now();
        this.horas = horas;
        this.certificadoPath = certificadoPath;
        this.statusAssinatura = StatusAssinatura.PENDENTE;
    }

    public String gerarQRCode() {
        if (discente == null || oportunidade == null) {
            System.out.println("Não da pra gerar o QR Code: dicente ou oportunidade invalidos");
            return null;
        }

        String conteudoQR = "CERTIFICADO-UFMA"
                + "|HASH:" + uuidHash
                + "|ALUNO:" + discente.getNome()
                + "|MATRICULA:" + discente.getMatricula()
                + "|OPORTUNIDADE:" + oportunidade.getTitulo()
                + "|HORAS:" + horas
                + "|EMISSAAO:" + dataEmissao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + "|STATUS:" + statusAssinatura.name();

        System.out.println("=========================================");
        System.out.println("          QR CODE - CERTIFICADO          ");
        System.out.println("=========================================");
        System.out.println(conteudoQR);
        System.out.println("=========================================");

        return conteudoQR;
    }

    public boolean verificarAutenticidade(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            System.out.println("Hash informado eh invalido");
            return false;
        }

        boolean autentico = this.uuidHash.equals(hash);

        if (autentico) {
            System.out.println("Certificado valido");
        } else {
            System.out.println("Certificado invalido");
        }

        return autentico;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getUuidHash() {
        return uuidHash;
    }
    public void setUuidHash(String uuidHash) {
        this.uuidHash = uuidHash;
    }

    public Discente getDiscente() {
        return discente;
    }
    public void setDiscente(Discente discente) {
        this.discente = discente;
    }

    public Oportunidade getOportunidade() {
        return oportunidade;
    }
    public void setOportunidade(Oportunidade oportunidade) {
        this.oportunidade = oportunidade;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }
    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public int getHoras() {
        return horas;
    }
    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getCertificadoPath() {
        return certificadoPath;
    }
    public void setCertificadoPath(String certificadoPath) {
        this.certificadoPath = certificadoPath;
    }

    public StatusAssinatura getStatusAssinatura() {
        return statusAssinatura;
    }
    public void setStatusAssinatura(StatusAssinatura statusAssinatura) {
        this.statusAssinatura = statusAssinatura;
    }

    @Override
    public String toString() {
        return "Certificado{" +
                "uuidHash='" + uuidHash + '\'' +
                ", discente=" + (discente != null ? discente.getNome() : "N/A") +
                ", oportunidade=" + (oportunidade != null ? oportunidade.getTitulo() : "N/A") +
                ", dataEmissao=" + dataEmissao +
                ", horas=" + horas +
                ", certificadoPath='" + certificadoPath + '\'' +
                ", statusAssinatura=" + statusAssinatura +
                '}';
    }
}
