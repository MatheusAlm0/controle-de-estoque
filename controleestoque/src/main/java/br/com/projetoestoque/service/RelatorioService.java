package br.com.projetoestoque.service;

import br.com.projetoestoque.model.Produto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RelatorioService {

    public static void exportarParaCSV(List<Produto> produtos, File arquivo) throws IOException {
        try (FileWriter writer = new FileWriter(arquivo)) {
            writer.write("ID,Nome,Categoria,Quantidade,Preço\n");
            for (Produto p : produtos) {
                writer.write(p.getId() + "," + p.getNome() + "," + p.getCategoria() + "," + p.getQuantidade() + "," + p.getPreco() + "\n");
            }
        }
    }

    public static void exportarParaTXT(List<Produto> produtos, File arquivo) throws IOException {
        try (FileWriter writer = new FileWriter(arquivo)) {
            for (Produto p : produtos) {
                writer.write("ID: " + p.getId() + "\nNome: " + p.getNome() + "\nCategoria: " + p.getCategoria() +
                        "\nQuantidade: " + p.getQuantidade() + "\nPreço: " + p.getPreco() + "\n\n");
            }
        }
    }
} 
