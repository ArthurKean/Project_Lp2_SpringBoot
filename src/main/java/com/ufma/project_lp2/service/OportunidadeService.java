package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.Papel;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import com.ufma.project_lp2.model.enums.StatusOportunidade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import com.ufma.project_lp2.repository.OportunidadeRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class OportunidadeService {

    @Autowired
    private OportunidadeRepository repository;

    public Oportunidade registrarOportunidade(Oportunidade oportunidade) {
        if (oportunidade != null) {
            Oportunidade salva = repository.save(oportunidade);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi criada e salva!");
            return salva;
        } else {
            System.out.println("Tentativa de registrar uma oportunidade inválida!");
            return null;
        }
    }

    public List<Oportunidade> listarOportunidades() {
        System.out.println("CATÁLOGO GERAL DE OPORTUNIDADES:");
        List<Oportunidade> todas = repository.findAll();
        for (Oportunidade op : todas) {
            System.out.println("Projeto: " + op.getTitulo() + " | Status: " + op.getStatus() + " | Vagas: " + op.getVagas());
        }
        return todas;
    }

    public List<Oportunidade> listarOportunidadesAbertas() {
        System.out.println("OPORTUNIDADES DISPONÍVEIS PARA INSCRIÇÃO:");
        List<Oportunidade> abertas = repository.findByStatusIn(
                Arrays.asList(StatusOportunidade.PUBLICADA, StatusOportunidade.ABERTA));
        for (Oportunidade op : abertas) {
            System.out.println("- Projeto: " + op.getTitulo() + " | Vagas Restantes: " + op.getVagas());
        }
        return abertas;
    }

    public void divulgarOportunidade(Oportunidade oportunidade, Usuario autorRequisitante) {
        if (autorRequisitante.getPapel() == Papel.COORDENADOR || autorRequisitante.getPapel() == Papel.ADMINISTRADOR) {
            oportunidade.setStatus(StatusOportunidade.PUBLICADA);
            repository.save(oportunidade);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi PUBLICADA diretamente");
        } else {
            oportunidade.setStatus(StatusOportunidade.AGUARDANDO_APROVACAO);
            repository.save(oportunidade);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi enviada e aguarda aprovação");
        }
    }

    public void aprovarOportunidade(Oportunidade oportunidade, Usuario avaliador) {
        if (avaliador.getPapel() == Papel.DOCENTE || avaliador.getPapel() == Papel.COORDENADOR) {
            if (oportunidade.getStatus() == StatusOportunidade.AGUARDANDO_APROVACAO) {
                oportunidade.setStatus(StatusOportunidade.PUBLICADA);
                repository.save(oportunidade);
                System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi APROVADA por " + avaliador.getNome());
            } else {
                System.out.println("Esta oportunidade não estava aguardando aprovação");
            }
        } else {
            System.out.println("Apenas Docentes ou Coordenadores podem aprovar oportunidades");
        }
    }

    public void encerrarOportunidade(Oportunidade oportunidade) {
        if (oportunidade != null) {
            oportunidade.encerrar(); // Chamando a lógica da própria classe Oportunidade
            repository.save(oportunidade);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi ENCERRADA com sucesso");
        }
    }

    public void inscreverDiscente(Discente discente, Oportunidade oportunidade) {
        if (oportunidade.getStatus() == StatusOportunidade.PUBLICADA || oportunidade.getStatus() == StatusOportunidade.ABERTA) {
            if (oportunidade.temVagasDisponiveis()) {
                Inscricao inscricao = new Inscricao(oportunidade, discente, "Inscrição via sistema interativo");
                oportunidade.getInscricoes().add(inscricao);
                repository.save(oportunidade);
                System.out.println("O sistema registrou a inscrição do aluno '" + discente.getNome() + "' no projeto '" + oportunidade.getTitulo() + "'.");
            } else {
                System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' não possui vagas disponíveis");
            }
        } else {
            System.out.println("A oportunidade precisa estar PUBLICADA ou ABERTA");
        }
    }

    public void abandonarOportunidade(Discente discente, Oportunidade oportunidade) {
        Inscricao inscricaoRemover = null;
        for (Inscricao inscricao : oportunidade.getInscricoes()) {
            if (inscricao.getDiscente().equals(discente)) {
                inscricaoRemover = inscricao;
                break;
            }
        }
        if (inscricaoRemover != null) {
            inscricaoRemover.setStatus(StatusInscricao.CANCELADO);
            repository.save(oportunidade);
            System.out.println("O estudante '" + discente.getNome() + "' abandonou a oportunidade '" + oportunidade.getTitulo() + "'. O status da inscrição mudou para CANCELADO.");
        } else {
            System.out.println("O estudante '" + discente.getNome() + "' não estava inscrito nesta oportunidade.");
        }
    }

    public Oportunidade buscarPorTitulo(String titulo) {
        return repository.findByTituloIgnoreCase(titulo).orElse(null);
    }

    public Oportunidade buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}

