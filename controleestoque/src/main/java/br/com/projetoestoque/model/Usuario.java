package br.com.projetoestoque.model;

public class Usuario {
    private int id;
    private String nome;
    private String nivelAcesso;
    private String senha;
    private String email;

    public Usuario() {}

    public Usuario(int id, String nome, String nivelAcesso, String senha, String email) {
        this.id = id;
        this.nome = nome;
        this.nivelAcesso = nivelAcesso;
        this.senha = senha;
        this.email = email;
    }

    public Usuario(String nome, String nivelAcesso, String senha, String email) {
        this.nome = nome;
        this.nivelAcesso = nivelAcesso;
        this.senha = senha;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}