package com.ufma.project_lp2.model;


import com.ufma.project_lp2.model.enums.Papel;
import com.ufma.project_lp2.model.enums.StatusMatricula;
import jakarta.persistence.*;

@Entity
public class Discente extends Usuario{

    private String matricula;
    private int semestreAtual;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Enumerated(EnumType.STRING)
    private StatusMatricula statusMatricula = StatusMatricula.ATIVO;

    public Discente() {
    }

    public Discente(String nome, String email,
                    String senha, Papel papel,
                    String matricula, int semestreAtual,
                    Curso curso) {
        super(nome, email, senha, papel);
        this.matricula = matricula;
        this.semestreAtual = semestreAtual;
        this.curso = curso;
        this.statusMatricula = StatusMatricula.ATIVO;
    }

    public void mudarCurso(Curso novoCurso) {
        this.curso = novoCurso;
    }
    
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getSemestreAtual() {
        return semestreAtual;
    }
    public void setSemestreAtual(int semestreAtual) {
        this.semestreAtual = semestreAtual;
    }

    public Curso getCurso() {
        return curso;
    }
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public StatusMatricula getStatusMatricula() {
        return statusMatricula;
    }
    public void setStatusMatricula(StatusMatricula statusMatricula) {
        this.statusMatricula = statusMatricula;
    }
    @Override
    public String toString() {
        return "Discente [" +
               "dados_usuario=" + super.toString() +
               ", matricula=" + matricula + 
               ", semestre_atual=" + semestreAtual +
               ", curso=" + curso + 
               ", statusMatricula=" + statusMatricula + "]";
    }

    @Override
    public void exibirPerfil() {
        String nomeCurso = "Sem Curso";
        if (curso != null) {
            nomeCurso = curso.getNome();
        }
        System.out.println("Perfil de Discente: " + getNome() + " | Matrícula: " + matricula + " | Curso: " + nomeCurso);
    }
}
