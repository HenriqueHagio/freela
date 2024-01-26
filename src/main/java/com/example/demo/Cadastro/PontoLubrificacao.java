package com.example.demo.Cadastro;

public class PontoLubrificacao {
    private String setor;
    private String equipamento;
    private String tagEquipamento;
    private String ponto;
    private String lubrificante;

    public PontoLubrificacao() {
        // Se precisar de inicializações no construtor, adicione aqui
    }

    // Getters e Setters para os campos

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getTagEquipamento() {
        return tagEquipamento;
    }

    public void setTagEquipamento(String tagEquipamento) {
        this.tagEquipamento = tagEquipamento;
    }

    public String getPonto() {
        return ponto;
    }

    public void setPonto(String ponto) {
        this.ponto = ponto;
    }

    public String getLubrificante() {
        return lubrificante;
    }

    public void setLubrificante(String lubrificante) {
        this.lubrificante = lubrificante;
    }
}
