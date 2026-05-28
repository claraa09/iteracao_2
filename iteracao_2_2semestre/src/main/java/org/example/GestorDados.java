package org.example;

import java.io.*;

public class GestorDados {
    private static final String FICHEIRO_BINARIO = "hospital_estado.dat";

    /**
     * Serializa (Grava) o estado completo do Hospital no disco.
     */
    public static void guardarEstado(Hospital hospital) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHEIRO_BINARIO))) {
            oos.writeObject(hospital);
            System.out.println(">>> [SISTEMA] Dados guardados com sucesso (" + FICHEIRO_BINARIO + ").");
        } catch (IOException e) {
            System.err.println("Erro ao guardar os dados: " + e.getMessage());
        }
    }

    /**
     * Deserializa (Lê) o estado do Hospital do disco.
     */
    public static Hospital carregarEstado() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHEIRO_BINARIO))) {
            Hospital hospital = (Hospital) ois.readObject();
            System.out.println(">>> [SISTEMA] Sessão anterior restaurada com sucesso.");
            return hospital;
        } catch (IOException | ClassNotFoundException e) {
            return null; // Devolve null se o ficheiro não existir (ex: primeira vez a correr)
        }
    }
}