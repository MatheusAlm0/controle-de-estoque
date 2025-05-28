package br.com.projetoestoque.service;

import br.com.projetoestoque.model.Notificacao;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoService {
    private static NotificacaoService instance;
    private final List<Notificacao> notificacoes = new ArrayList<>();

    private NotificacaoService() {}

    public static NotificacaoService getInstance() {
        if (instance == null) instance = new NotificacaoService();
        return instance;
    }

    public void adicionar(String mensagem) {
        notificacoes.add(new Notificacao(mensagem));
    }

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }

    public void remover(Notificacao n) {
        notificacoes.remove(n);
    }
}
