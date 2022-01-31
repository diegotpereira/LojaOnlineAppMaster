package br.java.lojaonlineappmaster.model;

public class Modelo {

    private String titulo;
    private String imagem;
    private String desc;

    public Modelo(String titulo, String imagem, String desc) {
        this.titulo = titulo;
        this.imagem = imagem;
        this.desc = desc;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
