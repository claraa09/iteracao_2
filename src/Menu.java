import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável por gerir a interação do utilizador com o programa,
 * permitindo a navegação entre listagens, estatísticas e análises de ocupação.
 * @author Clara Soares (202504216)
 * @author Gabriela Caneiro(202505760)
 */
public class Menu {
    /**
     * Objeto hospital que contém os dados a serem manipulados.
     */
    private Hospital hospital;
    /**
     * Scanner para a leitura de dados da consola.
     */
    private Scanner teclado;
    /**
     * Formatador de datas padrão para entrada e saída de dados (dd/MM/yyyy).
     */
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor da classe Menu.
     * Inicializa o scanner de leitura e associa o hospital de onde os dados serão lidos.
     * @param hospital O objeto Hospital que contém a lista de enfermarias e episódios
     */
    public Menu(Hospital hospital) {
        this.hospital = hospital;
        this.teclado = new Scanner(System.in);
    }

    /**
     * Método principal que inicia o menu.
     * Exibe as opções disponíveis e redireciona o fluxo para os métodos auxiliares,
     * com base na escolha do utilizador.
     */
    public void exibir() {
        int opcao = -1;

        // Cabeçalho inicial ao correr o programa
        System.out.println("\n====================================================");
        System.out.println("   BEM-VINDO AO SISTEMA: " + hospital.getNome().toUpperCase());
        System.out.println("   Estado: Dados carregados e validados com sucesso.");
        System.out.println("====================================================");

        do {
            System.out.println("\n----------- MENU DE GESTÃO -----------");
            System.out.println("1. Listar Enfermarias (Ordenadas por taxa de Ocupação)");
            System.out.println("2. Ver Estatísticas de Internamento (LoS)");
            System.out.println("3. Listar Episódios de uma Enfermaria (Ordenados para data de Admissão)");
            System.out.println("4. Consultar Ocupação em Data Específica");
            System.out.println("5. Calcular Percentagem de Pressão (Período)");
            System.out.println("6. Alterar a capacidade das camas");
            System.out.println("7. Calcular a percentagem de pressão global das enfermarias");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            // Validação para garantir que o utilizador insere um número
            if (teclado.hasNextInt()) {
                opcao = teclado.nextInt();
                teclado.nextLine(); // Limpar o buffer do teclado

                if (opcao == 1) {
                    menuListarEnfermarias();
                }
                else if (opcao == 2) {
                    menuEstatisticasLoS();
                }
                else if (opcao == 3) {
                    menuListarEpisodios();
                }
                else if (opcao == 4) {
                    menuOcupacaoData();
                }
                else if (opcao == 5) {
                    menuAnalisePressao();
                }
                else if (opcao == 6) {
                    alterarCamas();
                }
                else if (opcao == 7) {
                    calcularPercentagemPressaoGlobal();
                }
                else if (opcao == 0) {
                    System.out.println("A encerrar o sistema... Até à próxima!");
                }
                else {
                    System.out.println("Opção inválida! Tente um número entre 0 e 5.");
                }
            } else {
                System.out.println("Erro: Por favor, insira apenas números.");
                teclado.next(); // Limpa a entrada inválida
            }

        } while (opcao != 0);
    }

    /**
     * Exibe o relatório de todas as enfremarias do hospital.
     * As enfermarias são apresentadas ordenadas pela sua taxa de ocupação atual (decrescente).
     */
    private void menuListarEnfermarias() {
        System.out.println("\n--- RELATÓRIO DE ENFERMARIAS ---");
        hospital.ordenarEnfermarias(); // Ordena pela taxa de ocupação (conforme o teu compareTo)
        for (Enfermaria e : hospital.getEnfermarias()) {
            System.out.println(e.toString());
            // Verifica se está em pressão hoje para um aviso extra
            if (e.emPressao(LocalDate.now())) {
                System.out.println("   [!] ATENÇÃO: Capacidade Crítica atingida.");
            }
        }
    }

    /**
     * Exibe as métricas de desempenho (LoS) de cada enfermaria.
     * Inclui média de internamento, valores mínimos, máximos e desvio padrão.
     */
    private void menuEstatisticasLoS() {
        System.out.println("\n--- INDICADORES DE DESEMPENHO (LoS) ---");
        for (Enfermaria e : hospital.getEnfermarias()) {
            System.out.println("Enfermaria " + e.getCodigo() + ":");
            System.out.println("   Média: " + String.format("%.2f", e.media()) + " dias");
            System.out.println("   Mínimo: " + e.minimo() + " | Máximo: " + e.maximo());
            System.out.println("   Desvio Padrão: " + String.format("%.2f", e.desvioPadrao()));
        }
    }

    /**
     * Solicita um código de enfermaria e lista os seus episódios.
     * Os episódios são apresentados ordenados pela data de admissão.
     */
    private void menuListarEpisodios() {
        System.out.print("Introduza o código da enfermaria: ");
        String cod = teclado.nextLine();

        boolean encontrada = false;
        for (Enfermaria e : hospital.getEnfermarias()) {
            if (e.getCodigo().equalsIgnoreCase(cod)) {
                encontrada = true;
                e.ordenarEpisodios(); // Ordena por data de admissão
                System.out.println("\nEPISÓDIOS DA ENFERMARIA " + cod + ":");
                for (Episodio ep : e.getEpisodios()) {
                    String alta = ep.isAlta() ? ep.getDataAlta().format(formato) : "Ainda Internado";
                    System.out.println("   Cama " + ep.getIdCama() + " | Início: " +
                            ep.getDataAdmissao().format(formato) + " | Fim: " + alta +
                            " | LoS: " + ep.calcularLos());
                }
            }
        }
        if (!encontrada) System.out.println("Enfermaria não encontrada.");
    }

    /**
     * Solicita uma data específica e exibe a taxa de ocupação das enfermarias nesse dia.
     */
    private void menuOcupacaoData() {
        System.out.print("Introduza a data de análise (dd/MM/yyyy): ");
        String dataLida = teclado.nextLine();
        LocalDate data = LocalDate.parse(dataLida, formato);

        System.out.println("\nTAXA DE OCUPAÇÃO EM " + data.format(formato) + ":");
        for (Enfermaria e : hospital.getEnfermarias()) {
            double taxa = e.calcularTaxaOcupacao(data);
            System.out.println("   Enfermaria " + e.getCodigo() + ": " + String.format("%.2f", taxa) + "%");
        }
    }

    /**
     * Analisa a pressão hospitalar de uma enfermaria num determinado intervalo de tempo.
     * Solicita o ID da enfermaria e as datas de início e fim do período.
     */
    private void menuAnalisePressao() {
        System.out.print("ID da Enfermaria: ");
        String id = teclado.nextLine();

        System.out.print("Data de Início (dd/MM/yyyy): ");
        String dataInicioLida = teclado.nextLine();
        LocalDate inicio = LocalDate.parse(dataInicioLida, formato);

        System.out.print("Data de Fim (dd/MM/yyyy): ");
        String dataFimLida = teclado.nextLine();
        LocalDate fim = LocalDate.parse(dataFimLida, formato);

        double perc = hospital.calcularPercentagemPressao(id, inicio, fim);
        System.out.println("\nRESULTADO: Pressão em " + String.format("%.2f", perc) + "% do tempo.");
    }

    private void alterarCamas(){
        System.out.print("Insira a percentagem de alteração (ex: 10 para aumentar, -5 para diminuir): ");
        double percentagem = teclado.nextDouble();

        Hospital.alterarCamasTotais(hospital.getEnfermarias(), percentagem);

        System.out.println("Capacidade de todas as enfermarias atualizada com sucesso!");
    }

    private void calcularPercentagemPressaoGlobal() {
        System.out.print("Introduza a data de referência (dd/MM/yyyy): ");
        String dataLida = teclado.nextLine();

        try {
            LocalDate data = LocalDate.parse(dataLida, formato);

            double percentagem = Hospital.calcularPercentagemEnfermariasEmPressao(hospital.getEnfermarias(), data);

            System.out.println("\n--- ANÁLISE DE PRESSÃO GLOBAL ---");
            System.out.printf("Em %s, %.2f%% das enfermarias do hospital estavam em esforço crítico (>85%%).%n",
                    data.format(formato), percentagem);

        } catch (Exception e) {
            System.out.println("Erro: Formato de data inválido. Use o padrão dd/MM/yyyy.");
        }
    }
}