package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HospitalTest {

    @Test
    void adicionarEnfermaria() {
        Hospital hospital = new Hospital("Hospital de Teste");
        EnfermariaCI enf = new EnfermariaCI("E1", 10, "14:00-16:00", 1013.25, 1013.25);

        // Testa se a lista começa vazia
        assertEquals(0, hospital.getEnfermarias().size(), "A lista deveria começar vazia.");

        // Adiciona e valida se o tamanho da lista aumentou
        hospital.getEnfermarias().add(enf);
        assertEquals(1, hospital.getEnfermarias().size(), "A enfermaria não foi adicionada corretamente.");
        assertEquals("E1", hospital.getEnfermarias().get(0).getCodigo(), "O código da enfermaria não bate certo.");
    }

    @Test
    void pesquisarEnfermaria() {
        // Preparação do cenário
        Hospital hospital = new Hospital("Hospital de Teste");
        EnfermariaCI enf1 = new EnfermariaCI("E1", 10, "14:00-16:00", 1013.25, 1013.25);
        EnfermariaCI enf2 = new EnfermariaCI("E2", 15, "16:00-18:00", 1013.25, 1013.25);

        hospital.adicionarEnfermaria(enf1);
        hospital.adicionarEnfermaria(enf2);

        // Execução da ação
        Enfermaria encontrada = hospital.pesquisarEnfermaria("   E2   ");

        // Verificação do resultado final
        assertNotNull(encontrada, "A enfermaria deveria ter sido encontrada.");
        assertEquals(15, encontrada.getCapacidadeCamas(), "Encontrou a enfermaria errada.");
        assertEquals("E2", encontrada.getCodigo(), "O código da enfermaria não bate certo.");
    }

    @Test
    void alterarCamasTotais() {
        // Testa a simulação da variação súbita de camas
        List<Enfermaria> lista = new ArrayList<>();
        EnfermariaCI enf = new EnfermariaCI("E1", 10, "14:00-16:00", 1013.25, 1013.25);
        lista.add(enf);

        // Executa o metodo estático - aumento de 20% sobre 10 camas = 12
        Hospital.alterarCamasTotais(lista, 20.0);

        assertEquals(12, enf.getCapacidadeCamas(), "O cálculo do aumento súbito de camas falhou.");
    }

    @Test
    void calcularPercentagemEnfermariasEmPressao() {

        List<Enfermaria> lista = new ArrayList<>();
        LocalDate dataRef = LocalDate.of(2026, 5, 30);

        // Enfermaria 1: Ocupação total (100% -> >85%, está em pressão)
        EnfermariaCI enf1 = new EnfermariaCI("E1", 1, "14:00-16:00", 1013.25, 1013.25);
        enf1.getEpisodios().add(new Episodio(1, dataRef, dataRef.plusDays(2)));

        // Enfermaria 2: Vazia (0% -> não está em pressão)
        EnfermariaCI enf2 = new EnfermariaCI("E2", 5, "15:00-17:00", 1013.25, 1013.25);

        lista.add(enf1);
        lista.add(enf2);

        // 1 de 2 enfermarias em pressão = 50.0%
        double percentagemObtida = Hospital.calcularPercentagemEnfermariasEmPressao(lista, dataRef);
        assertEquals(50.0, percentagemObtida, 0.01, "O cálculo da percentagem global de pressão falhou.");
    }

    @Test
    void ordenarEnfermariasPorIndice() {

        List<Enfermaria> lista = new ArrayList<>();
        LocalDate dataRef = LocalDate.of(2026, 5, 30);

        EnfermariaCI enf1 = new EnfermariaCI("E1", 10, "14:00-16:00", 1013.25, 1013.25); // Ocupação baixa
        EnfermariaCI enf2 = new EnfermariaCI("E2", 2, "15:00-17:00", 1013.25, 1013.25);  // Ocupação alta

        enf2.getEpisodios().add(new Episodio(1, dataRef, dataRef.plusDays(5)));
        enf2.getEpisodios().add(new Episodio(2, dataRef, dataRef.plusDays(5)));

        lista.add(enf1);
        lista.add(enf2);

        // Antes da ordenação: [E1, E2]
        Hospital.ordenarEnfermariasPorIndice(lista, dataRef);
        // Após ordenação (Decrescente): Deve ficar [E2, E1] porque E2 tem mais pressão

        assertEquals("E2", lista.get(0).getCodigo(), "A ordenação decrescente falhou: E2 devia estar em primeiro.");
    }

    @Test
    void ordenarEnfermarias() {

        List<Enfermaria> lista = new ArrayList<>();
        EnfermariaCI enf1 = new EnfermariaCI("E1", 10, "14:00", 1013.25, 1013.25);
        EnfermariaCI enf2 = new EnfermariaCI("E2", 10, "15:00", 1013.25, 1013.25);

        lista.add(enf1);
        lista.add(enf2);

        lista.sort((e1, e2) -> e1.getCodigo().compareTo(e2.getCodigo()));

        assertEquals("E1", lista.get(0).getCodigo(), "A ordenação falhou.");
    }
}