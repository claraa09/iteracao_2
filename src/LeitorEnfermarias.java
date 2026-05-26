import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LeitorEnfermarias {
    public static List<Enfermaria> lerEnfermarias(String caminho, PrintWriter log) throws FileNotFoundException {
        List<Enfermaria> lista = new ArrayList<>();

        try {
            Scanner leitor = new Scanner(new File(caminho));

            if (leitor.hasNextLine()) {
                leitor.nextLine();
            }

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();

                if (!linha.trim().isEmpty()) {
                    String[] dados = linha.split(";", -1);

                    String codigo = dados[0].trim();
                    String tipo = dados[1].trim();
                    int capacidade = Integer.parseInt(dados[2].trim());

                    if (tipo.equalsIgnoreCase("GERAL")) {
                        int numAcompanhantes = dados[4].trim().isEmpty() ? 0 : Integer.parseInt(dados[4].trim());
                        List<String> recursosDisponiveis = Arrays.asList(dados[8].trim().split(",\\s*"));
                        lista.add(new EnfermariaGeral(codigo, capacidade, numAcompanhantes, recursosDisponiveis));

                    } else if (tipo.equalsIgnoreCase("PSIQ")) {
                        String horarioVisitas = dados[3].trim();
                        String nivelTxt = dados[5].trim();
                        int nivelSeguranca = nivelTxt.equalsIgnoreCase("Alto") ? 3 : (nivelTxt.equalsIgnoreCase("Médio") ? 2 : 1);
                        lista.add(new EnfermariaPsiquiatrica(codigo, capacidade, horarioVisitas, nivelSeguranca));

                    } else if (tipo.equalsIgnoreCase("UCI")) {
                        String horarioVisitas = dados[3].trim();
                        double pressaoAtm = dados[6].trim().isEmpty() ? 0.0 : Double.parseDouble(dados[6].trim());
                        double pressaoAtmRef = dados[7].trim().isEmpty() ? 0.0 : Double.parseDouble(dados[7].trim());
                        lista.add(new EnfermariaCI(codigo, capacidade, horarioVisitas,pressaoAtm, pressaoAtmRef));

                    }else{
                        String erro = "LOG ERRO: Tipo de enfermaria inválido (" + tipo + ") na linha: " + linha;
                        System.out.println(erro);
                        log.println(LocalDate.now() + " | " + erro);
                    }

                }

            }
            leitor.close();


        }catch (FileNotFoundException e) {
            String erro = "ERRO CRÍTICO: O ficheiro " + "Episodios.csv"+ " não foi encontrado.";
            System.out.println(erro);
            log.println(LocalDate.now() + " | " + erro);
        }

        return lista;
    }
}

