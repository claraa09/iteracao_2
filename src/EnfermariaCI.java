/**
 * Classe que representa uma Unidade de Cuidados Intensivos (UCI).
 * Estende a classe abstrata Enfermaria e inclui parâmetros de controlo ambiental e horários restritos.
 * @author Clara Soares (202504216)
 * @author Gabriela Carnerio (202505760)
 */
public class EnfermariaCI extends Enfermaria {

    /**
     * Horário permitido para a entrada de visitantes.
     */
    private String horarioVisitas;
    /**
     * Valor atual da pressão atmosférica dentro da unidade.
     */
    private double pressaoAtm;
    /**
     * Valor de referência ideal para a pressão atmosférica da unidade.
     */
    private double pressaoAtmRef;

    /**
     * Construtor para a Enfermaria de Cuidados Intensivos.
     * @param codigo Identificador único da enfermaria.
     * @param capacidadeCamas Número total de camas disponíveis.
     * @param horarioVisitas Período das visitas (ex: 14h-16h).
     * @param pressaoAtm Pressão atmosférica medida.
     * @param pressaoAtmRef Pressão atmosférica de referência.
     */
    public EnfermariaCI (String codigo, int capacidadeCamas, String horarioVisitas, double pressaoAtm, double pressaoAtmRef){
        super (codigo, capacidadeCamas);//Chama o construtor da classe base (Enfermaria)
        this.horarioVisitas=horarioVisitas;
        this.pressaoAtm=pressaoAtm;
        this.pressaoAtmRef=pressaoAtmRef;
    }

    /**
     * Devolve o horário definido para as visitas nesta unidade de cuidados intensivos.
     * @return String com o intervalo de horas das visitas.
     */
    public String getHorarioVisitas(){
        return horarioVisitas;
    }
    /**
     * Define ou altera o horário de visitas da unidade.
     * @param horarioVisitas Novo período de visitas (ex: "11:00-12:00").
     */
    public void setHorarioVisitas(String horarioVisitas){
        this.horarioVisitas=horarioVisitas;
    }
    /**
     * Devolve o valor atual da pressão atmosférica controlada dentro da UCI.
     * @return O valor da pressão atmosférica.
     */
    public double getPressaoAtm(){
        return pressaoAtm;
    }
    /**
     * Atualiza o valor da pressão atmosférica da unidade.
     * @param pressaoAtm O novo valor de pressão atmosférica a registar.
     */
    public void setPressaoAtm(double pressaoAtm){
        this.pressaoAtm= pressaoAtm;
    }

    /**
     * Devolve a pressão atmosférica de referência.
     * @return Valor de referência em double.
     */
    public double getPressaoAtmRef(){
        return pressaoAtmRef;
    }
    public void setPressaoAtmRef(double pressaoAtmRef){
        this.pressaoAtmRef=pressaoAtmRef;
    }
}
