package com.example.demo.Estoque;

import com.example.demo.Lubrificantes.Lubrificante;

import java.util.List;

public class LeitorExcel {

    private String descricao;

    private static Lubrificante lubrificante = new Lubrificante();

    static List<Lubrificante> listaLubrificantes = lubrificante.recuperarTodos();


    public LeitorExcel(String descricao) {
        this.descricao = descricao;
    }

    public static void main(String[] args) {

        // Verificação se a lista foi preenchida corretamente
        if (listaLubrificantes != null) {
            for (Lubrificante lubrificante : listaLubrificantes) {
                System.out.println("Código: " + lubrificante.getCodigo());
                System.out.println("Descrição: " + lubrificante.getDescricao());
                System.out.println("------------------------------");
            }
        } else {
            System.out.println("Erro ao ler o arquivo.");
        }
    }

//    public static List<Lubrificante> lerArquivoExcel() {
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Lubrificantes");
//        TableView<Lubrificante> table = new TableView<>();
//        TableColumn<Lubrificante, String> codigoColumn = new TableColumn<>("Código");
//        codigoColumn.setCellValueFactory(cellData -> {
//            String codigo = String.valueOf(cellData.getValue().getCodigo());
//            return new SimpleStringProperty(codigo);
//        });
//        TableColumn<Lubrificante, String> descricaoColumn = new TableColumn<>("Descrição");
//        descricaoColumn.setCellValueFactory(cellData -> {
//            String codigo = cellData.getValue().getDescricao();
//            return new SimpleStringProperty(codigo);
//        });
//        table.getColumns().addAll(codigoColumn, descricaoColumn);
//
//        int rowNum = 0;
//        for (Lubrificante l : listaLubrificantes) {
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(l.getCodigo());
//            row.createCell(1).setCellValue(l.getDescricao());
//        }
//
//
//        return listaLubrificantes;
//    }

    public static List<Lubrificante> getListaLubrificantes() {
        return listaLubrificantes;
    }
}
