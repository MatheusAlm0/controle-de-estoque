package br.com.projetoestoque.model;

import java.time.LocalDateTime;

public class Movimentacao {
    private int id;
    private int produtoId;
    private int quantidade;
    private String tipo; // entrada ou saida
    private LocalDateTime data;

    public Movimentacao(int id, int produtoId, int quantidade, String tipo, LocalDateTime data) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getData() {
        return data;
    }
} 
