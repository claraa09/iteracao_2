/**
 * Interface que define o contrato para a geração de métricas estatísticas sobre o tempo de internamento dos pacientes.
 * Os métodos aqui definidos permitem analisar o desempenho das enfermarias através do cálculo
 * de indicadores baseados nos episódios que já tiveram alta.
 * @author Gabriela Carneiro (202505760)
 * @author Clara Soares (202504216)
 */
public interface EstatisticaInternamento {

    /**
     * Calcula a média aritmética do tempo de permanência (LoS) dos pacientes.
     * @return O valor médio de dias de internamento. Devolve 0.0 se não existirem episódios com alta.
     */
    double media();

    /**
     * Calcula o desvio padrão do tempo de permanência, indicando a dispersão dos dados em relação à média.
     * @return O valor do desvio padrão. Devolve 0.0 se a lista de episódios for insuficiente para o cálculo.
     */
    double desvioPadrao();

    /**
     * Identifica o menor tempo de internamento registado na unidade.
     * @return O número mínimo de dias que um paciente esteve internado.
     */
    int minimo();

    /**
     * Identifica o maior tempo de internamento registado na unidade.
     * @return O número máximo de dias que um paciente esteve internado.
     */
    int maximo();
}
