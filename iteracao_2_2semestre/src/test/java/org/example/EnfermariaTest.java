package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

public class EnfermariaTest {

    // Criação de referências para as classes que vamos testar em isolamento
    private Enfermaria enfermariaMedica;
    private LocalDate dataReferencia;

    @BeforeAll
    public static void initAll() {
        System.out.println("====== INÍCIO DA BATERIA DE TESTES: CLASSE ENFERMARIA ======");
    }

    @AfterAll
    public static void tearDownAll() {
        System.out.println("====== FIM DA BATERIA DE TESTES: CLASSE ENFERMARIA ======");
    }

    @BeforeEach
    public void setUp() {
        // Criação de uma enfermaria concreta
        enfermariaMedica = new EnfermariaCI("E1", 10, "14:00-16:00", 1013.25, 1013.25);

        // Data fixa para a simulação
        dataReferencia = LocalDate.of(2026, 5, 30);

        // Adição de episódios
        Episodio ep1 = new Episodio(1, LocalDate.of(2026, 5, 25), LocalDate.of(2026, 5, 30) );
        Episodio ep2 = new Episodio(2, LocalDate.of(2026, 5, 30), LocalDate.of(2026, 6, 5));
        Episodio ep3 = new Episodio(3, LocalDate.of(2026, 5, 20), LocalDate.of(2026, 5, 30));

        // Adição dos episódios à enfermaria de teste
        enfermariaMedica.getEpisodios().add(ep1);
        enfermariaMedica.getEpisodios().add(ep2);
        enfermariaMedica.getEpisodios().add(ep3);
    }

    @Test
    public void testCalcularTaxaOcupacao() {
        double taxaObtida = enfermariaMedica.calcularTaxaOcupacao(dataReferencia);

        assertEquals(30.0, taxaObtida, 0.01, "O cálculo da taxa de ocupação falhou.");
    }

    @Test
    public void testCalcularScoreTurnover() {
        int scoreObtido = enfermariaMedica.calcularScoreTurnover(dataReferencia);

        assertEquals(3, scoreObtido, "O score de turnover não condiz com a tabela do RF6.");
    }

    @Test
    public void testEmPressao() {
        // Com 30% de ocupação, o metodo emPressao() tem de devolver obrigatoriamente FALSE
        boolean emPressao = enfermariaMedica.emPressao(dataReferencia);

        assertFalse(emPressao, "A enfermaria não deveria estar em pressão com apenas 30% de ocupação.");
    }

    @Test
    public void testEstatisticasInternamento() {
        assertEquals(5, enfermariaMedica.determinarMinimo(), "Tempo mínimo de internamento errado.");
        assertEquals(10, enfermariaMedica.determinarMaximo(), "Tempo máximo de internamento errado.");
        assertEquals(7.0, enfermariaMedica.determinarMedia(), 0.01, "Média de internamento errada.");
        assertEquals(2.16, enfermariaMedica.determinarDesvioPadrao(), 0.01, "Desvio padrão errado.");
    }

    @Test
    public void testCalcularScoreOcupacao() {
        assertEquals(1, enfermariaMedica.calcularScoreOcupacao(dataReferencia));
    }

    @Test
    public void testCalcularIndicePressao() {
        assertEquals(1.6, enfermariaMedica.calcularIndicePressao(dataReferencia), 0.01);
    }

    @Test
    public void testObterBarraASCII() {
        String barra = enfermariaMedica.obterBarraASCII(30.0, '#');
        assertNotNull(barra);
        assertEquals(15, barra.length(), "A barra ASCII deveria conter exatamente 15 caracteres.");
    }

    @Test
    public void testCapacidadeComAssumption() {
        assumeTrue(enfermariaMedica.getCapacidadeCamas() > 0);

        enfermariaMedica.setCapacidadeCamas(12);
        assertEquals(12, enfermariaMedica.getCapacidadeCamas());
    }
}