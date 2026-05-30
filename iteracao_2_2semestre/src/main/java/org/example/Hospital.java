package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Classe principal do modelo de domínio que representa a unidade hospitalar.
 * Centraliza a gestão de todas as enfermarias e fornece operações globais
 * de pesquisa, cálculo de pressão e alteração de capacidades.
 *
 * @author Clara Soares (202504216)
 * @author Gabriela Carneiro (202505760)
 */
public class Hospital implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
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

    /**
     * Substitui a lista de enfermarias do hospital por uma nova lista.
     * @param enfermarias A nova lista de enfermarias a associar ao hospital.
     */
    public void setEnfermarias(List<Enfermaria> enfermarias) {
        this.enfermarias = enfermarias;
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

    /**
     * Altera a capacidade total de camas de uma lista de enfermarias aplicando uma percentagem de variação.
     * Garante que a capacidade nunca assume valores negativos.
     * @param listaEnfermarias Lista de enfermarias a alterar.
     * @param percentagemVariacao Valor percentual da alteração (positivo para aumentar, negativo para diminuir).
     */
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

    /**
     * Calcula a percentagem de enfermarias de uma dada lista que se encontram em situação
     * de pressão hospitalar numa data específica.
     * @param lista Lista de enfermarias a analisar.
     * @param dataRef Data de referência para o cálculo.
     * @return Percentagem (0.0 a 100.0) de enfermarias em pressão.
     */
    public static double calcularPercentagemEnfermariasEmPressao(List<Enfermaria> lista, LocalDate dataRef) {
        if (lista == null || lista.isEmpty()) {
            return 0.0;
        }

        int contadorPressao = 0;

        for (Enfermaria enf : lista) {
            if (enf.emPressao(dataRef)) {
                contadorPressao++;
            }
        }
        return ((double) contadorPressao / lista.size()) * 100.0;
    }

    /**
     * Procura uma enfermaria na lista do hospital através do seu código identificador.
     * @param codigo O código da enfermaria a pesquisar (ex: "E01").
     * @return O objeto {@link Enfermaria} encontrado, ou null se não existir.
     */
    public Enfermaria pesquisarEnfermaria(String codigo) {
        if (codigo == null || this.enfermarias == null) {
            return null;
        }

        // Percorre a lista de enfermarias do hospital
        for (Enfermaria e : this.enfermarias) {
            // Ignora maiúsculas/minúsculas e espaços extras
            if (e.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return e; // Encontrou! Devolve a enfermaria imediatamente
            }
        }
        return null; // Se percorreu tudo e não encontrou, devolve null
    }

    /**
     * Ordena uma lista de enfermarias por ordem decrescente do seu Índice de Pressão
     * calculado para uma data específica. Utiliza um Comparator anónimo.
     * @param enfermarias Lista de enfermarias a ordenar.
     * @param data Data de referência para calcular o índice de cada enfermaria.
     */
    public static void ordenarEnfermariasPorIndice(List<Enfermaria> enfermarias, LocalDate data){
        Collections.sort(enfermarias, Collections.reverseOrder(new Comparator<Enfermaria>() {
            @Override
            public int compare(Enfermaria e1, Enfermaria e2) {
                double indice1 = e1.calcularIndicePressao(data);
                double indice2 = e2.calcularIndicePressao(data);
                if (indice1 == indice2){
                    return 0;
                } else if (indice1<indice2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }));
    }

    /**
     * Adiciona uma nova enfermaria diretamente à lista definitiva do hospital.
     * @param novaEnf O objeto {@link Enfermaria} a ser inserido.
     */
    public void adicionarEnfermaria(Enfermaria novaEnf) {
        if (novaEnf != null) {
            this.enfermarias.add(novaEnf); // Adiciona diretamente à lista real e definitiva
        }
    }
}