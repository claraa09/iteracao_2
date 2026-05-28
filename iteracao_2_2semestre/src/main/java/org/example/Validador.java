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
        if (texto!= null || texto.trim().isEmpty()) return false;
        try {
            Double.parseDouble(texto.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isData (String texto) {
        if (texto!= null || texto.trim().isEmpty()) return false;
        try {
            LocalDate.parse(texto.trim(), FORMATO);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
