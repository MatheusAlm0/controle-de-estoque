package br.com.projetoestoque.model;

public class Produto {
    private String id;
    private String codigo;
    private String marca;
    private String modelo;
    private String categoria;
    private double quantidade;
    private double preco;

    public Produto(String id, String codigo, String marca, String modelo, String categoria, double quantidade, double preco) {
        this.id = id;
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // Construtor vazio para uso em ProdutoDAO
    public Produto() {}

    // Getters e setters para os campos
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getNome() {
        return marca + " " + modelo;
    }

    public void setId(int int1) {
        this.id = String.valueOf(int1);
    }

}