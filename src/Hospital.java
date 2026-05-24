import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;

public class Hospital {
    /**
     * Nome da unidade hospitalar
     */
    private String nome;
    /**
     * Lista polimórfica que armazena todas as enfermarias do hospital (Geral, PSIQ, UCI)
     */
    private List<Enfermaria> enfermarias;

    /**
     * Construtor da classe Hospital.
     * @param nome Nome a atribuir à unidade hospitalar.
     */
    public Hospital(String nome) {
        this.nome = nome;
        this.enfermarias = new ArrayList<>();
    }

    /**
     * Devolve o nome da unidade hospitalar.
     * @return String contendo o nome do hospital.
     */
    public String getNome() { return nome; }
    /**
     * Devolve a lista global de enfermarias geridas pelo hospital.
     * Esta lista contém todos os tipos de enfermarias (Geral, Psiquiátrica e UCI)
     * carregadas através do sistema.
     * @return List de objetos do tipo {@link Enfermaria}.
     */
    public List<Enfermaria> getEnfermarias() { return enfermarias; }

    // Adiciona este método na classe Hospital
    public void setEnfermarias(List<Enfermaria> enfermarias) {
        this.enfermarias = enfermarias;
    }

    public void carregarEpisodios(String enfs, PrintWriter log) throws FileNotFoundException {
        Scanner leitor = new Scanner(new File(enfs));
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (leitor.hasNextLine()) leitor.nextLine();

        while (leitor.hasNextLine()) {
            String linha = leitor.nextLine();
            if (!linha.trim().isEmpty()) {
                String[] dados = linha.split(";", -1);

                if (dados.length >= 4) {
                    String idEnf = dados[0].trim();
                    int idCama = Integer.parseInt(dados[1].trim());
                    LocalDate dataAdm = LocalDate.parse(dados[2].trim(), formato);

                    LocalDate dataAlta = (dados[3].trim().isEmpty()) ? null : LocalDate.parse(dados[3].trim(), formato);

                    if (dataAlta != null && dataAlta.isBefore(dataAdm)) {
                        String erro = "LOG ERRO: Data de alta inválida no ID " + idEnf + " (Cama " + idCama + ")";
                        System.out.println(erro);
                        log.println(LocalDate.now() + " | " + erro);
                    } else {
                        Enfermaria alvo = null;
                        for (Enfermaria e : enfermarias) {
                            if (e.getCodigo().equals(idEnf)) { alvo = e; }
                        }

                        if (alvo != null) {
                            alvo.getEpisodios().add(new Episodio(idCama, dataAdm, dataAlta));
                        } else {
                            String erro = "LOG ERRO: Enfermaria " + idEnf + " não encontrada para o episódio.";
                            System.out.println(erro);
                            log.println(LocalDate.now() + " | " + erro);
                        }
                    }
                }
            }
        }
        leitor.close();
    }

    /**
     * Ordena a lista global de enfermarias com base no critério de ocupação
     * definido no método compareTo da classe Enfermaria.
     */
    public void ordenarEnfermarias() {
        if (this.enfermarias != null && !this.enfermarias.isEmpty()) {
            this.enfermarias.sort(null);
        }
    }

    /**
     * Calcula a percentagem de dias em que uma enfermaria específica esteve sob
     * pressão num determinado intervalo de tempo.
     * @param idEnf Código identificador da enfermaria.
     * @param inicio Data de início da análise.
     * @param fim Data de fim da análise.
     * @return Percentagem de dias (0.0 a 100.0) em que a ocupação superou o limiar de pressão.
     */
    public double calcularPercentagemPressao(String idEnf, LocalDate inicio, LocalDate fim) {
        Enfermaria alvo = null;
        for (Enfermaria e : enfermarias) {
            if (e.getCodigo().equals(idEnf)) { alvo = e; }
        }

        if (alvo == null) return 0.0;

        int totalDias = 0;
        int diasEmPressao = 0;
        LocalDate dataAtual = inicio;

        while (!dataAtual.isAfter(fim)) {
            totalDias++;
            if (alvo.emPressao(dataAtual)) { diasEmPressao++; }
            dataAtual = dataAtual.plusDays(1);
        }
        return (totalDias == 0) ? 0.0 : ((double) diasEmPressao / totalDias) * 100;
    }

    public static void alterarCamasTotais(List<Enfermaria> listaEnfermarias, double percentagemVariacao) {
        if (listaEnfermarias == null || listaEnfermarias.isEmpty()) {
            return;
        }

        for (Enfermaria enf : listaEnfermarias) {
            int capacidadeAtual = enf.getCapacidadeCamas();
            double variacao = capacidadeAtual * (percentagemVariacao / 100.0);
            int novaCapacidade = (int) Math.round(capacidadeAtual + variacao);

            if (novaCapacidade < 0) {
                novaCapacidade = 0;
            }

            enf.setCapacidadeCamas(novaCapacidade);
        }
    }

    public static double calcularPercentagemEnfermariasEmPressao(List<Enfermaria> lista, LocalDate dataRef) {
        if (lista == null || lista.isEmpty()) {
            return 0.0;
        }

        int contadorPressao = 0;

        // Percorre a lista que veio por parâmetro (já não usa 'this')
        for (Enfermaria enf : lista) {
            if (enf.emPressao(dataRef)) {
                contadorPressao++;
            }
        }

        return ((double) contadorPressao / lista.size()) * 100.0;
    }
}