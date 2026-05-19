import java.time.LocalDate;

/**
 * Interface que define os métodos necessários para a gestão de ocupação hospitalar.
 * Garante que qualquer unidade que a implemente seja capaz de calcular a sua taxa de utilização
 * e identificar situações de sobrecarga.
 * @author Gabriela Carneiro (202505760)
 * @author Clara Soares (202504216)
 */
public interface IndicadoresOcupacao {

    /**
     * Calcula a percentagem de camas ocupadas em relação à capacidade total.
     * @param dataRef A data de referÊncia para a qual se pretende o cálculo.
     * @return A taxa de ocupação expressa em percentagem.
     */
    double calcularTaxaOcupacao(LocalDate dataRef);

    /**
     * Verifica se a unidade se encontra em situação de pressão hospitalar.
     * Geralmente, considera-se em pressão quando a ocupação ultrapassa um determinado limiar definido.
     * @param dataRef A data de referência para a verificação.
     * @return true se a unidade estiver em situação de pressão, false caso contrário.
     */
    boolean emPressao(LocalDate dataRef);

    /**
     * Determina o número absoluto de pacientes internados num momento específico.
     * @param dataRef A data de referência para a contagem.
     * @return O número total de camas ocupadas na data indicada.
     */
    int camasOcupadas(LocalDate dataRef);
}
