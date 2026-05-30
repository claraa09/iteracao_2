package org.example;

import java.util.Arrays;
import java.util.List;
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
     *
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
            System.out.println("6. Inserir um novo registo");
            System.out.println("7. Alterar a capacidade das camas");
            System.out.println("8. Calcular a percentagem de pressão global das enfermarias");
            System.out.println("9. Calcular Índice de Pressão (ordenado por ordem decrescente)");
            System.out.println("10. Visualizar Gráfico de Barras (Ocupação)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            // Validação para garantir que o utilizador insere um número
            if (teclado.hasNextInt()) {
                opcao = teclado.nextInt();
                teclado.nextLine(); // Limpar o buffer do teclado

                switch (opcao) {
                    case 1:
                        menuListarEnfermarias();
                        break;
                    case 2:
                        menuEstatisticasLoS();
                        break;
                    case 3:
                        menuListarEpisodios();
                        break;
                    case 4:
                        menuOcupacaoData();
                        break;
                    case 5:
                        menuAnalisePressao();
                        break;
                    case 6:
                        escolherRegisto();
                        break;
                    case 7:
                        alterarCamas();
                        break;
                    case 8:
                        calcularPercentagemPressaoGlobal();
                        break;
                    case 9:
                        menuIndicePressao();
                        break;
                    case 10:
                        menuGraficoBarras();
                        break;
                    case 0:
                        System.out.println("A encerrar o sistema... Até à próxima!");
                        GestorDados.guardarEstado(hospital);
                        System.out.println("A encerrar o sistema... Até à próxima!");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente um número entre 0 e 9.");
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
            System.out.println("   Média: " + String.format("%.2f", e.determinarMedia()) + " dias");
            System.out.println("   Mínimo: " + e.determinarMinimo() + " | Máximo: " + e.determinarMaximo());
            System.out.println("   Desvio Padrão: " + String.format("%.2f", e.determinarDesvioPadrao()));
        }
    }

    /**
     * Solicita um código de enfermaria e lista os seus episódios.
     * Os episódios são apresentados ordenados pela data de admissão.
     */
    private void menuListarEpisodios() {
        System.out.print("Introduza o código da enfermaria: ");
        String codigo = teclado.nextLine().trim();

        boolean encontrada = false;
        for (Enfermaria e : hospital.getEnfermarias()) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                encontrada = true;
                e.ordenarEpisodios(); // Ordena por data de admissão
                System.out.println("\nEPISÓDIOS DA ENFERMARIA " + codigo + ":");
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

        while (!Validador.isData(dataLida)) {
            System.out.print("Erro: Formato inválido. Reintroduza a data (dd/MM/yyyy): ");
            dataLida = teclado.nextLine();
        }
        LocalDate data = Validador.converterDataSegura(dataLida);

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
        String id = teclado.nextLine().trim();

        Enfermaria e = hospital.pesquisarEnfermaria(id);
        if (e == null) {
            System.out.println ("Enfermaria não encontrada.");
            return;
        }

        System.out.print("Data de Início (dd/MM/yyyy): ");
        String dataInicioLida = teclado.nextLine();
        while (!Validador.isData(dataInicioLida)) {
            System.out.print("Erro: Formato inválido. Reintroduza a data de início (dd/MM/yyyy): ");
            dataInicioLida = teclado.nextLine();
        }
        LocalDate inicio = Validador.converterDataSegura(dataInicioLida);

        System.out.print("Data de Fim (dd/MM/yyyy): ");
        String dataFimLida = teclado.nextLine();
        while (!Validador.isData(dataFimLida)) {
            System.out.print("Erro: Formato inválido. Reintroduza a data de fim (dd/MM/yyyy): ");
            dataFimLida = teclado.nextLine();
        }
        LocalDate fim = Validador.converterDataSegura(dataFimLida);

        if (fim.isBefore(inicio)) {
            System.out.println("Erro: A data de fim não pode ser anterior à data de início.");
            return;
        }
        double perc = hospital.calcularPercentagemPressao(id, inicio, fim);
        System.out.println("\nRESULTADO: Pressão em " + String.format("%.2f", perc) + "% do tempo.");
    }

    /**
     * Apresenta o submenu interativo para seleção do tipo de registo a inserir.
     * Navega para os fluxos específicos de criação de episódio ou enfermaria.
     */
    private void escolherRegisto() {
        int opcao = -1;
        do {
            System.out.println("\n--- SUB-MENU INSERÇÃO ---");
            System.out.println("1. Inserir novo Episódio");
            System.out.println("2. Inserir nova Enfermaria");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            if (teclado.hasNextInt()) {
                opcao = teclado.nextInt();
                teclado.nextLine();
                switch (opcao) {
                    case 1:
                        inserirEpisodio();
                        break;
                    case 2:
                        inserirEnfermaria();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } else {
                System.out.println("Erro: Insira um número.");
                teclado.nextLine(); // Limpar entrada inválida
            }
        } while (opcao != 0);
    }

    private void inserirEpisodio() {
        System.out.println("\n--- INSERIR NOVO EPISÓDIO ---");

        System.out.print("Introduza o Código da Enfermaria: ");
        String idEnf = teclado.nextLine();
        Enfermaria alvo = hospital.pesquisarEnfermaria(idEnf);

        if (alvo == null) {
            System.out.println("Erro: A enfermaria " + idEnf + " não existe no sistema. Registo cancelado.");
            return; // Sai do método e volta ao menu
        }

        System.out.print("Introduza o número da Cama: ");
        String camaTxt = teclado.nextLine();
        while (!Validador.isInteiro(camaTxt)) {
            System.out.print("Erro! Tem de inserir um número inteiro válido. Tente novamente: ");
            camaTxt = teclado.nextLine();
        }
        int idCama = Integer.parseInt(camaTxt);

        System.out.print("Introduza a Data de Admissão (dd/MM/yyyy): ");
        String dataAdmTxt = teclado.nextLine();
        while (!Validador.isData(dataAdmTxt)) {
            System.out.print("Erro! Formato inválido. Use dd/MM/yyyy: ");
            dataAdmTxt = teclado.nextLine();
        }
        LocalDate dataAdmissao = Validador.converterDataSegura(dataAdmTxt);

        System.out.print("Introduza a Data de Alta (Deixe em branco se ainda internado): ");
        String dataAltaTxt = teclado.nextLine();
        LocalDate dataAlta = null;

        if (!dataAltaTxt.trim().isEmpty()) {
            while (!Validador.isData(dataAltaTxt)) {
                System.out.print("Erro! Formato inválido. Use dd/MM/yyyy ou deixe em branco: ");
                dataAltaTxt = teclado.nextLine();
            }
            dataAlta = Validador.converterDataSegura(dataAltaTxt);
        }

        try {
            Episodio novoEp = new Episodio(idCama, dataAdmissao, dataAlta);
            alvo.adicionarEpisodio(novoEp);

            System.out.println("Sucesso! Episódio registado na enfermaria " + idEnf + ".");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na inserção: " + e.getMessage());
        }

    }

    private void inserirEnfermaria() {
        System.out.println("\n--- INSERIR NOVA ENFERMARIA ---");

        System.out.print("Introduza o Código da nova Enfermaria (ex: E06): ");
        String codigo = teclado.nextLine().trim();

        if (hospital.pesquisarEnfermaria(codigo) != null) {
            System.out.println("Erro: Já existe uma enfermaria com o código " + codigo + ".");
            return;
        }

        System.out.print("Tipo de Enfermaria (GERAL / PSIQ / UCI): ");
        String tipo = teclado.nextLine().trim().toUpperCase();
        if (!tipo.equals("GERAL") && !tipo.equals("PSIQ") && !tipo.equals("UCI")) {
            System.out.println("Erro: Tipo de enfermaria inválido.");
            return;
        }

        System.out.print("Capacidade total de camas: ");
        String capTxt = teclado.nextLine();
        while (!Validador.isInteiro(capTxt)) {
            System.out.print("Erro! Insira um número inteiro para a capacidade: ");
            capTxt = teclado.nextLine();
        }
        int capacidade = Integer.parseInt(capTxt);

        Enfermaria novaEnfermaria = null;

        if (tipo.equals("GERAL")) {
            System.out.print("Horário de visitas (ex: 14:00 - 16:00): ");
            String horario = teclado.nextLine().trim();
            while (!Validador.isHorario(horario)) {
                System.out.print("Erro! Formato inválido. Use o padrão HH:MM - HH:MM: ");
                horario = teclado.nextLine().trim();
            }
            String acompTxt = teclado.nextLine();
            while (!Validador.isInteiro(acompTxt)) {
                System.out.print("Erro! Insira um número inteiro: ");
                acompTxt = teclado.nextLine();
            }
            int acompanhantes = Integer.parseInt(acompTxt);

            System.out.print("Insira os recursos (separados por vírgulas, ex: Oxigénio, Monitor): ");
            String recursosInput = teclado.nextLine();
            List<String> recursos = Arrays.asList(recursosInput.split(",\\s*"));

            novaEnfermaria = new EnfermariaGeral(codigo, capacidade, acompanhantes, recursos);

        } else if (tipo.equals("PSIQ")) {
            System.out.print("Horário de visitas (ex: 14:00 - 16:00): ");
            String horario = teclado.nextLine().trim();

            System.out.print("Nível de segurança (1-Baixo, 2-Médio, 3-Alto): ");
            String nivelTxt = teclado.nextLine();
            while (!Validador.isInteiro(nivelTxt)) {
                System.out.print("Erro! Escolha um número inteiro (1, 2 ou 3): ");
                nivelTxt = teclado.nextLine();
            }
            int nivel = Integer.parseInt(nivelTxt);

            novaEnfermaria = new EnfermariaPsiquiatrica(codigo, capacidade, horario, nivel);

        } else if (tipo.equals("UCI")) {
            System.out.print("Horário de visitas (ex: 14:00 - 16:00): ");
            String horario = teclado.nextLine().trim();
            while (!Validador.isHorario(horario)) {
                System.out.print("Erro! Formato inválido. Use o padrão HH:MM - HH:MM: ");
                horario = teclado.nextLine().trim();
            }

            System.out.print("Pressão Atmosférica Atual: ");
            String pAtmTxt = teclado.nextLine();
            while (!Validador.isDecimal(pAtmTxt)) {
                System.out.print("Erro! Insira um valor decimal (ex: 1.0): ");
                pAtmTxt = teclado.nextLine();
            }
            double pAtm = Double.parseDouble(pAtmTxt);

            System.out.print("Pressão Atmosférica de Referência: ");
            String pRefTxt = teclado.nextLine();
            while (!Validador.isDecimal(pRefTxt)) {
                System.out.print("Erro! Insira um valor decimal: ");
                pRefTxt = teclado.nextLine();
            }
            double pRef = Double.parseDouble(pRefTxt);

            novaEnfermaria = new EnfermariaCI(codigo, capacidade, horario, pAtm, pRef);
        }

        if (novaEnfermaria != null) {
            hospital.adicionarEnfermaria(novaEnfermaria);
            System.out.println("Sucesso: Enfermaria " + codigo + " (" + tipo + ") adicionada ao hospital!");
        }
    }

    /**
     * Executa o fluxo de alteração global de camas de todas as enfermarias do sistema.
     * Solicita o fator de variação e efetua a limpeza do buffer pós-leitura de valores decimais.
     */
    private void alterarCamas() {
        System.out.print("Insira a percentagem de alteração (ex: 10 para aumentar, -5 para diminuir): ");
        double percentagem = teclado.nextDouble();
        teclado.nextLine();

        Hospital.alterarCamasTotais(hospital.getEnfermarias(), percentagem);

        System.out.println("Capacidade de todas as enfermarias atualizada com sucesso!");
    }

    private void calcularPercentagemPressaoGlobal() {
        System.out.print("Introduza a data de referência (dd/MM/yyyy): ");
        String dataLida = teclado.nextLine();

        try {
            LocalDate data = Validador.converterDataSegura(dataLida);

            double percentagem = Hospital.calcularPercentagemEnfermariasEmPressao(hospital.getEnfermarias(), data);

            System.out.println("\n--- ANÁLISE DE PRESSÃO GLOBAL ---");
            System.out.printf("Em %s, %.2f%% das enfermarias do hospital estavam em esforço crítico (>85%%).%n",
                    data.format(formato), percentagem);

        } catch (Exception e) {
            System.out.println("Erro: Formato de data inválido. Use o padrão dd/MM/yyyy.");
        }
    }

    /**
     * Solicita uma data de referência e apresenta o ranking de enfermarias
     * pelo Índice de Pressão em ordem decrescente.
     */
    private void menuIndicePressao(){
        System.out.println("Introduza a data de referência (dd/MM/yyyy): ");
        String dataLida = teclado.nextLine();

        try {
            LocalDate data = Validador.converterDataSegura(dataLida);

            Hospital.ordenarEnfermariasPorIndice(hospital.getEnfermarias(), data);

            System.out.println("\n--- RANKING DE ÍNDICE DE PRESSÃO EM " + data.format(formato) + " ---");
            System.out.printf("%-4s | %-6s | %-8s | %s%n", "Pos.", "Enf.", "Índice", "Interpretação");
            System.out.println("-".repeat(45));

            int posicao = 1;
            for (Enfermaria e : hospital.getEnfermarias()){
                double indice = e.calcularIndicePressao(data);

                String interpretacao;
                if (indice <= 2.0){
                    interpretacao="Pressão Baixa";
                } else if (indice <= 3.5) {
                    interpretacao = "Pressão Moderada";
                } else {
                    interpretacao = "Pressão Alta";
                }

                System.out.printf("%-4d | %-6s | %-8.1f | %s%n", posicao, e.getCodigo(), indice, interpretacao);
                posicao++;
            }

        } catch (Exception e) {
            System.out.println("Erro: Formato de data inválido. Use o padrão dd/MM/yyyy.");
        }
    }

    private void menuGraficoBarras() {
        System.out.print("Introduza o código da enfermaria: ");
        String cod = teclado.nextLine();

        Enfermaria enfermariaEscolhida = hospital.pesquisarEnfermaria(cod);
        if (enfermariaEscolhida == null) {
            System.out.println("Erro: Enfermaria não encontrada.");
            return;
        }

        System.out.print("Data de Início (dd/MM/yyyy): ");
        String dataInicio = teclado.nextLine();
        while (!Validador.isData(dataInicio)) {
            System.out.print("Erro! Formato incorreto. Reintroduza a Data de Início (dd/MM/yyyy) sem espaços extra: ");
            dataInicio = teclado.nextLine();
        }
        LocalDate inicio = Validador.converterDataSegura(dataInicio);

        System.out.print("Data de Fim (dd/MM/yyyy): ");
        String dataFim = teclado.nextLine();
        while (!Validador.isData(dataFim)) {
            System.out.print("Erro! Formato incorreto. Reintroduza a Data de Fim (dd/MM/yyyy): ");
            dataFim = teclado.nextLine();
        }
        LocalDate fim = Validador.converterDataSegura(dataFim);

        if (fim.isBefore(inicio)) {
            System.out.println("Erro: A data de fim não pode ser anterior à data de início.");
            return;
        }

        char simbolo = '#';

        System.out.println("\nOrientação:");
        System.out.println("1. Horizontal");
        System.out.println("2. Vertical");
        System.out.println("0. Voltar ao menu principal");
        System.out.print("Escolha: ");

        int orientacao = -1;
        if (teclado.hasNextInt()) {
            orientacao = teclado.nextInt();
        }
        teclado.nextLine();

        switch (orientacao) {
            case 1:
                mostrarGraficoHorizontal(enfermariaEscolhida, inicio, fim, simbolo);
                break;
            case 2:
                mostrarGraficoVertical(enfermariaEscolhida, inicio, fim, simbolo);
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida. Escolha 1 (Horizontal) ou 2 (Vertical).");
                break;
        }

    }


    private void mostrarGraficoHorizontal (Enfermaria enfermaria, LocalDate inicio, LocalDate fim,char simbolo){
        System.out.println("\n--- GRÁFICO DE BARRAS HORIZONTAL (" + enfermaria.getCodigo() + ") ---");
        LocalDate dataAtual = inicio;

        while (!dataAtual.isAfter(fim)) {

            double perc = enfermaria.calcularTaxaOcupacao(dataAtual);
            int numBarras = (int) Math.round((perc * 50) / 100.0);

            StringBuilder barra = new StringBuilder();
            for (int i = 0; i < numBarras; i++) {
                barra.append(simbolo);
            }

            System.out.printf("%s %s [%s]\n",
                    enfermaria.getCodigo(),
                    dataAtual.format(formato),
                    barra.toString());

            dataAtual = dataAtual.plusDays(1);
        }
    }

    private void mostrarGraficoVertical (Enfermaria enfermaria, LocalDate inicio, LocalDate fim,char simbolo){
        System.out.println("\n--- GRÁFICO DE BARRAS VERTICAL (" + enfermaria.getCodigo() + ") ---");

        java.util.List<Double> percentagens = new java.util.ArrayList<>();
        java.util.List<String> datas = new java.util.ArrayList<>();

        LocalDate dataAtual = inicio;
        while (!dataAtual.isAfter(fim)) {
            percentagens.add(enfermaria.calcularTaxaOcupacao(dataAtual));
            datas.add(dataAtual.format(DateTimeFormatter.ofPattern("dd/MM")));
            dataAtual = dataAtual.plusDays(1);
        }

        int linhasAltura = 10;

        for (int nivel = linhasAltura; nivel > 0; nivel--) {
            System.out.printf("%3d%% | ", nivel * 10);

            for (Double perc : percentagens) {

                int barrasVerticais = (int) Math.round(perc / 10.0);

                if (barrasVerticais >= nivel) {
                    System.out.print("  " + simbolo + "   ");
                } else {
                    System.out.print("      ");
                }
            }
            System.out.println();
        }

        System.out.print("      ");
        for (int i = 0; i < percentagens.size(); i++) {
            System.out.print("------");
        }
        System.out.println();

        System.out.print("       ");
        for (String d : datas) {
            System.out.print(d + "  ");
        }
        System.out.println();
    }
}