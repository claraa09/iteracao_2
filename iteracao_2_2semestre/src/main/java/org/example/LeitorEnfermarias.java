package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LeitorEnfermarias {

    public static List<Enfermaria> lerEnfermarias(String caminho, PrintWriter log) {
        List<Enfermaria> lista = new ArrayList<>();

        try (Scanner leitor = new Scanner(new File(caminho))) {

            if (leitor.hasNextLine()) {
                leitor.nextLine(); // Salta o cabeçalho
            }

            int numLinha = 1;
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                numLinha++;

                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";", -1);

                    // Previne erros se a linha estiver cortada
                    if (dados.length < 3) continue;

                    String codigo = dados[0].trim();
                    String tipo = dados[1].trim();
                    String capTxt = dados[2].trim();

                    // 1. VALIDAÇÃO DA CAPACIDADE
                    if (!Validador.isInteiro(capTxt)) {
                        String erro = "LOG ERRO (Linha " + numLinha + "): Capacidade inválida -> " + capTxt;
                        System.out.println(erro);
                        if (log != null) log.println(LocalDate.now() + " | " + erro);
                        continue; // Salta esta linha
                    }

                    // Se passou no validador, podemos converter sem medo!
                    int capacidade = Integer.parseInt(capTxt);

                    // 2. CRIAÇÃO DAS ENFERMARIAS
                    if (tipo.equalsIgnoreCase("GERAL")) {
                        String acompTxt = dados[4].trim();
                        int numAcompanhantes = 0;

                        // Valida os acompanhantes se a coluna não estiver vazia
                        if (!acompTxt.isEmpty()) {
                            if (Validador.isInteiro(acompTxt)) {
                                numAcompanhantes = Integer.parseInt(acompTxt);
                            } else {
                                String erro = "LOG ERRO (Linha " + numLinha + "): Nº Acompanhantes inválido -> " + acompTxt;
                                System.out.println(erro);
                                if (log != null) log.println(LocalDate.now() + " | " + erro);
                                continue;
                            }
                        }

                        List<String> recursosDisponiveis = Arrays.asList(dados[8].trim().split(",\\s*"));
                        lista.add(new EnfermariaGeral(codigo, capacidade, numAcompanhantes, recursosDisponiveis));

                    } else if (tipo.equalsIgnoreCase("PSIQ")) {
                        String horarioVisitas = dados[3].trim();
                        String nivelTxt = dados[5].trim();
                        int nivelSeguranca = nivelTxt.equalsIgnoreCase("Alto") ? 3 : (nivelTxt.equalsIgnoreCase("Médio") ? 2 : 1);
                        lista.add(new EnfermariaPsiquiatrica(codigo, capacidade, horarioVisitas, nivelSeguranca));

                    } else if (tipo.equalsIgnoreCase("UCI")) {
                        String horarioVisitas = dados[3].trim();
                        String pAtmTxt = dados[6].trim();
                        String pRefTxt = dados[7].trim();

                        // Valida a pressão atmosférica usando a função para decimais
                        double pressaoAtm = 0.0;
                        if (!pAtmTxt.isEmpty() && Validador.isDecimal(pAtmTxt)) {
                            pressaoAtm = Double.parseDouble(pAtmTxt);
                        }

                        double pressaoAtmRef = 0.0;
                        if (!pRefTxt.isEmpty() && Validador.isDecimal(pRefTxt)) {
                            pressaoAtmRef = Double.parseDouble(pRefTxt);
                        }

                        lista.add(new EnfermariaCI(codigo, capacidade, horarioVisitas, pressaoAtm, pressaoAtmRef));

                    } else {
                        String erro = "LOG ERRO (Linha " + numLinha + "): Tipo de enfermaria inválido (" + tipo + ")";
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

        return lista;
    }
}