package br.com.projetoestoque.util;

public class Sessao {
    private static Integer usuarioId;

    public static Integer getUsuarioId() {
        return usuarioId;
    }

    public static void setUsuarioId(Integer id) {
        usuarioId = id;
    }
}