package com.ufma.project_lp2.service;


import com.ufma.project_lp2.model.Discente;
import com.ufma.project_lp2.model.Inscricao;
import com.ufma.project_lp2.model.Oportunidade;
import com.ufma.project_lp2.model.Usuario;
import com.ufma.project_lp2.model.enums.Papel;
import com.ufma.project_lp2.model.enums.StatusInscricao;
import com.ufma.project_lp2.model.enums.StatusOportunidade;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OportunidadeService {

    private List<Oportunidade> bancoDeOportunidades;

    public OportunidadeService() {
        this.bancoDeOportunidades = new ArrayList<>();
    }

    public void registrarOportunidade(Oportunidade oportunidade) {
        if (oportunidade != null) {
            bancoDeOportunidades.add(oportunidade);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi criada e salva!");
        } else {
            System.out.println("Tentativa de registrar uma oportunidade inválida!");
        }
    }

    public List<Oportunidade> listarOportunidades() {
        System.out.println("CATÁLOGO GERAL DE OPORTUNIDADES:");
        for (Oportunidade op : bancoDeOportunidades) {
            System.out.println("Projeto: " + op.getTitulo() + " | Status: " + op.getStatus() + " | Vagas: " + op.getVagas());
        }
        return bancoDeOportunidades;
    }

    public List<Oportunidade> listarOportunidadesAbertas() {
        System.out.println("OPORTUNIDADES DISPONÍVEIS PARA INSCRIÇÃO:");
        List<Oportunidade> abertas = new ArrayList<>();
        for (Oportunidade op : bancoDeOportunidades) {
            if (op.getStatus() == StatusOportunidade.PUBLICADA || op.getStatus() == StatusOportunidade.ABERTA) {
                abertas.add(op);
                System.out.println("- Projeto: " + op.getTitulo() + " | Vagas Restantes: " + op.getVagas());
            }
        }
        return abertas;
    }

    public void divulgarOportunidade(Oportunidade oportunidade, Usuario autorRequisitante) {
        if (autorRequisitante.getPapel() == Papel.COORDENADOR || autorRequisitante.getPapel() == Papel.ADMINISTRADOR) {
            oportunidade.setStatus(StatusOportunidade.PUBLICADA);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi PUBLICADA diretamente");
        } else {
            oportunidade.setStatus(StatusOportunidade.AGUARDANDO_APROVACAO);
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi enviada e aguarda aprovação");
        }
    }

    public void aprovarOportunidade(Oportunidade oportunidade, Usuario avaliador) {
        if (avaliador.getPapel() == Papel.DOCENTE || avaliador.getPapel() == Papel.COORDENADOR) {
            if (oportunidade.getStatus() == StatusOportunidade.AGUARDANDO_APROVACAO) {
                oportunidade.setStatus(StatusOportunidade.PUBLICADA);
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
            System.out.println("A oportunidade '" + oportunidade.getTitulo() + "' foi ENCERRADA com sucesso");
        }
    }

    public void inscreverDiscente(Discente discente, Oportunidade oportunidade) {
        if (oportunidade.getStatus() == StatusOportunidade.PUBLICADA || oportunidade.getStatus() == StatusOportunidade.ABERTA) {
            if (oportunidade.temVagasDisponiveis()) {
                Inscricao inscricao = new Inscricao(oportunidade, discente, "Inscrição via sistema interativo");
                oportunidade.getInscricoes().add(inscricao);
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
            System.out.println("O estudante '" + discente.getNome() + "' abandonou a oportunidade '" + oportunidade.getTitulo() + "'. O status da inscrição mudou para CANCELADO.");
        } else {
            System.out.println("O estudante '" + discente.getNome() + "' não estava inscrito nesta oportunidade.");
        }
    }

    public Oportunidade buscarPorTitulo(String titulo) {
        for (Oportunidade op : bancoDeOportunidades) {
            if (op.getTitulo().equalsIgnoreCase(titulo)) {
                return op;
            }
        }
        return null;
    }
}
