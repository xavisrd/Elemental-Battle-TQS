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
    
    @Test
    public void testExecuteTurn() {
        
        // Partició: AMBDÓS PERSONATGES VIUS (atac normal)
        
        // PATH 1: Atac normal, defender sobreviu
        Character warrior = new Character("Warrior", 100, 30, 10, 15, Element.FIRE);
        Character mage = new Character("Mage", 100, 20, 5, 12, Element.WATER);
        
        Battle battle1 = new Battle(warrior, mage);
        int initialHealth = mage.getHealth();
        
        battle1.executeTurn();
        
        // Warrior més ràpid, ataca primer
        assertTrue(mage.getHealth() < initialHealth, "El defensor hauria de rebre dany");
        assertFalse(mage.isDead(), "El defensor hauria de seguir viu");
        
        // PATH 2: Atac letal, defender mor
        Character strongAttacker = new Character("Strong", 100, 50, 10, 15);
        Character weakDefender = new Character("Weak", 20, 10, 5, 10);
        
        Battle battle2 = new Battle(strongAttacker, weakDefender);
        battle2.executeTurn();
        
        assertTrue(weakDefender.isDead(), "El defensor hauria de morir");
        assertEquals(0, weakDefender.getHealth());
        
        // Partició: ATTACKER MORT (no ataca)
        
        // PATH 3: Attacker ja mort abans del torn
        Character deadAttacker = new Character("Dead", 0, 30, 10, 20);
        Character aliveDefender = new Character("Alive", 100, 20, 5, 10);
        
        Battle battle3 = new Battle(deadAttacker, aliveDefender);
        int defenderHealthBefore = aliveDefender.getHealth();
        
        battle3.executeTurn();
        
        assertEquals(defenderHealthBefore, aliveDefender.getHealth(), "Defender no hauria de rebre dany");
        
        // Partició: DEFENDER MORT (no rep dany addicional)
        
        // PATH 4: Defender ja mort abans del torn
        Character attacker = new Character("Attacker", 100, 30, 10, 15);
        Character deadDefender = new Character("DeadDefender", 0, 20, 5, 12);
        
        Battle battle4 = new Battle(attacker, deadDefender);
        
        battle4.executeTurn();
        
        assertEquals(0, deadDefender.getHealth(), "Defender mort segueix a 0");
        
        // Partició: AMBDÓS MORTS (no passa res)
        
        // PATH 5: Cap personatge pot actuar
        Character dead1 = new Character("Dead1", 0, 30, 10, 15);
        Character dead2 = new Character("Dead2", 0, 20, 5, 12);
        
        Battle battle5 = new Battle(dead1, dead2);
        
        battle5.executeTurn();
        
        assertEquals(0, dead1.getHealth());
        assertEquals(0, dead2.getHealth());
    }
    
    @Test
    public void testIsFinished() {
        
        // Partició: AMBDÓS VIUS (batalla continua)
        
        Character char1 = new Character("Warrior", 100, 30, 10, 15);
        Character char2 = new Character("Mage", 80, 40, 5, 12);
        
        Battle battle1 = new Battle(char1, char2);
        assertFalse(battle1.isFinished());
        
        // Partició: CHARACTER1 MORT (batalla acabada)
        
        Character deadChar1 = new Character("Dead1", 0, 30, 10, 15);
        Character aliveChar1 = new Character("Alive1", 80, 40, 5, 12);
        
        Battle battle2 = new Battle(deadChar1, aliveChar1);
        assertTrue(battle2.isFinished());
        
        // Partició: CHARACTER2 MORT (batalla acabada)
        
        Character aliveChar2 = new Character("Alive2", 100, 30, 10, 15);
        Character deadChar2 = new Character("Dead2", 0, 40, 5, 12);
        
        Battle battle3 = new Battle(aliveChar2, deadChar2);
        assertTrue(battle3.isFinished());
        
        // Partició: AMBDÓS MORTS (batalla acabada)
        
        Character dead1 = new Character("Dead1", 0, 30, 10, 15);
        Character dead2 = new Character("Dead2", 0, 40, 5, 12);
        
        Battle battle4 = new Battle(dead1, dead2);
        assertTrue(battle4.isFinished());
    }
    
    @Test
    public void testExecuteRound() {
        
        // Partició: BATALLA AMB MÚLTIPLES TORNS (loop testing)
        
        Character warrior = new Character("Warrior", 100, 25, 10, 15);
        Character mage = new Character("Mage", 100, 25, 10, 12);
        
        Battle battle1 = new Battle(warrior, mage);
        
        assertFalse(battle1.isFinished());
        
        // Executar una ronda amb múltiples torns fins que algú mori
        battle1.executeRound();
        
        assertTrue(battle1.isFinished(), "La batalla hauria d'acabar després de la ronda");
        assertTrue(warrior.isDead() || mage.isDead(), "Un personatge hauria de morir");
        
        // Verificar que el guanyador està viu
        if (warrior.isDead()) {
            assertFalse(mage.isDead(), "El mage hauria de ser el guanyador");
            assertTrue(mage.getHealth() > 0);
        } else {
            assertFalse(warrior.isDead(), "El warrior hauria de ser el guanyador");
            assertTrue(warrior.getHealth() > 0);
        }
        
        // Partició: BATALLA JA ACABADA (no executa torns)
        
        Character deadChar = new Character("Dead", 0, 30, 10, 15);
        Character aliveChar = new Character("Alive", 100, 40, 5, 12);
        
        Battle battle2 = new Battle(deadChar, aliveChar);
        int healthBefore = aliveChar.getHealth();
        
        battle2.executeRound();
        
        assertEquals(healthBefore, aliveChar.getHealth(), "No hauria d'executar cap torn");
        assertTrue(battle2.isFinished());
        
        // Partició: BATALLA RÀPIDA (un sol torn letal)
        
        Character strongAttacker = new Character("Strong", 100, 100, 10, 20);
        Character weakDefender = new Character("Weak", 10, 10, 5, 10);
        
        Battle battle3 = new Battle(strongAttacker, weakDefender);
        
        battle3.executeRound();
        
        assertTrue(battle3.isFinished());
        assertTrue(weakDefender.isDead());
        assertFalse(strongAttacker.isDead());
    }
}
