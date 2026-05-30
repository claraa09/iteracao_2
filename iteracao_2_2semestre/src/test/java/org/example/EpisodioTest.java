package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class EpisodioTest {

    private LocalDate dataBase;

    @BeforeAll
    public static void initAll() {
        System.out.println("====== INÍCIO DA BATERIA DE TESTES: CLASSE EPISODIO ======");
    }

    @AfterAll
    public static void tearDownAll() {
        System.out.println("====== FIM DA BATERIA DE TESTES: CLASSE EPISODIO ======");
    }

    @BeforeEach
    public void setUp() {
        // Inicializa uma data de referência limpa antes de cada teste individual
        dataBase = LocalDate.of(2026, 5, 30);
    }

    @Test
    public void testCalcularLosComSucesso() {
        // Criação um episódio com 5 dias de internamento (Admissão: 10/05, Alta: 15/05)
        LocalDate admissao = LocalDate.of(2026, 5, 10);
        LocalDate alta = LocalDate.of(2026, 5, 15);
        Episodio episodio = new Episodio(101, admissao, alta);

        // O resultado esperado de dias entre as datas é 5
        assertEquals(5, episodio.calcularLos(), "O cálculo de dias de internamento (LoS) falhou.");
    }

    @Test
    public void testCalcularLosPacienteAindaInternado() {
        // Se o paciente ainda está internado, a data de alta é null. O LoS deve ser 0.
        Episodio episodio = new Episodio(102, LocalDate.now(), null);
        assertEquals(0, episodio.calcularLos(), "Paciente sem alta deve ter LoS igual a 0.");
    }

    @Test
    public void testDataAltaAnteriorAdmissaoDeveLancarExcecao() {
        LocalDate admissao = LocalDate.of(2026, 5, 15);
        LocalDate alta = LocalDate.of(2026, 5, 10); // Alta antes da admissão!

        // Verifica se o construtor lança a exceção
        boolean lancouErro = false;

        try {
            new Episodio(103, admissao, alta);
        } catch (IllegalArgumentException e) {
            lancouErro = true;
        }

        // Validação final usando o assertTrue
        assertTrue(lancouErro, "O construtor devia ter lançado IllegalArgumentException.");
    }

    @Test
    public void testCompareTo() {
        Episodio epAntigo = new Episodio(1, dataBase.minusDays(5), null);
        Episodio epNovo = new Episodio(2, dataBase, null);

        // O mais antigo veio antes, ou seja, deve retornar valor negativo
        assertTrue(epAntigo.compareTo(epNovo) < 0, "O episódio antigo deveria vir primeiro.");
        // O mesmo objeto comparado consigo mesmo, ou seja, deve retornar 0
        assertEquals(0, epAntigo.compareTo(epAntigo));
    }

    @Test
    public void testCalcularLosComAssumption() {
        Episodio episodio = new Episodio(101, dataBase.minusDays(5), dataBase);

        // Suposição: "Só quero validar a matemática se a cama tiver um ID válido"
        assumeTrue(episodio.getIdCama() > 0);

        // Se a suposição passar, o JUnit faz a asserção normal
        assertEquals(5, episodio.calcularLos());
    }
}