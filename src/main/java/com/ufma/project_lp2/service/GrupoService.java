package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.DiscenteDiretor;
import com.ufma.project_lp2.model.Docente;
import com.ufma.project_lp2.model.Grupo;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.Cargos;
import com.ufma.project_lp2.model.enums.StatusGrupo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.GrupoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class GrupoService {

    @Autowired
    private GrupoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    public Grupo registrarGrupo(Grupo grupo) {
        if (grupo != null) {
            if (grupo.getResponsavel() != null && grupo.getResponsavel().getId() != null) {
                Usuario responsavel = usuarioService.buscarPorId(grupo.getResponsavel().getId());
                if (responsavel instanceof Docente) {
                    grupo.setResponsavel((Docente) responsavel);
                }
            }
            Grupo salvo = repository.save(grupo);
            System.out.println("O grupo '" + grupo.getNome() + "' foi registrado no sistema.");
            return salvo;
        } else {
            System.out.println("Tentativa de registrar grupo inválido");
            return null;
        }
    }

    public Grupo aprovarGrupo(Grupo grupo) {
        if (grupo != null) {
            grupo.alterarStatus(StatusGrupo.ATIVO);
            Grupo salvo = repository.save(grupo);
            System.out.println("O grupo '" + grupo.getNome() + "' foi formalmente APROVADO.");
            return salvo;
        }
        return null;
    }

    public Grupo adicionarMembro(Grupo grupo, Usuario membro) {
        if (grupo != null && membro != null) {
            grupo.adicionarMembro(membro);
            return repository.save(grupo);
        } else {
            System.out.println("Grupo ou Usuário inválido para adição.");
            return null;
        }
    }

    public Grupo removerMembro(Grupo grupo, Usuario membro) {
        if (grupo != null && membro != null) {
            grupo.removerMembro(membro);
            return repository.save(grupo);
        } else {
            System.out.println("Grupo ou Usuário inválido para remoção.");
            return null;
        }
    }

    public Grupo atribuirCargo(Grupo grupo, Usuario discente, Cargos cargo, Docente solicitante) {
        if (grupo == null || discente == null || solicitante == null) {
            System.out.println("Dados inválidos para alteração de cargo.");
            return null;
        }
        
        if (!grupo.getResponsavel().equals(solicitante)) {
            System.out.println("Apenas o Docente responsável ('" + grupo.getResponsavel().getNome() + "') pode alterar os cargos deste grupo.");
            return null;
        }

        grupo.atribuirCargo(discente, cargo);
        return repository.save(grupo);
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

    public List<Grupo> listarTodosOsGrupos() {
        System.out.println("LISTA DE TODOS OS GRUPOS (Incluindo Inativos):");
        for (Grupo g : repository.findAll()) {
            System.out.println("Grupo: " + g.getNome() + " | Status: " + g.getStatus());
        }
        return repository.findAll();
    }

    public String solicitarCriacaoDeNovoGrupo(DiscenteDiretor alunoDiretor, Grupo novoGrupo) {
        String msg = "O aluno Diretor '" + alunoDiretor.getNome() + "' solicitou a criação do grupo '" + novoGrupo.getNome() + "'. Encaminhado para aprovação do coordenador.";
        System.out.println(msg);
        return msg;
    }
    public List<Usuario> listarUsuariosdeUmGrupo(String listGrupo){
        for(Grupo g: repository.findAll()) {
            if(g.getNome().equalsIgnoreCase(listGrupo)){
                g.listarMembros();
                return g.getUsuariosRegistrados();
            }
        }
        System.out.println("Grupo nao encontrado");
        return new ArrayList<>();
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
