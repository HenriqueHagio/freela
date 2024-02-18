package com.example.demo.Estoque;

public class ItemProduto {
    private String codigoProduto;
    private String eanProduto;
    private String nomeProduto;
    private String ncmProduto;
    private String cestProduto;
    private String cfopProduto;
    private String unidadeComercial;
    private double quantidadeComercial;
    private double valorUnitarioComercial;
    private Integer quantidade;
    private String unidade;

    public ItemProduto(String codigoProduto, String eanProduto, String nomeProduto, String ncmProduto,
                       String cestProduto, String cfopProduto, String unidadeComercial,
                       double quantidadeComercial, double valorUnitarioComercial, Integer quantidade, String unidade) {
        this.codigoProduto = codigoProduto;
        this.eanProduto = eanProduto;
        this.nomeProduto = nomeProduto;
        this.ncmProduto = ncmProduto;
        this.cestProduto = cestProduto;
        this.cfopProduto = cfopProduto;
        this.unidadeComercial = unidadeComercial;
        this.quantidadeComercial = quantidadeComercial;
        this.valorUnitarioComercial = valorUnitarioComercial;
        this.quantidade = quantidade;
        this.unidade = unidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}
