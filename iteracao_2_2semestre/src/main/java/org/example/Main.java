package org.example;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Classe principal de entrada (Entry Point) do sistema hospitalar.
 * Orquestra a inicialização da aplicação: tenta recuperar o estado de uma
 * sessão anterior (Serialização) ou importa a configuração base via CSV.
 *
 * @author Clara Soares (202504216)
 * @author Gabriela Carneiro (202505760)
 */
public class Main {

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

    /**
     * Metodo principal que arranca a execução do software.
     * @param args Argumentos de linha de comandos (não utilizados).
     * @throws Exception Propagação genérica de exceções críticas de E/S.
     */
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