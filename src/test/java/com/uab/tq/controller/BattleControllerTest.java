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
}
