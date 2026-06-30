package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.DiscenteDiretor;
import com.ufma.project_lp2.model.Docente;
import com.ufma.project_lp2.model.Grupo;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.Cargos;
import com.ufma.project_lp2.model.enums.StatusGrupo;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.GrupoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository repository;

    public void registrarGrupo(Grupo grupo) {
        if (grupo != null) {
            repository.save(grupo);
            System.out.println("O grupo '" + grupo.getNome() + "' foi registrado no sistema.");
        } else {
            System.out.println("Tentativa de registrar grupo inválido.");
        }
    }

    public void aprovarGrupo(Grupo grupo) {
        if (grupo != null) {
            grupo.alterarStatus(StatusGrupo.ATIVO);
            repository.save(grupo);
            System.out.println("O grupo '" + grupo.getNome() + "' foi formalmente APROVADO.");
        }
    }

    public void adicionarMembro(Grupo grupo, Usuario membro) {
        if (grupo != null && membro != null) {
            grupo.adicionarMembro(membro);
            repository.save(grupo);
        } else {
            System.out.println("Grupo ou Usuário inválido para adição.");
        }
    }

    public void atribuirCargo(Grupo grupo, Usuario discente, Cargos cargo, Docente solicitante) {
        if (grupo == null || discente == null || solicitante == null) {
            System.out.println("Dados inválidos para alteração de cargo.");
            return;
        }
        
        if (!grupo.getResponsavel().equals(solicitante)) {
            System.out.println("Apenas o Docente responsável ('" + grupo.getResponsavel().getNome() + "') pode alterar os cargos deste grupo.");
            return;
        }

        grupo.atribuirCargo(discente, cargo);
        repository.save(grupo);
    }

    public List<Grupo> listarGruposAtivos() {
        System.out.println("CATÁLOGO DE GRUPOS ATIVOS:");
        List<Grupo> ativos = new ArrayList<>();
        
        for (Grupo g : repository.findAll()) {
            if (g.getStatus() == StatusGrupo.ATIVO) {
                ativos.add(g);
                System.out.println("- Grupo: " + g.getNome() + " | Área: " + g.getTipo() + " | Professor Chefe: " + g.getResponsavel().getNome());
            }
        }
        return ativos;
    }

    public void listarTodosOsGrupos() {
        System.out.println("LISTA DE TODOS OS GRUPOS (Incluindo Inativos):");
        for (Grupo g : repository.findAll()) {
            System.out.println("Grupo: " + g.getNome() + " | Status: " + g.getStatus());
        }
    }

    public void solicitarCriacaoDeNovoGrupo(DiscenteDiretor alunoDiretor, Grupo novoGrupo) {
        System.out.println("O aluno Diretor '" + alunoDiretor.getNome() + "' solicitou a criação do grupo '" + novoGrupo.getNome() + "'. Encaminhado para aprovação do coordenador.");

    }
    public void listarUsuariosdeUmGrupo(String listGrupo){
        for(Grupo g: repository.findAll()) {
            if(g.getNome().equalsIgnoreCase(listGrupo)){
                g.listarMembros();
                return;
            }
        }
        System.out.println("Grupo nao encontrado");
    }

    public Grupo buscarGrupoPorNome(String nome){
        for(Grupo g: repository.findAll()){
            if(g.getNome().equalsIgnoreCase(nome)){
                return g;
            }
        }
        return null;
    }
}
