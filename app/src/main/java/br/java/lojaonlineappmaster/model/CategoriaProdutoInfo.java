package br.java.lojaonlineappmaster.model;

public class CategoriaProdutoInfo {

    private String ProdutoImagem;
    private String ProdutoNome;
    private String ProdutoPreco;
    private String ProdutoDataVencimento;
    private boolean ehFavorito;

    public CategoriaProdutoInfo() {
    }

    public CategoriaProdutoInfo(String produtoImagem, String produtoNome, String produtoPreco,
                                String produtoDataVencimento, boolean ehFavorito) {
        ProdutoImagem = produtoImagem;
        ProdutoNome = produtoNome;
        ProdutoPreco = produtoPreco;
        ProdutoDataVencimento = produtoDataVencimento;
        this.ehFavorito = ehFavorito;
    }

    public String getProdutoImagem() {
        return ProdutoImagem;
    }

    public void setProdutoImagem(String produtoImagem) {
        ProdutoImagem = produtoImagem;
    }

    public String getProdutoNome() {
        return ProdutoNome;
    }

    public void setProdutoNome(String produtoNome) {
        ProdutoNome = produtoNome;
    }

    public String getProdutoPreco() {
        return ProdutoPreco;
    }

    public void setProdutoPreco(String produtoPreco) {
        ProdutoPreco = produtoPreco;
    }

    public String getProdutoDataVencimento() {
        return ProdutoDataVencimento;
    }

    public void setProdutoDataVencimento(String produtoDataVencimento) {
        ProdutoDataVencimento = produtoDataVencimento;
    }

    public boolean isEhFavorito() {
        return ehFavorito;
    }

    public void setEhFavorito(boolean ehFavorito) {
        this.ehFavorito = ehFavorito;
    }
}

