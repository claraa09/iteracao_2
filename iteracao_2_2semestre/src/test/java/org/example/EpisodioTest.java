package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class EpisodioTest {

    @Test
    public void testCalcularLosComSucesso() {
        // Criar um episódio com 5 dias de internamento (Admissão: 10/05, Alta: 15/05)
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
        LocalDate altaInvalida = LocalDate.of(2026, 5, 10); // Alta antes da admissão!

        // Verifica se o construtor lança a exceção que corrigimos antes
        assertThrows(IllegalArgumentException.class, () -> {
            new Episodio(103, admissao, altaInvalida);
        }, "Deveria ter lançado erro porque a data de alta é anterior à admissão.");
    }
}