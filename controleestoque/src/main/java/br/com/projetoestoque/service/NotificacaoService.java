package br.com.projetoestoque.service;

import br.com.projetoestoque.model.Notificacao;
import br.com.projetoestoque.dao.NotificacaoDAO;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoService {
    private static NotificacaoService instance;
    private final List<Notificacao> notificacoes = new ArrayList<>();
    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    private NotificacaoService() {}

    public static NotificacaoService getInstance() {
        if (instance == null) instance = new NotificacaoService();
        return instance;
    }

    public void adicionar(String mensagem) {
        Notificacao n = new Notificacao(mensagem);
        notificacoes.add(n);
        notificacaoDAO.salvar(n); // Salva no banco!
    }

    public void carregarDoBanco() {
        notificacoes.clear();
        notificacoes.addAll(notificacaoDAO.listarTodas());
    }

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }

    public void remover(Notificacao n) {
        notificacoes.remove(n);
        notificacaoDAO.remover(n); // Remove do banco também!
    }
}
