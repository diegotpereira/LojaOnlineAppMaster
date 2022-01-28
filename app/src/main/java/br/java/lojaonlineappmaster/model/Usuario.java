package br.java.lojaonlineappmaster.model;

public class Usuario {

    private String expirado;
    private String imagem;
    private String preco;
    private String quantidade;
    private String categoria;

    public Usuario() {
    }

    public Usuario(String expirado, String imagem, String preco, String quantidade, String categoria) {
        this.expirado = expirado;
        this.imagem = imagem;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoria = categoria;
    }

    public String getExpirado() {
        return expirado;
    }

    public void setExpirado(String expirado) {
        this.expirado = expirado;
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
