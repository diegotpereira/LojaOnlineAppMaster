package br.java.lojaonlineappmaster.model;

public class HorizontalProdutoModel {

    private String produtoImagem;
    private String produtoTitulo;
    private String produtoPreco;
    private boolean verificado;
    private String DataVencimento;

    public HorizontalProdutoModel() {
    }

    public HorizontalProdutoModel(String produtoImagem, String produtoTitulo, String produtoPreco,
                                  boolean verificado, String dataVencimento) {
        this.produtoImagem = produtoImagem;
        this.produtoTitulo = produtoTitulo;
        this.produtoPreco = produtoPreco;
        this.verificado = verificado;
        DataVencimento = dataVencimento;
    }

    public String getProdutoImagem() {
        return produtoImagem;
    }

    public void setProdutoImagem(String produtoImagem) {
        this.produtoImagem = produtoImagem;
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

    public String getDataVencimento() {
        return DataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        DataVencimento = dataVencimento;
    }
}
