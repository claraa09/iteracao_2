package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária responsável pela validação e conversão segura de dados de entrada.
 * Contém métodos estáticos para verificar tipos numéricos, formatos de data e padrões de horário.
 * * @author Clara Soares (202504216)
 * @author Gabriela Carneiro (202505760)
 */

public class Validador {

    /**
     * Formatador padrão para exibição e leitura estrita de datas no formato dd/MM/yyyy.
     */
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Verifica se uma String pode ser convertida num número inteiro válido.
     * * @param texto A String a ser validada.
     * @return true se o texto for um inteiro válido, false caso contrário ou se for nulo/vazio.
     */
    public static boolean isInteiro (String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        try {
            Integer.parseInt (texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * Verifica se uma String pode ser convertida num número decimal (double) válido.
     * * @param texto A String a ser validada.
     * @return true se o texto for um decimal válido, false caso contrário ou se for nulo/vazio.
     */
    public static boolean isDecimal (String texto) {
        if (texto== null || texto.trim().isEmpty()) return false;
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida se uma String representa uma data válida no calendário gregoriano.
     * Utiliza o método de conversão segura para garantir a resiliência a espaços.
     * * @param texto A String contendo a data a validar.
     * @return true se a data for logicamente válida, false se falhar o formato ou não existir.
     */
    public static boolean isData (String texto) {
        if (texto== null || texto.trim().isEmpty()) return false;
        try {
            converterDataSegura(texto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Corta a String de entrada pelas barras, limpa espaços em branco internos de
     * cada componente (dia, mês, ano) e reconstrói o objeto LocalDate.
     * Evita exceções de parsing causadas por digitação incorreta do utilizador.
     * * @param texto A String da data com ou sem espaços adicionais (ex: "12 / 03 / 2024").
     * @return O objeto {@link LocalDate} correspondente à data parametrizada.
     * @throws IllegalArgumentException Se a estrutura não contiver exatamente três componentes.
     * @throws NumberFormatException Se os valores cortados não forem números inteiros válidos.
     * @throws java.time.DateTimeException Se os números inseridos não formarem uma data válida no calendário (ex: 30 de fevereiro).
     */
    public static LocalDate converterDataSegura(String texto) {
        String[] partes = texto.split("/");

        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato de data incompleto.");
        }

        // O trim() limpa os espaços individuais de cada bloco isolado
        int dia = Integer.parseInt(partes[0].trim());
        int mes = Integer.parseInt(partes[1].trim());
        int ano = Integer.parseInt(partes[2].trim());

        // Cria o objeto LocalDate (valida automaticamente se o dia/mês existem no calendário)
        return LocalDate.of(ano, mes, dia);
    }

    /**
     * Valida se uma String obedece ao padrão de intervalo de horário "HH:MM - HH:MM".
     * Permite flexibilidade de minutos opcionais e suporta a marcação limite de até 24:00.
     * * @param texto A String do horário a ser validada (ex: "10 - 17", "08:30 - 24:00").
     * @return true se o formato e os valores das horas forem válidos, false caso contrário.
     */
    public static boolean isHorario(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;

        // Padrão Regex: Aceita "14:00 - 16:00" ou "09:30-10:45"
        String padrao = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\\s*-\\s*([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

        return texto.trim().matches(padrao);
    }
}
