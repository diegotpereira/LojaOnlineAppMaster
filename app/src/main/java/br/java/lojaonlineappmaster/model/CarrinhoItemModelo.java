package br.java.lojaonlineappmaster.model;

public class CarrinhoItemModelo {

    public static final int carrinho_item = 0;
    private int tipo;

    // Carrinho Itens
    private String produtoImagem;
    private String produtoTitulo;
    private int cupon;
    private int preco;
    private int precoReduzido;
    private int quantidade;
    private int ofertaAplicada;
    private int cuponAplicada;
    private boolean CarrinhoItemDeletado = false;

    public CarrinhoItemModelo(int tipo, String produtoImagem, String produtoTitulo, int cupon, int preco,
                              int precoReduzido, int quantidade, int ofertaAplicada,
                              int cuponAplicada) {
        this.tipo = tipo;
        this.produtoImagem = produtoImagem;
        this.cupon = cupon;
        this.preco = preco;
        this.precoReduzido = precoReduzido;
        this.quantidade = quantidade;
        this.ofertaAplicada = ofertaAplicada;
        this.cuponAplicada = cuponAplicada;
        this.produtoTitulo = produtoTitulo;
    }

    public static int getCarrinho_item() {
        return carrinho_item;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getProdutoImagem() {
        return produtoImagem;
    }

    public void setProdutoImagem(String produtoImagem) {
        this.produtoImagem = produtoImagem;
    }

    public int getCupon() {
        return cupon;
    }

    public void setCupon(int cupon) {
        this.cupon = cupon;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public int getPrecoReduzido() {
        return precoReduzido;
    }

    public void setPrecoReduzido(int precoReduzido) {
        this.precoReduzido = precoReduzido;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getOfertaAplicada() {
        return ofertaAplicada;
    }

    public void setOfertaAplicada(int ofertaAplicada) {
        this.ofertaAplicada = ofertaAplicada;
    }

    public int getCuponAplicada() {
        return cuponAplicada;
    }

    public void setCuponAplicada(int cuponAplicada) {
        this.cuponAplicada = cuponAplicada;
    }

    public String getProdutoTitulo() {
        return produtoTitulo;
    }

    public void setProdutoTitulo(String produtoTitulo) {
        this.produtoTitulo = produtoTitulo;
    }

    public boolean isCarrinhoItemDeletado() {
        return CarrinhoItemDeletado;
    }

    public void setCarrinhoItemDeletado(boolean carrinhoItemDeletado) {
        CarrinhoItemDeletado = carrinhoItemDeletado;
    }
}
