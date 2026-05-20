import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um episódio de internamento hospitalar.
 * Contém informações sobre a cama ocupada e o período de permanência,
 * permitindo o cálculo do tempo de internamento e a ordenação cronológica.
 * @author Gabriela Craneiro (202505760)
 * @author Clara Soares (202504216)
 */
public class Episodio implements Comparable<Episodio>, Serializable {
    /**
     * Identificador da cama utilizada durante o episódio
     */
    private int idCama;
    /**
     * Data de entrada do paciente na unidade
     */
    private LocalDate dataAdmissao;
    /**
     * Data de saída do paciente (pode ser nula se o internamento estiver em curso)
     */
    private LocalDate dataAlta;

    /**
     * Construtor completo para a criação de um episódio de internamento.
     * @param idCama Identificador da cama atribuída.
     * @param dataAdmissao Data de início do internamento.
     * @param dataAlta Data de fim do internamento (null se ainda internado).
     */
    public Episodio(int idCama, LocalDate dataAdmissao, LocalDate dataAlta) {

        if (dataAlta != null & dataAlta.isAfter(dataAdmissao)){
            throw new IllegalArgumentException("Erro: Data de alta anterior à admissão (Cama " + idCama + ").");
        }
        this.idCama = idCama;
        this.dataAdmissao = dataAdmissao;
        this.dataAlta = dataAlta;
    }

    /**
     * Devolve o identificador da cama onde o paciente está ou esteve internado.
     * @return O número identificador da cama.
     */
    public int getIdCama() {
        return idCama;
    }
    /**
     * Define o identificador da cama para este episódio de internamento.
     * @param idCama O número da cama a atribuir.
     */
    public void setIdCama(int idCama) {
        this.idCama = idCama;
    }
    /**
     * Devolve a data em que o paciente deu entrada na enfermaria.
     * @return Objeto {@link LocalDate} com a data de admissão.
     */
    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }
    /**
     * Define a data de admissão do paciente.
     * @param dataAdmissao A data de início do episódio.
     */
    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
    /**
     * Devolve a data em que o paciente teve alta da unidade.
     * @return Objeto {@link LocalDate} com a data de alta, ou null se o paciente ainda estiver internado.
     */
    public LocalDate getDataAlta() {
        return dataAlta;
    }
    /**
     * Define a data de alta do paciente, finalizando o episódio de internamento.
     * @param dataAlta A data de fim do episódio.
     */
    public void setDataAlta(LocalDate dataAlta) {
        this.dataAlta = dataAlta;
    }

    /**
     * Verifica se o episódio já foi finalizado com uma data de alta.
     * @return true se existir data de alta, false caso contrário.
     */
    public boolean isAlta(){
        return dataAlta!=null;
    }

    /**
     * Compara este episódio com outro com base na data de admissão.
     * Permite a ordenação cronológica de listas de episódios.
     * @param outro O episódio a comparar.
     * @return Valor negativo se este episódio for anterior, positivo se for posterior.
     */
    @Override
    public int compareTo(Episodio outro){
        if (this.dataAdmissao == null || outro.getDataAdmissao() == null){
            return 0;
        }
        return this.dataAdmissao.compareTo(outro.getDataAdmissao());
    }

    /**
     * Calcula o tempo de permanência (Length of Stay) em dias.
     * O cálculo é feito com base na diferença entre a admissão e a alta.
     * @return O número total de dias de internamento. Devolve 0 se o paciente ainda não tiver tido alta.
     */
    public int calcularLos(){
        if (this.dataAlta == null){
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(this.dataAdmissao,this.dataAlta);
    }
}