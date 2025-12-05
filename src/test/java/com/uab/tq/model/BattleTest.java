package com.uab.tq.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BattleTest {
    
    @Test
    public void testBattleCreation() {
        Character char1 = new Character("Warrior", 100, 30, 10, 15);
        Character char2 = new Character("Mage", 80, 40, 5, 12);
        
        Battle battle = new Battle(char1, char2);
        
        assertEquals(char1, battle.getCharacter1());
        assertEquals(char2, battle.getCharacter2());
    }
    
    @Test
    public void testDetermineTurnOrder() {
        
        // Partició: CHARACTER1 MÉS RÀPID (speed1 > speed2)
        
        Character fastChar = new Character("Fast", 100, 30, 10, 20);
        Character slowChar = new Character("Slow", 100, 30, 10, 10);
        
        Battle battle1 = new Battle(fastChar, slowChar);
        assertEquals(fastChar, battle1.determineTurnOrder());
        
        // Valor frontera: diferència mínima (speed1 = speed2 + 1)
        Character char15 = new Character("Speed15", 100, 30, 10, 15);
        Character char14 = new Character("Speed14", 100, 30, 10, 14);
        
        Battle battle2 = new Battle(char15, char14);
        assertEquals(char15, battle2.determineTurnOrder());
        
        // Partició: CHARACTER2 MÉS RÀPID (speed1 < speed2)
        
        Battle battle3 = new Battle(slowChar, fastChar);
        assertEquals(fastChar, battle3.determineTurnOrder());
        
        // Valor frontera: diferència mínima inversa
        Battle battle4 = new Battle(char14, char15);
        assertEquals(char15, battle4.determineTurnOrder());
        
        // Partició: MATEIXA VELOCITAT (speed1 == speed2, retorn aleatori)
        
        Character char10A = new Character("Twin1", 100, 30, 10, 10);
        Character char10B = new Character("Twin2", 100, 30, 10, 10);
        
        Battle battle5 = new Battle(char10A, char10B);
        Character first = battle5.determineTurnOrder();
        
        // El resultat ha de ser un dels dos
        assertTrue(first == char10A || first == char10B);
        
        // Executar múltiples vegades per verificar aleatorietat
        boolean foundChar1 = false;
        boolean foundChar2 = false;
        
        for (int i = 0; i < 100; i++) {
            Battle tempBattle = new Battle(char10A, char10B);
            Character result = tempBattle.determineTurnOrder();
            if (result == char10A) foundChar1 = true;
            if (result == char10B) foundChar2 = true;
        }
        
        // Amb 100 iteracions, estadísticament ambdós haurien d'aparèixer
        assertTrue(foundChar1 && foundChar2, "L'aleatorietat hauria de retornar ambdós personatges");
    }
}
