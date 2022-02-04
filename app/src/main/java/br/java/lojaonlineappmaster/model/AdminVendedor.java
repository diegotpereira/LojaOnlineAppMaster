package br.java.lojaonlineappmaster.model;

public class AdminVendedor {

    private String nome;
    private String categoria;
    private String dataaVencimento;
    private String imagem;
    private String preco;
    private String quantidade;

    public AdminVendedor(String nome, String categoria, String dataaVencimento, String imagem, String preco, String quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.dataaVencimento = dataaVencimento;
        this.imagem = imagem;
        this.preco = preco;
        this.quantidade = quantidade;
    }
}
