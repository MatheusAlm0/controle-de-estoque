package br.com.projetoestoque.model;

public class Usuario {
    private int id;
    private String nome;
    private String nivelAcesso;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String nivelAcesso, String senha) {
        this.nome = nome;
        this.nivelAcesso = nivelAcesso;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}