package br.java.lojaonlineappmaster.model;

public class Produto {

    public String quantidade;
    public String preco;
    public String vencimento;
    public String imagem;

    public Produto(String quantidade, String preco,
                   String vencimento, String imagem) {
        this.quantidade = quantidade;
        this.preco = preco;
        this.vencimento = vencimento;
        this.imagem = imagem;
    }
}
