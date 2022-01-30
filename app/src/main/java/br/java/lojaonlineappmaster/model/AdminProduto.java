package br.java.lojaonlineappmaster.model;

public class AdminProduto {

    private String nome;
    private String categoria;
    private String dataVencimento;
    private String imagem;
    private String preco;
    private String quantidade;

    public AdminProduto() {
    }

    public AdminProduto(String nome, String categoria, String dataVencimento, String imagem,
                        String preco, String quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.dataVencimento = dataVencimento;
        this.imagem = imagem;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
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
}
