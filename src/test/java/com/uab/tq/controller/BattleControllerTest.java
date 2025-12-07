package com.uab.tq.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}

