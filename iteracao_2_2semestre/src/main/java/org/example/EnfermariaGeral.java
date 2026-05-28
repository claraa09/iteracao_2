package org.example;

import java.util.List;

/**
 * Representa uma enfermaria de cuidados gerais no sistema hospitalar.
 * Esta classe estende a funcionalidade da classe abstrata Enfermaria,
 * acrescentando informações sobre acompanhantes e recursos técnicos disponíveis.
 * @author Clara Soares (202504216)
 * @author Gabriela Carneiro (202505760)
 */
public class EnfermariaGeral extends Enfermaria {

    /**
     * Número máximo de acompanhantes permitidos por paciente
     */
    private int numAcompanhantes;
    /**
     * Lista de recursos materiais ou técnicos disponíveis na unidade (ex: Oxigénio, Monitor)
     */
    private List<String> recursosDisponiveis;

    /**
     * Construtor completo para a Enfermaria Geral.
     * * @param codigo Identificador único da enfermaria.
     * @param capacidadeCamas Número total de camas disponíveis.
     * @param numAcompanhantes Número de acompanhantes permitidos.
     * @param recursosDisponiveis Lista de strings com os recursos da unidade.
     */
    public EnfermariaGeral (String codigo, int capacidadeCamas, int numAcompanhantes, List<String> recursosDisponiveis){
        super (codigo, capacidadeCamas);//Chama o construtor da classe base (Enfermaria)
        this.numAcompanhantes=numAcompanhantes;
        this.recursosDisponiveis=recursosDisponiveis;
    }

    /**
     * Devolve o número de acompanhantes permitidos para os pacientes desta enfermaria.
     * @return O número de acompanhantes por paciente.
     */
    public int getnumAcompanhantes(){
        return numAcompanhantes;
    }
    /**
     * Atualiza o limite de acompanhantes permitidos na unidade.
     * @param numAcompanhantes O novo número máximo de acompanhantes.
     */
    public void setNumAcompanhantes(int numAcompanhantes){
        this.numAcompanhantes=numAcompanhantes;
    }
    /**
     * Devolve a lista de recursos técnicos e materiais disponíveis nesta enfermaria.
     * @return Uma List de Strings contendo os recursos (ex: "Oxigénio", "Ventilador").
     */
    public List<String> getRecursosDisponiveis(){
        return recursosDisponiveis;
    }
    /**
     * Substitui a lista de recursos disponíveis por uma nova listagem.
     * @param recursosDisponiveis A nova lista de recursos a atribuir à enfermaria.
     */
    public void setRecursosDisponiveis(List<String> recursosDisponiveis){this.recursosDisponiveis= recursosDisponiveis;}
}