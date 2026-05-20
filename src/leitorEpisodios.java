import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LeitorEpisodios {

    // Constante para definir as regras das datas (só aqui e não no Menu)
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Repara que recebe o caminho E o objeto hospital
    public static void ler(String "Episodios.csv", Hospital hospital) {

        try (BufferedReader br = new BufferedReader(new FileReader("Episodios.csv"))) {
            String linha = br.readLine();
            int numLinha = 1;

            while ((linha = br.readLine()) != null) {
                numLinha++;
                String[] dados = linha.split(";", -1);

                try {
                    String idEnfermaria = dados[0].trim();
                    String idCama = dados[1].trim();
                    LocalDate dataAd = LocalDate.parse(dados[2].trim(), FORMATO);

                    LocalDate dataAlta = null;
                    if (!dados[3].trim().isEmpty()) {
                        dataAlta = LocalDate.parse(dados[3].trim(), FORMATO);
                    }

                    // A ponte de ligação (Procurar no hospital)
                    Enfermaria alvo = hospital.pesquisarEnfermaria(idEnfermaria);

                    if (alvo != null) {
                        alvo.getEpisodios().add(new Episodio(idCama, dataAd, dataAlta));
                    } else {
                        System.err.println("Aviso (Linha " + numLinha + "): Enfermaria " + idEnfermaria + " não existe.");
                    }

                } catch (DateTimeParseException e) {
                    System.err.println("Erro: Formato de data inválido na linha " + numLinha);
                } catch (IllegalArgumentException e) {
                    System.err.println("Erro: Regra de negócio violada na linha " + numLinha + " -> " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Ficheiro de episódios não encontrado: " + "Episodios.csv");
        } catch (IOException e) {
            System.err.println("Erro de leitura no ficheiro de episódios.");
        }
    }
}