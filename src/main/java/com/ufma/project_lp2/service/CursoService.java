package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Curso;
import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.enums.StatusMatricula;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {

    private List<Curso> bancoDeCursos;

    public CursoService() {
        this.bancoDeCursos = new ArrayList<>();
    }

    public void cadastrarCurso(Curso curso) {
        if (curso == null) {
            System.out.println("Curso nulo. Operação cancelada");
            return;
        }
        if (buscarPorCodigo(curso.getCodigo()) != null) {
            System.out.println("Já existe um curso com o código " + curso.getCodigo() + " cadastrado no sistema");
            return;
        }
        bancoDeCursos.add(curso);
        System.out.println("O curso '" + curso.getNome() + "' (PPC Versão: " + curso.getVersaoPpc() + ") foi cadastrado com sucesso");
    }

    public Curso buscarPorCodigo(int codigo) {
        for (Curso c : bancoDeCursos) {
            if (c.getCodigo() == codigo) {
                return c;
            }
        }
        return null;
    }

    public void atualizarPPC(int codigo, int novasCargaHoraria, String novaVersao) {
        Curso curso = buscarPorCodigo(codigo);
        if (curso == null) {
            System.out.println("Curso com código " + codigo + " não encontrado para revisão de PPC.");
            return;
        }
        System.out.println("REVISÃO INSTITUCIONAL: Iniciando atualização do PPC do curso '" + curso.getNome() + "'...");
        curso.atualizarPPC(novasCargaHoraria, novaVersao); // Chama o método da classe Curso
        System.out.println("Atualização do PPC concluída no sistema");
    }

    public void iniciarRevisaoDePPC(Curso curso, int novasCargaHoraria, String novaVersao) {
        if (curso == null) {
            System.out.println("Curso invalido para revisão de PPC.");
            return;
        }
        atualizarPPC(curso.getCodigo(), novasCargaHoraria, novaVersao);
    }

    public int obterCargaHoraria(int codigo) {
        Curso curso = buscarPorCodigo(codigo);
        if (curso != null) {
            System.out.println("A carga horária total exigida pelo PPC do curso '" + curso.getNome() + "' é de " + curso.getCargaHoraria() + " horas.");
            return curso.getCargaHoraria();
        }
        System.out.println("Curso não encontrado.");
        return -1;
    }

    public List<Curso> listarTodos() {
        System.out.println("CATÁLOGO DE CURSOS UFMA:");
        if (bancoDeCursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado no momento");
        }
        for (Curso c : bancoDeCursos) {
            System.out.println("- [" + c.getCodigo() + "] " + c.getNome() + " | PPC: " + c.getVersaoPpc() + " | Carga Horária: " + c.getCargaHoraria() + "h");
        }
        return new ArrayList<>(bancoDeCursos);
    }

    public boolean removerCurso(int codigo) {
        Curso curso = buscarPorCodigo(codigo);
        if (curso == null) {
            System.out.println("Curso com código " + codigo + " não encontrado");
            return false;
        }
        bancoDeCursos.remove(curso);
        System.out.println("O curso '" + curso.getNome() + "' foi removido do sistema");
        return true;
    }

    public void matricularDiscente(int codigoCurso, Discente discente) {
        Curso curso = buscarPorCodigo(codigoCurso);
        if (curso == null) {
            System.out.println("Curso com código " + codigoCurso + " não encontrado");
            return;
        }
        if (discente == null) {
            System.out.println("Discente inválido");
            return;
        }

        for (Discente d : curso.getDiscentes()) {
            if (d.getMatricula().equals(discente.getMatricula())) {
                System.out.println("O discente '" + discente.getNome() + "' já está matriculado neste curso");
                return;
            }
        }
        
        curso.adicionarDiscente(discente);
        System.out.println("Matrícula confirmada! O aluno '" + discente.getNome() + "' está oficialmente matriculado no curso '" + curso.getNome() + "'.");
    }

    public List<Discente> listarAlunosPorStatus(int codigoCurso, StatusMatricula status) {
        Curso curso = buscarPorCodigo(codigoCurso);
        if (curso == null) {
            System.out.println("Curso com código " + codigoCurso + " não encontrado.");
            return new ArrayList<>();
        }
        
        List<Discente> filtrados = curso.listarAlunosPorStatus(status);
        System.out.println("Alunos com status '" + status + "' no curso " + curso.getNome() + " (" + filtrados.size() + "):");
        for(Discente d : filtrados) {
            System.out.println("- " + d.getNome() + " | Matrícula: " + d.getMatricula());
        }
        return filtrados;
    }

    public int totalDeAlunos(int codigoCurso) {
        Curso curso = buscarPorCodigo(codigoCurso);
        if (curso == null) {
            System.out.println("Curso com código " + codigoCurso + " não encontrado.");
            return -1;
        }
        int total = curso.getDiscentes().size();
        System.out.println("O curso '" + curso.getNome() + "' possui atualmente " + total + " alunos matriculados.");
        return total;
    }
}
