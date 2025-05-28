package br.com.projetoestoque.model;

public class Notificacao {
    private String mensagem;
    private boolean lida;

    public Notificacao(String mensagem) {
        this.mensagem = mensagem;
        this.lida = false;
    }

    public String getMensagem() { return mensagem; }
    public boolean isLida() { return lida; }
    public void marcarComoLida() { this.lida = true; }

    @Override
    public String toString() {
        return mensagem;
    }
}
