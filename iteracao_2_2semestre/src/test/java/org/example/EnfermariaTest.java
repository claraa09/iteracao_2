package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários para a Classe Enfermaria")
public class EnfermariaTest {

    // Criação de referências para as classes que vamos testar em isolamento
    private Enfermaria enfermariaMedica;
    private LocalDate dataReferencia;

    @BeforeEach
    void setUp() {
        // 1. Executa antes de cada teste para garantir que o estado está limpo
        // Criação de uma enfermaria concreta (ex: Código "E1" e capacidade de 10 camas)
        enfermariaMedica = new EnfermariaCI("E1", 10, "14:00-16:00", 1013.25, 1013.25);

        // Data fixa para a simulação
        dataReferencia = LocalDate.of(2026, 5, 30);

        // 2. Adicionar episódios controlados para os cálculos matemáticos baterem certo
        Episodio ep1 = new Episodio(1, LocalDate.of(2026, 5, 25), LocalDate.of(2026, 5, 30) );

        Episodio ep2 = new Episodio(2, LocalDate.of(2026, 5, 30), LocalDate.of(2026, 6, 5));

        Episodio ep3 = new Episodio(3, LocalDate.of(2026, 5, 20), LocalDate.of(2026, 5, 30));
        ep3.setDataAlta(LocalDate.of(2026, 5, 30));

        // Adição dos episódios à enfermaria de teste
        enfermariaMedica.getEpisodios().add(ep1);
        enfermariaMedica.getEpisodios().add(ep2);
        enfermariaMedica.getEpisodios().add(ep3);
    }

    @Test
    @DisplayName("Deve calcular corretamente a taxa de ocupação para um dia específico")
    void testCalcularTaxaOcupacao() {
        // No dia 30/05/2026, temos 3 camas ocupadas (ep1, ep2, ep3 ainda contam nesse dia)
        // 3 camas ocupadas em 10 totais = 30.0% de ocupação
        double taxaEsperada = 30.0;
        double taxaObtida = enfermariaMedica.calcularTaxaOcupacao(dataReferencia);

        assertEquals(taxaEsperada, taxaObtida, 0.01, "O cálculo da taxa de ocupação falhou.");
    }

    @Test
    @DisplayName("Deve calcular o Score de Turnover corretamente baseado no fluxo de doentes")
    void testCalcularScoreTurnover() {
        int scoreEsperado = 3;
        int scoreObtido = enfermariaMedica.calcularScoreTurnover(dataReferencia);

        assertEquals(scoreEsperado, scoreObtido, "O score de turnover não condiz com a tabela do RF6.");
    }

    @Test
    @DisplayName("Deve validar que a enfermaria não está em situação de pressão crítica (<85%)")
    void testEmPressaoFalso() {
        // Com 30% de ocupação, o metodo emPressao() tem de devolver obrigatoriamente FALSE
        boolean emPressao = enfermariaMedica.emPressao(dataReferencia);

        assertFalse(emPressao, "A enfermaria não deveria estar em pressão com apenas 30% de ocupação.");
    }

    @Test
    @DisplayName("Deve detetar pressão crítica quando a ocupação ultrapassa os 85%")
    void testEmPressaoVerdadeiro() {
        // Situação de stress adicionando doentes fictícios até encher as camas
        for (int i = 4; i <= 9; i++) {
            enfermariaMedica.getEpisodios().add(new Episodio(i, LocalDate.of(2026, 5, 28), LocalDate.of(2026, 5, 30)));
        }

        // 9 camas ocupadas em 10 totais = 90.0% (> 85%)
        boolean emPressao = enfermariaMedica.emPressao(dataReferencia);

        assertTrue(emPressao, "O sistema falhou em detetar a pressão crítica acima dos 85%.");
    }
}