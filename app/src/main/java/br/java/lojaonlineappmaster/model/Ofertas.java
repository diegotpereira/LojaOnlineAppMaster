package br.java.lojaonlineappmaster.model;

public class Ofertas {

    String titulo;
    String descricao;
    String img;

    public Ofertas() {
    }

    public Ofertas(String descricao, String img) {
        this.descricao = descricao;
        this.img = img;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
