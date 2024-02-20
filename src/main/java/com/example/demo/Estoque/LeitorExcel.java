package com.example.demo.Estoque;

import com.example.demo.Lubrificantes.Lubrificante;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeitorExcel {

    private String descricao;

    public LeitorExcel(String descricao) {
        this.descricao = descricao;
    }

    public static void main(String[] args) {
        String filePath = "D:\\freela\\src\\main\\resources\\com\\example\\demo\\comeco\\BD_PRODUTOS_LUBVEL.xlsx";
        List<Lubrificante> listaLubrificantes = lerArquivoExcel(filePath);

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

    public static List<Lubrificante> lerArquivoExcel(String filePath) {
        List<Lubrificante> listaLubrificantes = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0); // Obtém a primeira planilha

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Ignora cabeçalho, se existir

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell codigoCell = row.getCell(0);
                String codigo;
                if (codigoCell.getCellType() == CellType.NUMERIC) {
                    codigo = String.valueOf((int) codigoCell.getNumericCellValue());
                } else {
                    codigo = codigoCell.getStringCellValue();
                }

                Cell descricaoCell = row.getCell(1);
                String descricao;
                if (descricaoCell.getCellType() == CellType.NUMERIC) {
                    descricao = String.valueOf((int) descricaoCell.getNumericCellValue());
                } else {
                    descricao = descricaoCell.getStringCellValue();
                }

                Lubrificante lubrificante = new Lubrificante(codigo, descricao);
                listaLubrificantes.add(lubrificante);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Retorna null em caso de erro na leitura do arquivo
        }

        return listaLubrificantes;
    }

    public static List<Lubrificante> getListaLubrificantes() {
        String filePath = "C:\\Users\\Lucas\\OneDrive\\Documentos\\BD_PRODUTOS_LUBVEL.xlsx";
        return LeitorExcel.lerArquivoExcel(filePath);
    }
}
