package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa uma unidade de internamento (Enfermaria).
 * Esta classe define a estrutura base e o comportamento comum a todas as enfermarias,
 * implementando a lógica de ocupação, estatísticas de permanência e ordenação.
 * @author Gabriela Carneiro (202505760)
 * @author Clara Soares (202504216)
 */
public abstract class Enfermaria implements Comparable<Enfermaria>, java.io.Serializable {
    /**
     * Código identificador único da enfermaria.
     */
    private String codigo;
    /**
     * Capacidade total de camas da unidade.
     */
    private int capacidadeCamas;
    /**
     * Lista de episódios de internamento ocorridos nesta enfermaria.
     */
    private List<Episodio> episodios;
    /**
     * Percentagem limite (85%) para considerar a enfermaria em situação de pressão.
     */
    private static final double LIMIAR_DE_PRESSAO = 85.0;

    /**
     * Construtor completo para criação de uma enfermaria.
     * @param codigo Identificador único de enfermaria.
     * @param capacidadeCamas Número total de camas disponíveis.
     */
    public Enfermaria (String codigo, int capacidadeCamas){
        this.codigo =  codigo;
        this.capacidadeCamas = capacidadeCamas;

        this.episodios = new ArrayList<>();
    }

    /**
     * Devolve o código identificador único da enfermaria.
     * @return String contendo o código (ex: "E01").
     */
    public String getCodigo(){return codigo;}
    /**
     * Devolve a capacidade total de camas configurada para esta unidade.
     * @return O número total de camas disponíveis.
     */
    public int getCapacidadeCamas(){
        return capacidadeCamas;
    }
    /**
     * Devolve a lista de todos os episódios de internamento associados a esta enfermaria.
     * @return Uma List contendo os objetos {@link Episodio}.
     */
    public List<Episodio> getEpisodios(){
        return episodios;
    }
    /**
     * Atualiza a capacidade total de camas da enfermaria.
     * @param capacidadeCamas O novo número de camas disponíveis.
     */
    public void setCapacidadeCamas(int capacidadeCamas){this.capacidadeCamas = capacidadeCamas;}
    /**
     * Substitui a lista atual de episódios de internamento por uma nova lista.
     * @param episodios A nova lista de objetos {@link Episodio} a associar à unidade.
     */
    public void setEpisodios(List<Episodio> episodios){this.episodios=episodios;}

    /**
     * Calcula o número de camas ocupadas numa determinada data de referência.
     * @param dataRef Data para validar a ocupação.
     * @return Número de pacientes internados no momento indicado.
     */
    public int camasOcupadas (LocalDate dataRef){
        int ocupadas = 0;
        for (Episodio e : episodios){
            if (!e.getDataAdmissao().isAfter(dataRef) &&
                    (e.getDataAlta() == null || !e.getDataAlta().isBefore(dataRef))){
                ocupadas++;
            }
        }
        return ocupadas;
    }

    /**
     * Calcula a taxa de ocupação da enfermaria para uma data específica.
     * @param dataRef Data de referência para o cálculo.
     * @return Percentagem de ocupação (0 a 100).
     */
    public double calcularTaxaOcupacao(LocalDate dataRef){
        if (this.capacidadeCamas == 0){
            return 0.0;
        }
        int ocupadas = camasOcupadas(dataRef);
        return ((double) ocupadas / this.capacidadeCamas) * 100;
    }

    /**
     * Verifica se a enfermaria está em situação de pressão hospitalar (ocupação > 85%).
     * @param dataRef Data para verificação.
     * @return true se a taxa for superior ao limiar, false caso contrário.
     */
    public boolean emPressao(LocalDate dataRef ){
        return calcularTaxaOcupacao(dataRef) > LIMIAR_DE_PRESSAO;
    }

    public String obterBarraASCII (double percentagem, char simbolo){
        if (percentagem <0) {
            double pergentagem = 0;
        }

        int numCaracteres = (int) Math.round ((percentagem*50)/100);

        StringBuilder barra = new StringBuilder();
        for (int i=0; i<numCaracteres; i++){
            barra.append(simbolo);
        }

        return barra.toString();
    }

    /**
     * Devolve uma representação textual da enfermaria, incluindo código,
     * capacidade e taxa de ocupação atual.
     * @return String formatada com os dados da enfermaria.
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append ("\nEnfermaria Código: ").append(codigo);
        s.append ("\nCapacidade: ").append(capacidadeCamas).append("camas");
        double taxaHoje = calcularTaxaOcupacao(LocalDate.now());
        s.append ("\nTaxa de Ocupação: ").append(String.format("%.2f", taxaHoje)).append("%");
        s.append("\nGráfico Ocupação: [").append(obterBarraASCII(taxaHoje, '#')).append("]");
        return s.toString();
    }

    /**
     * Compara duas enfermarias com base no código identificador.
     * @param obj Objeto a comparar.
     * @return true se os códigos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals (Object obj){
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Enfermaria other = (Enfermaria) obj;
        return this.codigo.equals(other.codigo);
    }

    /**
     * Define a ordem natural das enfermarias com base na taxa de ocupação (decrescente).
     * @param outra A enfermaria a comparar.
     * @return Resultado da comparação para ordenação.
     */
    @Override
    public int compareTo(Enfermaria outra) {
        LocalDate hoje = LocalDate.now();
        return Double.compare(outra.calcularTaxaOcupacao(hoje), this.calcularTaxaOcupacao(hoje));
    }

    /**
     * Ordena a lista de episódios da enfermaria utilizando a ordenação natural da classe Episodio.
     */
    public void ordenarEpisodios() {
        if (this.episodios != null && !this.episodios.isEmpty()) {
            this.episodios.sort(null);
        }
    }

    /**
     * Filtra a lista global de episódios devolvendo apenas aqueles que já receberam alta.
     * @return Lista de episódios finalizados.
     */
    private List<Episodio> getEpisodiosAltas(){
        List<Episodio> finalizados = new ArrayList<>();
        for (Episodio e : episodios){
            if (e.isAlta()){
                finalizados.add(e);
            }
        }
        return finalizados;
    }

    /**
     * Determina o tempo mínimo de internamento (LoS) entre os episódios finalizados.
     * @return Menor número de dias de internamento.
     */
    public int determinarMinimo(){
        List<Episodio> lista = getEpisodiosAltas();
        if (lista.isEmpty()){
            return 0;
        }
        int min = lista.getFirst().calcularLos();
        for(Episodio e : lista){
            if (e.calcularLos()<min){
                min=e.calcularLos();
            }
        }
        return min;
    }

    /**
     * Determina o tempo máximo de internamento (LoS) entre os episódios finalizados.
     * @return Maior número de dias de internamento.
     */
    public int determinarMaximo(){
        List<Episodio> lista = getEpisodiosAltas();
        if (lista.isEmpty()){
            return 0;
        }
        int max = lista.get(0).calcularLos();
        for(Episodio e : lista){
            if (e.calcularLos()> max){
                max =e.calcularLos();
            }
        }
        return max;
    }

    /**
     * Calcula a média aritmética do tempo de permanência dos episódios com alta.
     * @return Média de dias de internamento.
     */
    public double determinarMedia(){
        List<Episodio> lista = getEpisodiosAltas();
        if (lista.isEmpty()){
            return 0;
        }
        double soma = 0;
        for (Episodio e: lista){
            soma += e.calcularLos();
        }
        return soma/lista.size();
    }

    /**
     * Calcula o desvio padrão do tempo de permanência dos episódios com alta.
     * @return Valor do desvio padrão.
     */
    public double determinarDesvioPadrao(){
        List<Episodio> lista = getEpisodiosAltas();
        int n = lista.size();
        if (n==0){
            return 0.0;
        }
        double media = determinarMedia();
        double somaQuadrados = 0;
        for (Episodio e: lista){
            somaQuadrados += Math.pow(e.calcularLos() - media, 2);
        }
        return Math.sqrt(somaQuadrados/n);
    }

    /**
     * Calcula o score de ocupação (1-5) para uma data de referência.
     * @param data Data de referência
     * @return Score entre 1 e 5
     */
    public int calcularScoreOcupacao(LocalDate data){

        double taxa = calcularTaxaOcupacao(data);

        if (taxa <= LIMIAR_DE_PRESSAO) {
            return 1;
        } else if (taxa <= 90){
            return 2;
        }else if (taxa <95){
            return 3;
        } else if (taxa <= 100) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * Calcula o score de turnover (1-5) para uma data de referência.
     * percTurnover = (admissões + altas) / camasTotais × 100
     * @param data Data de referência
     * @return Score entre 1 e 5
     */
    public int calcularScoreTurnover(LocalDate data) {
        int admissoes = 0;
        int altas = 0;

        for (Episodio ep : episodios) {
            if (ep.getDataAdmissao().isEqual(data)) {
                admissoes++;
            }
            if (ep.isAlta() && ep.getDataAlta().isEqual(data)) {
                altas++;
            }
        }

        double turnover = (double)(admissoes + altas) / getCapacidadeCamas() * 100;

        if (turnover <= 10) {
            return 1;
        } else if (turnover <= 20) {
            return 2;
        } else if (turnover <= 30) {
            return 3;
        } else if (turnover <= 40) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * Calcula o Índice de Pressão (1-5) com 1 casa decimal.
     * Índice = 0.7 × scoreOcupação + 0.3 × scoreTurnover
     * @param data Data de referência
     * @return Índice de pressão
     */
    public double calcularIndicePressao(LocalDate data) {
        double indice = 0.7 * calcularScoreOcupacao(data) + 0.3 * calcularScoreTurnover(data);
        return Math.round(indice * 10.0) / 10.0;// Arredonda o índice para 1 casa decimal
        }
}