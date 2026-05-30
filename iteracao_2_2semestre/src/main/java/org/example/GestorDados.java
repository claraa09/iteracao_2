package org.example;

import java.io.*;

/**
 * Classe utilitária responsável pela persistência binária do estado do sistema.
 * Implementa a serialização e desserialização do objeto principal (Hospital)
 * e todas as suas dependências em memória.
 *
 * @author Clara Soares (202504216)
 * @author Gabriela Carneiro (202505760)
 */
public class GestorDados {
    private static final String FICHEIRO_BINARIO = "hospital_estado.dat";

    /**
     * Serializa o estado completo do Hospital guardando-o num ficheiro binário local.
     * @param hospital O objeto raiz {@link Hospital} a ser guardado.
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
     * Desserializa o estado do Hospital a partir do ficheiro binário local.
     * @return O objeto {@link Hospital} recuperado, ou null se o ficheiro não existir ou falhar a leitura.
     */
    public static Hospital carregarEstado() {
        File f = new File(FICHEIRO_BINARIO);
        if (!f.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Hospital) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}