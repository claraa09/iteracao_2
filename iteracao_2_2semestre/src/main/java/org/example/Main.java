package org.example;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * Classe principal de entrada (Entry Point) do sistema hospitalar.
 * Esta classe é responsável por orquestrar a inicialização do hospital,
 * gerir o carregamento de dados a partir de ficheiros externos e
 * instanciar a interface de utilizador (Menu).
 * @author Gabriela Carneiro (202505760)
 * @author Clara Soares (202504216)
 */
public class Main {
    // O carregamento "estático" porque os nomes não mudam durante a execução.
    /**
     * Caminho fixo para o ficheiro que contém os dados das enfermarias
     */
    private static final String FICHEIRO_ENFERMARIAS = "Enfermaria.csv";
    /**
     * Caminho fixo para o ficheiro que contém os dados dos episódios de internamento
     */
    private static final String FICHEIRO_EPISODIOS = "Episodios.csv";
    /**
     * Nome do ficheiro destinado ao registo histórico (log) de erros e eventos do sistema
     */
    private static final String FILE_LOG = "hospital_sistema.log";

    public static void main(String[] args) throws Exception {
        System.out.println(">>> [SISTEMA] A iniciar o programa...");

        Hospital meuHospital = GestorDados.carregarEstado();

        if (meuHospital == null) {
            System.out.println(">>> [SISTEMA] Sessão não encontrada. A importar dados dos ficheiros CSV...");
            meuHospital = new Hospital("Hospital Central");

            PrintWriter logWriter = new PrintWriter(new FileWriter(FILE_LOG, true));
            meuHospital.setEnfermarias(LeitorEnfermarias.lerEnfermarias(FICHEIRO_ENFERMARIAS, logWriter));
            LeitorEpisodios.lerEpisodios(FICHEIRO_EPISODIOS, meuHospital, logWriter);
            logWriter.close();

            System.out.println(">>> [SISTEMA] Ficheiros CSV importados com sucesso!");

        } else {
            System.out.println(">>> [SISTEMA] Dados da sessão anterior restaurados com sucesso!");
        }

        Menu menuPrincipal = new Menu(meuHospital);
        menuPrincipal.exibir();

    }
}