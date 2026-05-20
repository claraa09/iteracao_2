import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class leitorEpisodios {
    public static List<Enfermaria> ler(String "Enfermaria.csv") {
        List<Enfermaria> enfermariasLidas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Enfermaria.csv"))) {
            String linha = br.readLine(); // Lê a primeira linha (cabeçalhos) e ignora
            int numLinha = 1;

            while ((linha = br.readLine()) != null) {
                numLinha++;
                // O -1 garante que colunas vazias no fim do CSV não rebentam o programa
                String[] dados = linha.split(";", -1);

                try {
                    // Aqui colocas a tua lógica de extração atual, mas protegida!
                    String id = dados[0].trim();
                    String tipo = dados[1].trim();
                    int capacidade = Integer.parseInt(dados[2].trim());

                    // Exemplo de instanciação protegida
                    if (tipo.equalsIgnoreCase("GERAL")) {
                        // ... extrair recursos e numAcompanhantes
                        // enfermariasLidas.add(new EnfermariaGeral(...));
                    }
                    // (Fazer os outros tipos: CI e Psiquiatrica)

                } catch (NumberFormatException e) {
                    System.err.println("Erro: Número inválido na linha " + numLinha + " do ficheiro Enfermarias.");
                } catch (IllegalArgumentException e) {
                    System.err.println("Erro lógico na linha " + numLinha + ": " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Ficheiro de enfermarias não encontrado: " + "\"Enfermaria.csv\"");
        } catch (IOException e) {
            System.err.println("Erro de leitura no ficheiro de enfermarias.");
        }

        return enfermariasLidas;
    }
}

