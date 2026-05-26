import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class LeitorEpisodios {

    // Repara que recebe o caminho E o objeto hospital
    public static void lerEpisodios(String caminho, Hospital hospital, PrintWriter log) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            Scanner leitor = new Scanner(caminho);

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
                    if (dados.length < 4) {
                        String erro = "LOG ERRO: Colunas insuficientes na linha " + numLinha + ": " + linha;
                        System.out.println(erro);
                        log.println(LocalDate.now() + " | " + erro);
                        continue;
                    }

                    try {
                        String idEnf = dados[0].trim();
                        int idCama = Integer.parseInt(dados[1].trim());
                        LocalDate dataAdm = LocalDate.parse(dados[2].trim(), formato);

                        // Trata o campo vazio caso o paciente ainda esteja internado (sem data de alta)
                        LocalDate dataAlta = dados[3].trim().isEmpty() ? null : LocalDate.parse(dados[3].trim(), formato);

                        // Procura a enfermaria correspondente dentro do hospital
                        Enfermaria alvo = null;
                        for (Enfermaria e : hospital.getEnfermarias()) {
                            if (e.getCodigo().equalsIgnoreCase(idEnf)) {
                                alvo = e;
                                break;
                            }
                        }

                        // Se encontrar a enfermaria, tenta instanciar e adicionar o episódio
                        if (alvo != null) {
                            try {
                                alvo.getEpisodios().add(new Episodio(idCama, dataAdm, dataAlta));
                            } catch (IllegalArgumentException e) {
                                // Captura validações de negócio do construtor de Episodio (ex: alta anterior à admissão)
                                String erro = "LOG ERRO (Linha " + numLinha + "): " + e.getMessage();
                                System.out.println(erro);
                                log.println(LocalDate.now() + " | " + erro);
                            }
                        } else {
                            String erro = "LOG ERRO (Linha " + numLinha + "): Enfermaria " + idEnf + " não encontrada no sistema.";
                            System.out.println(erro);
                            log.println(LocalDate.now() + " | " + erro);
                        }

                    } catch (NumberFormatException e) {
                        String erro = "LOG ERRO: Identificador de cama inválido na linha " + numLinha + ".";
                        System.out.println(erro);
                        log.println(LocalDate.now() + " | " + erro);
                    } catch (DateTimeParseException e) {
                        String erro = "LOG ERRO: Formato de data incorreto na linha " + numLinha + ". Use dd/MM/yyyy.";
                        System.out.println(erro);
                        log.println(LocalDate.now() + " | " + erro);
                    }
                }
            }
            leitor.close();

        } catch (FileNotFoundException e) {
            String erro = "ERRO CRÍTICO: O ficheiro de episódios (" + caminho + ") não foi encontrado.";
            System.out.println(erro);
            log.println(LocalDate.now() + " | " + erro);
        }

    }
}