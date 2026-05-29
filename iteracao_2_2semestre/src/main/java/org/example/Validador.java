package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validador {
    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean isInteiro (String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        try {
            Integer.parseInt (texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public static boolean isDecimal (String texto) {
        if (texto== null || texto.trim().isEmpty()) return false;
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isData (String texto) {
        if (texto== null || texto.trim().isEmpty()) return false;
        try {
            LocalDate.parse(texto.trim(), FORMATO);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valida se uma string tem o formato de horário "HH:MM - HH:MM".
     * Garante horas entre 00 e 23, e minutos entre 00 e 59.
     */
    public static boolean isHorario(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;

        // Padrão Regex: Aceita "14:00 - 16:00" ou "09:30-10:45"
        String padrao = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]\\s*-\\s*([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

        return texto.trim().matches(padrao);
    }
}
