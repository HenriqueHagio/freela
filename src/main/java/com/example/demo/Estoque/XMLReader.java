package com.example.demo.Estoque;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLReader {

    public static List<ItemEstoque> lerArquivoXML(String caminhoArquivo) {
        List<ItemEstoque> itens = new ArrayList<>();

        try {
            File arquivo = new File(caminhoArquivo);
            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder construtor = fabrica.newDocumentBuilder();
            Document documento = construtor.parse(arquivo);

            documento.getDocumentElement().normalize();

            NodeList listaNos = documento.getElementsByTagName("det");

            for (int i = 0; i < listaNos.getLength(); i++) {
                Node no = listaNos.item(i);

                if (no.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) no;

                    String nome = elemento.getElementsByTagName("xProd").item(0).getTextContent();
                    double quantidade = Double.parseDouble(elemento.getElementsByTagName("qCom").item(0).getTextContent());
                    String unidade = elemento.getElementsByTagName("uCom").item(0).getTextContent();

                    ItemEstoque item = new ItemEstoque(nome, quantidade, unidade);
                    itens.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itens;
    }
}
