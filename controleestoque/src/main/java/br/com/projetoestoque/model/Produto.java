package br.com.projetoestoque.model;

public class Produto {
    private int id;
    private String nome;
    private String categoria;
    private double preco;

    // 🔹 Construtor vazio
    public Produto() {
    }

    // 🔹 Construtor completo (opcional)
    public Produto(int id, String nome, String categoria, double preco) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
    }

    // 🔹 Getters e Setters
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
