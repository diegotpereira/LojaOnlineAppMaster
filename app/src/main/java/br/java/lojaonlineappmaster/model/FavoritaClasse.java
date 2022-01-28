package br.java.lojaonlineappmaster.model;

public class FavoritaClasse {

    private String produtoTitulo;
    private String produtoPreco;
    private boolean verificado;

    public FavoritaClasse() {
    }

    public FavoritaClasse(String produtoTitulo, String produtoPreco, boolean verificado) {
        this.produtoTitulo = produtoTitulo;
        this.produtoPreco = produtoPreco;
        this.verificado = verificado;
    }

    public String getProdutoTitulo() {
        return produtoTitulo;
    }

    public void setProdutoTitulo(String produtoTitulo) {
        this.produtoTitulo = produtoTitulo;
    }

    public String getProdutoPreco() {
        return produtoPreco;
    }

    public void setProdutoPreco(String produtoPreco) {
        this.produtoPreco = produtoPreco;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
}
