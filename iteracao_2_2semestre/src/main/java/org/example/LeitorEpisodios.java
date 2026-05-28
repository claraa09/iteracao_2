package org.example;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class LeitorEpisodios {

    // Repara que recebe o caminho E o objeto hospital
    public static void lerEpisodios(String caminho, Hospital hospital, PrintWriter log) throws FileNotFoundException {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            Scanner leitor = new Scanner(new File(caminho));

            if (leitor.hasNextLine()) {
                leitor.nextLine();
            }

            int numLinha = 1;
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                numLinha++;

                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";", -1);

                    // Validação estrutural básica da linha
                    if (dados.length < 4) continue;

                    String codigo = dados[0].trim();
                    String idCamaTxt = dados[1].trim();
                    String dataAdmTxt = dados[2].trim();
                    String dataAltaTxt = dados[3].trim();

                    // 1. VALIDAÇÕES: Usar a nossa nova classe
                    if (!Validador.isInteiro(idCamaTxt)) {
                        System.err.println("Erro na linha " + numLinha + ": ID da Cama não é um número.");
                        continue;
                    }
                    if (!Validador.isData(dataAdmTxt)) {
                        System.err.println("Erro na linha " + numLinha + ": Data de Admissão inválida.");
                        continue;
                    }
                    if (!dataAltaTxt.isEmpty() && !Validador.isData(dataAltaTxt)) {
                        System.err.println("Erro na linha " + numLinha + ": Data de Alta inválida.");
                        continue;
                    }

                    // 2. CONVERSÃO SEGURA (Nenhum try-catch de formatação é necessário aqui agora!)
                    int idCama = Integer.parseInt(idCamaTxt);
                    LocalDate dataAd = LocalDate.parse(dataAdmTxt, Validador.FORMATO);

                    LocalDate dataAlta = null;
                    if (!dataAltaTxt.isEmpty()) {
                        dataAlta = LocalDate.parse(dataAltaTxt, Validador.FORMATO);
                    }

                    // 3. Lógica normal de procurar no hospital e adicionar
                    Enfermaria alvo = hospital.pesquisarEnfermaria(codigo);
                    if (alvo != null) {
                        try {
                            alvo.getEpisodios().add(new Episodio(idCama, dataAd, dataAlta));
                        } catch (IllegalArgumentException e) {
                            String erro = "LOG ERRO (Linha " + numLinha + "): " + e.getMessage();
                            System.out.println(erro);
                            if (log != null) log.println(LocalDate.now() + " | " + erro);
                        }
                    } else {
                        String erro = "LOG ERRO (Linha " + numLinha + "): Enfermaria " + codigo + " não encontrada.";
                        System.out.println(erro);
                        if (log != null) log.println(LocalDate.now() + " | " + erro);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            String erro = "ERRO CRÍTICO: O ficheiro " + caminho + " não foi encontrado.";
            System.out.println(erro);
            if (log != null) log.println(LocalDate.now() + " | " + erro);
        }

    }
}
