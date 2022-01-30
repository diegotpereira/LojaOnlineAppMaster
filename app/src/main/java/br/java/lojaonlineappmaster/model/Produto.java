package br.java.lojaonlineappmaster.model;

public class Produto {

    public String quantidade;
    public String preco;
    public String dataVencimento;
    public String imagem;

    public Produto(String quantidade, String preco,
                   String dataVencimento, String imagem) {
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataVencimento = dataVencimento;
        this.imagem = imagem;
    }
}
