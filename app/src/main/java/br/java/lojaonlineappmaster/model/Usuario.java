package br.java.lojaonlineappmaster.model;

public class Usuario {

    private String dataVencimento;
    private String imagem;
    private String preco;
    private String quantidade;
    private String categoria;

    public Usuario() {
    }

    public Usuario(String dataVencimento, String imagem, String preco, String quantidade, String categoria) {
        this.dataVencimento = dataVencimento;
        this.imagem = imagem;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
