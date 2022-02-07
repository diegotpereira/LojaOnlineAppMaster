package br.java.lojaonlineappmaster.model;

public class AdminVendedor {

    private String nome;
    private String img;
    private String qrimg;
    private String salario;

    public AdminVendedor(String nome, String img, String qrimg, String salario) {
        this.nome = nome;
        this.img = img;
        this.qrimg = qrimg;
        this.salario = salario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQrimg() {
        return qrimg;
    }

    public void setQrimg(String qrimg) {
        this.qrimg = qrimg;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }
}
