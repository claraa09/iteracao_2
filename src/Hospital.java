import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;

/**
 * Classe principal de gestão hospitalar.
 * Atua como o repositório central de dados, sendo responsável pelo carregamento
 * de ficheiros externos, gestão da lista de enfermarias e geração de indicadores globais.
 * @author Gabriela Craneiro (202505760)
 * @author Clara Soares
 */
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

    // Setters omitidos para garantir o encapsulamento e integridade da lista global.

    /**
     * Lê e processa os dados das enfermarias a partir de um ficheiro CSV.
     * Realiza a validação de tipos de enfermaria e capacidades. Erros de leitura
     * são registados na consola e num ficheiro de log.
     * @param enf Nome ou caminho do ficheiro CSV de enfermarias.
     * @param log Objeto PrintWriter para escrita de erros num ficheiro de log externo.
     * @throws FileNotFoundException Caso o ficheiro CSV indicado não seja encontrado.
     */

    public void carregarEnfermarias(String enf, PrintWriter log) throws FileNotFoundException {
        Scanner leitor = new Scanner(new File(enf));

        if (leitor.hasNextLine()) {
            leitor.nextLine(); // Salta cabeçalho
        }

        while (leitor.hasNextLine()) {
            String linha = leitor.nextLine();
            if (!linha.trim().isEmpty()) {
                String[] dados = linha.split(";", -1);

                if (dados.length < 9) {
                    String erro = "LOG ERRO: Dados insuficientes na linha: " + linha;
                    System.out.println(erro);
                    log.println(LocalDate.now() + " | " + erro);
                } else {
                    String codigo = dados[0].trim();
                    String tipo = dados[1].trim();
                    int capacidadeCamas = Integer.parseInt(dados[2].trim());

                    if (capacidadeCamas <= 0) {
                        String erro = "LOG ERRO: Capacidade negativa ou nula (" + capacidadeCamas + ") no código: " + codigo;
                        System.out.println(erro);
                        log.println(LocalDate.now() + " | " + erro);
                    } else {
                        // Lógica de criação por tipo
                        if (tipo.equalsIgnoreCase("GERAL")) {
                            int numAcomp = dados[4].trim().isEmpty() ? 0 : Integer.parseInt(dados[4].trim());
                            List<String> recursos = Arrays.asList(dados[8].trim().split(", "));
                            enfermarias.add(new EnfermariaGeral(codigo, capacidadeCamas, numAcomp, recursos));
                        }
                        else if (tipo.equalsIgnoreCase("PSIQ")) {
                            String horario = dados[3].trim();
                            String nivelTexto = dados[5].trim();
                            int nivel = nivelTexto.equalsIgnoreCase("Alto") ? 3 : (nivelTexto.equalsIgnoreCase("Médio") ? 2 : 1);
                            enfermarias.add(new EnfermariaPsiquiatrica(codigo, capacidadeCamas, horario, nivel));
                        }
                        else if (tipo.equalsIgnoreCase("UCI")) {
                            String horario = dados[3].trim();
                            double pAtm = Double.parseDouble(dados[6].trim());
                            double pRef = Double.parseDouble(dados[7].trim());
                            enfermarias.add(new EnfermariaCI(codigo, capacidadeCamas, horario, pAtm, pRef));
                        }
                        else {
                            String erro = "LOG ERRO: Tipo de enfermaria inválido (" + tipo + ") na linha: " + linha;
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
     * Carrega os episódios de internamento e associa-os à enfermaria correspondente.
     * Verifica a validade lógica das datas (admissão vs alta).
     * @param enfs Nome ou caminho do ficheiro CSV de episódios.
     * @param log Objeto PrintWriter para registo de erros lógicos ou de associação.
     * @throws FileNotFoundException Caso o ficheiro de episódios não exista.
     */
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
}