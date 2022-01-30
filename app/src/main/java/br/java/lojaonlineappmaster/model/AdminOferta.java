package br.java.lojaonlineappmaster.model;

public class AdminOferta {

    private String OfertaNome;
    private String OfertaDescricao;
    private String OfertaImg;

    public AdminOferta() {
    }

    public AdminOferta(String ofertaNome, String ofertaDescricao, String ofertaImg) {
        OfertaNome = ofertaNome;
        OfertaDescricao = ofertaDescricao;
        OfertaImg = ofertaImg;
    }

    public String getOfertaNome() {
        return OfertaNome;
    }

    public void setOfertaNome(String ofertaNome) {
        OfertaNome = ofertaNome;
    }

    public String getOfertaDescricao() {
        return OfertaDescricao;
    }

    public void setOfertaDescricao(String ofertaDescricao) {
        OfertaDescricao = ofertaDescricao;
    }

    public String getOfertaImg() {
        return OfertaImg;
    }

    public void setOfertaImg(String ofertaImg) {
        OfertaImg = ofertaImg;
    }
}
