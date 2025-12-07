package com.uab.tq.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.uab.tq.model.Action;
import com.uab.tq.model.Battle;
import com.uab.tq.model.Character;
import com.uab.tq.model.Element;
import com.uab.tq.view.ConsoleView;

public class BattleControllerTest {
    
    @Mock
    private ConsoleView mockView;
    
    @Mock
    private Battle mockBattle;
    
    private BattleController controller;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BattleController(mockView);
    }
    
    @Test
    public void testControllerCreation() {
        assertNotNull(controller);
        assertEquals(mockView, controller.getView());
    }
    
    @Test
    public void testStartBattle() {
        
        // Partició: INICIAR BATALLA AMB DOS PERSONATGES VIUS
        
        Character char1 = new Character("Warrior", 100, 30, 10, 15, Element.FIRE);
        Character char2 = new Character("Mage", 80, 40, 5, 12, Element.WATER);
        
        controller.startBattle(char1, char2);
        
        // Verificar que s'ha creat la batalla
        assertNotNull(controller.getBattle());
        assertEquals(char1, controller.getBattle().getCharacter1());
        assertEquals(char2, controller.getBattle().getCharacter2());
        
        // Verificar que s'ha mostrat l'estat inicial (mock)
        verify(mockView, times(1)).displayBattleStatus(char1, char2);
        
        // Partició: INICIAR BATALLA AMB PERSONATGE MORT
        
        Character deadChar = new Character("Dead", 0, 30, 10, 15);
        Character aliveChar = new Character("Alive", 100, 40, 5, 12);
        
        controller.startBattle(deadChar, aliveChar);
        
        assertNotNull(controller.getBattle());
        assertTrue(controller.getBattle().isFinished());
        
        // Verificar que s'ha mostrat l'estat (inclou batalla ja acabada)
        verify(mockView, times(1)).displayBattleStatus(deadChar, aliveChar);
        
        // Partició: INICIAR BATALLA AMB VALORS FRONTERA
        
        // Health = 1 (mínim viu)
        Character weakChar = new Character("Weak", 1, 10, 5, 10);
        Character normalChar = new Character("Normal", 50, 20, 10, 12);
        
        controller.startBattle(weakChar, normalChar);
        
        assertNotNull(controller.getBattle());
        assertFalse(controller.getBattle().isFinished());
        
        verify(mockView, times(1)).displayBattleStatus(weakChar, normalChar);
    }
    
    // ========== TESTS PER processRound() - COMMIT 24 (RED) ==========
    
    @Test
    public void testProcessRound_SimpleLoop() {
        
        // Partició: PROCESSAR 1 RONDA (LOOP SIMPLE - 1 iteració)
        
        Character char1 = new Character("Hero", 100, 30, 10, 15, Element.FIRE);
        Character char2 = new Character("Enemy", 80, 25, 8, 12, Element.WATER);
        
        when(mockBattle.getCharacter1()).thenReturn(char1);
        when(mockBattle.getCharacter2()).thenReturn(char2);
        when(mockBattle.isFinished()).thenReturn(false);
        
        controller.processRound(mockBattle);
        
        // Verificar que s'ha executat una ronda
        verify(mockBattle, times(1)).executeRound();
        
        // Verificar que s'ha mostrat l'estat després de la ronda
        verify(mockView, times(1)).displayBattleStatus(char1, char2);
    }
    
    @Test
    public void testProcessRound_MultipleRounds() {
        
        // Partició: PROCESSAR MÚLTIPLES RONDES (LOOP SIMPLE - N iteracions)
        
        Character char1 = new Character("Warrior", 100, 30, 10, 15);
        Character char2 = new Character("Mage", 80, 25, 8, 12);
        
        when(mockBattle.getCharacter1()).thenReturn(char1);
        when(mockBattle.getCharacter2()).thenReturn(char2);
        when(mockBattle.isFinished()).thenReturn(false);
        
        // Processar 5 rondes
        for (int i = 0; i < 5; i++) {
            controller.processRound(mockBattle);
        }
        
        // Verificar que s'han executat 5 rondes
        verify(mockBattle, times(5)).executeRound();
        verify(mockView, times(5)).displayBattleStatus(char1, char2);
    }
    
    @Test
    public void testProcessRound_NestedLoops() {
        
        // Partició: NESTED LOOPS - Múltiples batalles amb múltiples rondes cadascuna
        
        Character char1 = new Character("Knight", 100, 30, 10, 15);
        Character char2 = new Character("Wizard", 80, 25, 8, 12);
        
        when(mockBattle.getCharacter1()).thenReturn(char1);
        when(mockBattle.getCharacter2()).thenReturn(char2);
        when(mockBattle.isFinished()).thenReturn(false);
        
        int numBattles = 3;
        int roundsPerBattle = 4;
        
        // LOOP EXTERN: Múltiples batalles
        for (int battle = 0; battle < numBattles; battle++) {
            // LOOP INTERN: Múltiples rondes per batalla
            for (int round = 0; round < roundsPerBattle; round++) {
                controller.processRound(mockBattle);
            }
        }
        
        // Verificar: 3 batalles × 4 rondes = 12 crides
        verify(mockBattle, times(numBattles * roundsPerBattle)).executeRound();
        verify(mockView, times(numBattles * roundsPerBattle)).displayBattleStatus(char1, char2);
    }
    
    @Test
    public void testProcessRound_BattleFinished() {
        
        // Partició: BATALLA JA ACABADA (no s'executa ronda)
        
        Character char1 = new Character("Winner", 50, 30, 10, 15);
        Character char2 = new Character("Loser", 0, 25, 8, 12);
        
        when(mockBattle.getCharacter1()).thenReturn(char1);
        when(mockBattle.getCharacter2()).thenReturn(char2);
        when(mockBattle.isFinished()).thenReturn(true);
        
        controller.processRound(mockBattle);
        
        // No s'ha d'executar la ronda si ja està acabada
        verify(mockBattle, never()).executeRound();
        
        // Però sí s'ha de mostrar l'estat final
        verify(mockView, times(1)).displayBattleStatus(char1, char2);
    }
    
    @Test
    public void testProcessRound_BoundaryZeroRounds() {
        
        // Partició: VALOR FRONTERA - 0 rondes
        
        Character char1 = new Character("Char1", 100, 30, 10, 15);
        Character char2 = new Character("Char2", 80, 25, 8, 12);
        
        when(mockBattle.getCharacter1()).thenReturn(char1);
        when(mockBattle.getCharacter2()).thenReturn(char2);
        when(mockBattle.isFinished()).thenReturn(false);
        
        // No processar cap ronda (loop 0 iteracions)
        for (int i = 0; i < 0; i++) {
            controller.processRound(mockBattle);
        }
        
        // Verificar que no s'ha cridat res
        verify(mockBattle, never()).executeRound();
        verify(mockView, never()).displayBattleStatus(any(), any());
    }
    
    // ========== TESTS PER handleAction() - COMMIT 26 (RED) ==========
    
    // PATH COVERAGE: Path 1 - ATTACK + Target viu
    @Test
    public void testHandleAction_AttackAliveTarget() {
        
        // Partició: ATACAR PERSONATGE VIU
        
        Character source = new Character("Attacker", 100, 30, 10, 15, Element.FIRE);
        Character target = new Character("Defender", 50, 20, 15, 12, Element.WATER);
        Battle battle = new Battle(source, target);
        
        int initialHealth = target.getHealth();
        
        controller.handleAction(Action.ATTACK, source, target, battle);
        
        // Verificar que s'ha executat l'atac
        assertTrue(target.getHealth() < initialHealth);
        
        // Verificar que s'ha mostrat l'atac (mock)
        verify(mockView, times(1)).displayAttack(eq(source), eq(target), anyInt());
    }
    
    // PATH COVERAGE: Path 2 - ATTACK + Target mort
    @Test
    public void testHandleAction_AttackDeadTarget() {
        
        // Partició: ATACAR PERSONATGE MORT (Decision/Condition Coverage)
        
        Character source = new Character("Attacker", 100, 30, 10, 15);
        Character target = new Character("Dead", 0, 20, 15, 12);
        Battle battle = new Battle(source, target);
        
        controller.handleAction(Action.ATTACK, source, target, battle);
        
        // No s'ha d'executar l'atac
        assertEquals(0, target.getHealth());
        
        // No s'ha de mostrar l'atac
        verify(mockView, never()).displayAttack(any(), any(), anyInt());
    }
    
    // PATH COVERAGE: Path 3 - DEFEND + Defense < 50
    @Test
    public void testHandleAction_DefendLowDefense() {
        
        // Partició: DEFENSAR AMB DEFENSA BAIXA (Decision/Condition Coverage)
        
        Character source = new Character("Defender", 100, 20, 30, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        int initialDefense = source.getDefense();
        
        controller.handleAction(Action.DEFEND, source, target, battle);
        
        // Verificar que s'ha aplicat el boost (defense *= 1.5)
        assertEquals((int)(initialDefense * 1.5), source.getDefense());
    }
    
    // PATH COVERAGE: Path 4 - DEFEND + Defense >= 50
    @Test
    public void testHandleAction_DefendHighDefense() {
        
        // Partició: DEFENSAR AMB DEFENSA ALTA (Decision/Condition Coverage)
        
        Character source = new Character("Tank", 100, 20, 60, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        int initialDefense = source.getDefense();
        
        controller.handleAction(Action.DEFEND, source, target, battle);
        
        // No s'ha d'aplicar boost
        assertEquals(initialDefense, source.getDefense());
    }
    
    // PATH COVERAGE: Path 5 - ITEM + Health < 100
    @Test
    public void testHandleAction_ItemLowHealth() {
        
        // Partició: USAR ITEM AMB VIDA BAIXA (Decision/Condition Coverage)
        
        Character source = new Character("Healer", 60, 20, 15, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        int initialHealth = source.getHealth();
        
        controller.handleAction(Action.ITEM, source, target, battle);
        
        // Verificar que s'ha curat (+20 HP, màxim 100)
        assertTrue(source.getHealth() > initialHealth);
        assertTrue(source.getHealth() <= 100);
    }
    
    // PATH COVERAGE: Path 6 - ITEM + Health = 100
    @Test
    public void testHandleAction_ItemFullHealth() {
        
        // Partició: USAR ITEM AMB VIDA PLENA (Decision/Condition Coverage)
        
        Character source = new Character("Healthy", 100, 20, 15, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        controller.handleAction(Action.ITEM, source, target, battle);
        
        // No s'ha de curar
        assertEquals(100, source.getHealth());
    }
    
    // BOUNDARY VALUES
    @Test
    public void testHandleAction_BoundaryDefense49() {
        
        // Valor frontera: defense = 49 (just per sota del límit)
        
        Character source = new Character("BorderDefender", 100, 20, 49, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        controller.handleAction(Action.DEFEND, source, target, battle);
        
        // Ha de rebre boost
        assertEquals((int)(49 * 1.5), source.getDefense());
    }
    
    @Test
    public void testHandleAction_BoundaryDefense50() {
        
        // Valor frontera: defense = 50 (límit exacte)
        
        Character source = new Character("LimitDefender", 100, 20, 50, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        controller.handleAction(Action.DEFEND, source, target, battle);
        
        // NO ha de rebre boost
        assertEquals(50, source.getDefense());
    }
    
    @Test
    public void testHandleAction_BoundaryHealth99() {
        
        // Valor frontera: health = 99 (curació aplicada però no excedeix 100)
        
        Character source = new Character("AlmostFull", 99, 20, 15, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        controller.handleAction(Action.ITEM, source, target, battle);
        
        // Ha de curar fins a 100 (no a 119)
        assertEquals(100, source.getHealth());
    }
    
    @Test
    public void testHandleAction_BoundaryHealth1() {
        
        // Valor frontera: health = 1 (mínim viu)
        
        Character source = new Character("Critical", 1, 30, 10, 15);
        Character target = new Character("Enemy", 80, 25, 10, 12);
        Battle battle = new Battle(source, target);
        
        controller.handleAction(Action.ITEM, source, target, battle);
        
        // Ha de curar
        assertEquals(21, source.getHealth());
    }
    
    // PAIRWISE TESTING + DATA-DRIVEN TESTING
    @Test
    public void testHandleAction_PairwiseCombinations() throws IOException {
        
        // DATA-DRIVEN: Llegir casos de test des de CSV (Pairwise Testing)
        
        String csvFile = "src/test/resources/handleAction_testcases.csv";
        List<TestCase> testCases = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                testCases.add(new TestCase(
                    Action.valueOf(values[0]),
                    Integer.parseInt(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]),
                    values[4]
                ));
            }
        }
        
        // Executar tots els casos de test
        for (TestCase tc : testCases) {
            Character source = new Character("Source", tc.sourceHealth, 20, tc.sourceDefense, 15);
            Character target = new Character("Target", tc.targetHealth, 25, 10, 12);
            Battle battle = new Battle(source, target);
            
            int initialSourceHealth = source.getHealth();
            int initialSourceDefense = source.getDefense();
            int initialTargetHealth = target.getHealth();
            
            controller.handleAction(tc.action, source, target, battle);
            
            // Verificar comportament esperat
            switch (tc.expectedBehavior) {
                case "AttackExecuted":
                    assertTrue(target.getHealth() < initialTargetHealth || target.isDead());
                    break;
                case "NoAttack":
                    assertEquals(initialTargetHealth, target.getHealth());
                    break;
                case "DefenseBoost":
                    assertTrue(source.getDefense() > initialSourceDefense);
                    break;
                case "NoBoost":
                    assertEquals(initialSourceDefense, source.getDefense());
                    break;
                case "HealApplied":
                    assertTrue(source.getHealth() > initialSourceHealth);
                    break;
                case "NoHeal":
                    assertEquals(initialSourceHealth, source.getHealth());
                    break;
            }
        }
        
        // Verificar que s'han processat tots els casos
        assertEquals(12, testCases.size());
    }
    
    // Inner class per Data-Driven Testing
    private static class TestCase {
        Action action;
        int sourceHealth;
        int sourceDefense;
        int targetHealth;
        String expectedBehavior;
        
        TestCase(Action action, int sourceHealth, int sourceDefense, int targetHealth, String expectedBehavior) {
            this.action = action;
            this.sourceHealth = sourceHealth;
            this.sourceDefense = sourceDefense;
            this.targetHealth = targetHealth;
            this.expectedBehavior = expectedBehavior;
        }
    }
}
