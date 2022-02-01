package br.java.lojaonlineappmaster.model;

public class MeuPedidoModelo {
    private String PedidoID;
    private String Data;
    private String pedidoNums;
    private String pedidoPreco;
    private String produtosPedido;
    private String verificarPedido;

    public MeuPedidoModelo(String pedidoID, String data, String pedidoNums, String pedidoPreco, String produtosPedido, String verificarPedido) {
        PedidoID = pedidoID;
        Data = data;
        this.pedidoNums = pedidoNums;
        this.pedidoPreco = pedidoPreco;
        this.produtosPedido = produtosPedido;
        this.verificarPedido = verificarPedido;
    }

    public String getPedidoID() {
        return PedidoID;
    }

    public void setPedidoID(String pedidoID) {
        PedidoID = pedidoID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getPedidoNums() {
        return pedidoNums;
    }

    public void setPedidoNums(String pedidoNums) {
        this.pedidoNums = pedidoNums;
    }

    public String getPedidoPreco() {
        return pedidoPreco;
    }

    public void setPedidoPreco(String pedidoPreco) {
        this.pedidoPreco = pedidoPreco;
    }

    public String getProdutosPedido() {
        return produtosPedido;
    }

    public void setProdutosPedido(String produtosPedido) {
        this.produtosPedido = produtosPedido;
    }

    public String getVerificarPedido() {
        return verificarPedido;
    }

    public void setVerificarPedido(String verificarPedido) {
        this.verificarPedido = verificarPedido;
    }
}
