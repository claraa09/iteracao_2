import java.io.FileWriter;
import java.io.PrintWriter;

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
        // Inicialização do Hospital
        Hospital meuHospital = new Hospital("Hospital Central");

        // Abre o PrintWritter com FileWriter(FILE_LOG, true) para não apagar o histórico anterior
        PrintWriter logWriter = new PrintWriter(new FileWriter(FILE_LOG, true));

        // Utilização das constantes para o carregamento automático
        System.out.println(">>> [SISTEMA] A carregar ficheiros...");
        meuHospital.setEnfermarias(leitorEnfermarias.lerEnfermarias(FICHEIRO_ENFERMARIAS, logWriter));
        leitorEpisodios.lerEpisodios("Episodios.csv", meuHospital, logWriter);
        logWriter.close();


        // O aviso de sucesso antes de entrar no menu
        System.out.println(">>> [SISTEMA] Ficheiros carregados com sucesso!");

        // Transição direta para o Menu interativo
        Menu menuPrincipal = new Menu(meuHospital);
        menuPrincipal.exibir();
    }
}