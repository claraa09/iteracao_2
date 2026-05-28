package org.example;

/**
 * Representa uma unidade de internamento de Psiquiatria.
 * Esta classe estende a classe abstrata Enfermaria, incorporando
 * especificidades como o nível de segurança da unidade e horários de visita restritos.
 * @author Clara Soares (202504216)
 * @author Gabriela Carneiro (202505760)
 */
public class EnfermariaPsiquiatrica extends Enfermaria {

    /**
     * Período definido para a realização de visitas (ex: "14:00 - 16:00")
     */
    private String horarioVisitas;
    /**
     * Grau de segurança da unidade, representado numericamente (ex: 1-Baixo, 2-Médio, 3-Alto)
     */
    private int nivelSeguranca;

    /**
     * Construtor completo para a Enfermaria Psiquiátrica.
     * @param codigo Identificador único da enfermaria.
     * @param capacidadeCamas Número total de camas disponíveis na unidade.
     * @param horarioVisitas String descritiva do horário permitido para visitas.
     * @param nivelSeguranca Inteiro que define o nível de segurança da enfermaria.
     */
    public EnfermariaPsiquiatrica (String codigo, int capacidadeCamas, String horarioVisitas, int nivelSeguranca){
        super (codigo, capacidadeCamas);//Chama o construtor da classe base (Enfermaria)
        this.horarioVisitas=horarioVisitas;
        this.nivelSeguranca=nivelSeguranca;
    }

    /**
     * Devolve o horário de visitas estabelecido para a unidade de psiquiatria.
     * @return String contendo o intervalo de tempo permitido para visitas.
     */
    public String getHorarioVisitas(){
        return horarioVisitas;
    }
    /**
     * Define ou atualiza o horário de visitas da enfermaria.
     * @param horarioVisitas O novo horário de visitas (ex: "15:00 - 17:00").
     */
    public void setHorarioVisitas(String horarioVisitas){
        this.horarioVisitas=horarioVisitas;
    }
    /**
     * Devolve o nível de segurança configurado para esta enfermaria.
     * @return Um número inteiro representativo do grau de segurança.
     */
    public int getNivelSeguranca(){
        return nivelSeguranca;
    }
    /**
     * Atualiza o nível de segurança da unidade psiquiátrica.
     * @param nivelSeguranca O novo grau de segurança (ex: 1 para baixo, 3 para alto).
     */
    public void setNivelSeguranca(int nivelSeguranca){
        this.nivelSeguranca=nivelSeguranca;
    }
}
