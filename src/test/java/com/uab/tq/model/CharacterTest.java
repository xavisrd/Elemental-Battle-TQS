package com.uab.tq.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests per a la classe Character (Model - MVC)
 * 
 * Aquesta és la versió 1 dels tests (TDD - Test First)
 * Objectiu: Verificar la creació bàsica d'un personatge amb nom
 */
public class CharacterTest {

    /**
     * TEST 1 - TDD VERSION 1
     * Cas de prova: Creació d'un personatge amb nom vàlid
     * 
     * CAIXA NEGRA - Partició equivalent: Nom vàlid (String no buit)
     * 
     * Precondició: Cap
     * Acció: Crear Character amb nom "Warrior"
     * Resultat esperat: getName() retorna "Warrior"
     */
    @Test
    public void testCreateCharacterWithName() {
        // Arrange & Act
        Character character = new Character("Warrior");
        
        // Assert
        assertEquals("Warrior", character.getName());
    }
    
    /**
     * TEST 2 - TDD VERSION 1
     * Cas de prova: Obtenir el nom d'un personatge
     * 
     * CAIXA NEGRA - Partició equivalent: Nom amb caràcters especials
     * 
     * Precondició: Character creat
     * Acció: Cridar getName()
     * Resultat esperat: Retorna el mateix nom proporcionat
     */
    @Test
    public void testGetName() {
        // Arrange
        Character mage = new Character("Mage");
        
        // Act
        String name = mage.getName();
        
        // Assert
        assertNotNull(name);
        assertEquals("Mage", name);
    }
    
    /**
     * TEST 3 - TDD VERSION 2
     * Cas de prova: Creació de personatge amb atributs de combat
     * 
     * Es proven les particions equivalents i valors límit dels paràmetres 
     * health, attack, defense i speed del constructor.
     */
    @Test
    public void testCharacterStats() {
        
        // Partició equivalent: Atributs positius vàlids (cas normal)
        Character warrior = new Character("Warrior", 100, 20, 10, 15);
        assertEquals("Warrior", warrior.getName());
        assertEquals(100, warrior.getHealth());
        assertEquals(20, warrior.getAttack());
        assertEquals(10, warrior.getDefense());
        assertEquals(15, warrior.getSpeed());

        // Partició: Tots els atributs negatius (cas extrem)
        Character allNegative = new Character("Invalid", -10, -5, -3, -1);
        assertEquals(0, allNegative.getHealth());
        assertEquals(0, allNegative.getAttack());
        assertEquals(0, allNegative.getDefense());
        assertEquals(0, allNegative.getSpeed());
        
        // PROVES PER AL PARÀMETRE HEALTH
        
        // Valor límit: Health = 0 (mínim permès)
        Character deadChar = new Character("Dead", 0, 10, 5, 5);
        assertEquals(0, deadChar.getHealth());
        
        // Valor límit: Health = 1 (cas frontera - just sobre mínim)
        Character almostDead = new Character("Almost", 1, 10, 5, 5);
        assertEquals(1, almostDead.getHealth());
        
        // Valor límit: Health molt alt (cas normal alt)
        Character tank = new Character("Tank", 9999, 5, 50, 2);
        assertEquals(9999, tank.getHealth());
        
        // Valor límit: Health negatiu -> ajustat a 0
        Character negativeHealth = new Character("Invalid", -10, 10, 5, 5);
        assertEquals(0, negativeHealth.getHealth());
        
        // PROVES PER AL PARÀMETRE ATTACK
        
        // Valor límit: Attack = 0 (mínim permès)
        Character noAttack = new Character("Pacifist", 50, 0, 10, 5);
        assertEquals(0, noAttack.getAttack());
        
        // Valor límit: Attack = 1 (cas frontera)
        Character minAttack = new Character("Weak1", 50, 1, 10, 5);
        assertEquals(1, minAttack.getAttack());
        
        // Valor límit: Attack negatiu -> ajustat a 0
        Character weakChar = new Character("Weak", 50, -5, 10, 5);
        assertEquals(0, weakChar.getAttack());
        
        // PROVES PER AL PARÀMETRE DEFENSE
        
        // Valor límit: Defense = 0 (mínim permès)
        Character glassCannonZero = new Character("GlassZero", 50, 20, 0, 10);
        assertEquals(0, glassCannonZero.getDefense());
        
        // Valor límit: Defense = 1 (cas frontera)
        Character minDefense = new Character("GlassOne", 50, 20, 1, 10);
        assertEquals(1, minDefense.getDefense());
        
        // Valor límit: Defense negatiu -> ajustat a 0
        Character noDefense = new Character("Glass", 50, 20, -3, 10);
        assertEquals(0, noDefense.getDefense());
        
        // PROVES PER AL PARÀMETRE SPEED
        
        // Valor límit: Speed = 0 (mínim permès)
        Character slowChar = new Character("Slow", 50, 10, 10, 0);
        assertEquals(0, slowChar.getSpeed());
        
        // Valor límit: Speed = 1 (cas frontera)
        Character minSpeed = new Character("SlowOne", 50, 10, 10, 1);
        assertEquals(1, minSpeed.getSpeed());
        
        // Valor límit: Speed negatiu -> ajustat a 0
        Character negativeSpeed = new Character("Frozen", 50, 10, 10, -2);
        assertEquals(0, negativeSpeed.getSpeed());
    }
    
    /**
     * TEST 4 - TDD VERSION 3
     * Cas de prova: Creació de personatge amb element
     * 
     * CAIXA NEGRA - Particions equivalents del paràmetre element:
     *   - Personatge amb element FIRE
     *   - Personatge amb element WATER
     *   - Personatge amb element GRASS
     *   - Personatge sense element (null - constructor sense element)
     */
    @Test
    public void testCharacterWithElement() {
        
        // Partició: Personatge amb element FIRE
        Character fireChar = new Character("Blaze", 100, 25, 10, 15, Element.FIRE);
        assertEquals("Blaze", fireChar.getName());
        assertEquals(Element.FIRE, fireChar.getElement());
        
        // Partició: Personatge amb element WATER
        Character waterChar = new Character("Aqua", 100, 20, 15, 12, Element.WATER);
        assertEquals(Element.WATER, waterChar.getElement());
        
        // Partició: Personatge amb element GRASS
        Character grassChar = new Character("Leaf", 100, 22, 12, 18, Element.GRASS);
        assertEquals(Element.GRASS, grassChar.getElement());
        
        // Partició: Personatge sense element (constructor sense paràmetre element)
        Character noElement = new Character("Neutral", 100, 20, 10, 15);
        assertNull(noElement.getElement());
    }
    
    /**
     * TEST 5 - TDD VERSION 1
     * Cas de prova: Càlcul de damage bàsic entre personatges
     * 
     * CAIXA NEGRA - Particions equivalents del paràmetre attack vs defense:
     *   - Attack > Defense (damage positiu)
     *   - Attack < Defense (damage = 0)
     *   - Attack = Defense (damage = 0)
     * 
     * Fórmula bàsica: damage = Math.max(0, attack - defense)
     */
    @Test
    public void testCalculateDamageBasic() {
        
        // Partició: Attack > Defense (damage positiu)
        
        Character attacker1 = new Character("Warrior", 100, 30, 10, 15);
        Character defender1 = new Character("Mage", 100, 10, 15, 10);
        assertEquals(15, attacker1.calculateDamage(defender1)); // 30 - 15 = 15
        
        Character attacker2 = new Character("Knight", 100, 50, 10, 12);
        Character defender2 = new Character("Archer", 100, 10, 20, 18);
        assertEquals(30, attacker2.calculateDamage(defender2)); // 50 - 20 = 30
        
        Character attacker3 = new Character("Berserker", 100, 40, 10, 10);
        Character defender3 = new Character("Rogue", 100, 10, 5, 20);
        assertEquals(35, attacker3.calculateDamage(defender3)); // 40 - 5 = 35
        
        // Partició: Attack < Defense (damage = 0, no pot ser negatiu)
        
        Character weakAttacker1 = new Character("Weak1", 100, 5, 10, 15);
        Character strongDefender1 = new Character("Tank1", 100, 10, 20, 10);
        assertEquals(0, weakAttacker1.calculateDamage(strongDefender1)); // 5 - 20 = -15 -> 0
        
        Character weakAttacker2 = new Character("Weak2", 100, 10, 10, 15);
        Character strongDefender2 = new Character("Tank2", 100, 10, 30, 10);
        assertEquals(0, weakAttacker2.calculateDamage(strongDefender2)); // 10 - 30 = -20 -> 0
        
        // Partició: Attack = Defense (damage = 0)
        
        Character equal1 = new Character("Equal1", 100, 15, 10, 15);
        Character equal2 = new Character("Equal2", 100, 10, 15, 10);
        assertEquals(0, equal1.calculateDamage(equal2)); // 15 - 15 = 0
        
        Character equal3 = new Character("Equal3", 100, 25, 10, 12);
        Character equal4 = new Character("Equal4", 100, 10, 25, 14);
        assertEquals(0, equal3.calculateDamage(equal4)); // 25 - 25 = 0
        
        // Valors límit
        
        // Valor límit: Attack = 0 (mínim)
        Character noAttacker = new Character("NoAttack", 100, 0, 10, 15);
        assertEquals(0, noAttacker.calculateDamage(defender1)); // 0 - 15 = -15 -> 0
        
        // Valor límit: Defense = 0 (mínim)
        Character noDefender = new Character("NoDefense", 100, 10, 0, 10);
        assertEquals(30, attacker1.calculateDamage(noDefender)); // 30 - 0 = 30
        
        // Valor límit: Attack = 1 (cas frontera)
        Character minAttacker = new Character("MinAttack", 100, 1, 10, 15);
        assertEquals(0, minAttacker.calculateDamage(defender1)); // 1 - 15 = -14 -> 0
        
        // Valor límit: Defense = 1 (cas frontera)  
        Character minDefender = new Character("MinDefense", 100, 10, 1, 10);
        assertEquals(29, attacker1.calculateDamage(minDefender)); // 30 - 1 = 29
    }
    
    /**
     * TEST 6 - TDD VERSION 2
     * Cas de prova: Càlcul de damage amb multiplicadors d'elements
     * 
     * CAIXA NEGRA - Particions equivalents segons relació d'elements:
     *   - Atacant amb avantatge elemental (element fort) -> damage x1.5
     *   - Atacant amb desavantatge elemental (element dèbil) -> damage x0.5
     *   - Sense relació elemental (mateix element, sense elements, o neutral) -> damage x1.0
     */
    @Test
    public void testCalculateDamageWithElements() {
        
        // Partició: AVANTATGE ELEMENTAL (damage x1.5)
        
        // FIRE vs GRASS (fort)
        Character fireChar = new Character("Blaze", 100, 30, 10, 15, Element.FIRE);
        Character grassChar = new Character("Leaf", 100, 10, 10, 12, Element.GRASS);
        assertEquals(30, fireChar.calculateDamage(grassChar)); // (30-10) * 1.5 = 30
        
        // WATER vs FIRE (fort)
        Character waterChar = new Character("Aqua", 100, 40, 10, 15, Element.WATER);
        Character fireChar2 = new Character("Ember", 100, 10, 15, 12, Element.FIRE);
        assertEquals(38, waterChar.calculateDamage(fireChar2)); // (40-15) * 1.5 = 37.5 -> 38 (rounded)
        
        // GRASS vs WATER (fort)
        Character grassChar2 = new Character("Vine", 100, 20, 10, 18, Element.GRASS);
        Character waterChar2 = new Character("Wave", 100, 10, 5, 10, Element.WATER);
        assertEquals(23, grassChar2.calculateDamage(waterChar2)); // (20-5) * 1.5 = 22.5 -> 23 (rounded)
        
        // Partició: DESAVANTATGE ELEMENTAL (damage x0.5)
        
        // FIRE vs WATER (dèbil)
        assertEquals(13, fireChar.calculateDamage(waterChar2)); // (30-5) * 0.5 = 12.5 -> 13 (rounded)
        
        // WATER vs GRASS (dèbil)
        assertEquals(15, waterChar.calculateDamage(grassChar)); // (40-10) * 0.5 = 15
        
        // GRASS vs FIRE (dèbil)
        assertEquals(5, grassChar2.calculateDamage(fireChar)); // (20-10) * 0.5 = 5
        
        // Partició: NEUTRAL (damage x1.0, sense multiplicador)
        
        // Mateix element (FIRE vs FIRE)
        Character fireChar3 = new Character("Inferno", 100, 25, 10, 14, Element.FIRE);
        assertEquals(15, fireChar3.calculateDamage(fireChar)); // (25-10) * 1.0 = 15
        
        // Atacant sense element
        Character noElementAttacker = new Character("Neutral1", 100, 30, 10, 15);
        assertEquals(20, noElementAttacker.calculateDamage(fireChar)); // (30-10) * 1.0 = 20
        
        // Defensor sense element
        Character noElementDefender = new Character("Neutral2", 100, 10, 10, 15);
        assertEquals(20, fireChar.calculateDamage(noElementDefender)); // (30-10) * 1.0 = 20
        
        // Ambdós sense element
        Character noElement1 = new Character("Plain1", 100, 20, 10, 15);
        Character noElement2 = new Character("Plain2", 100, 10, 8, 12);
        assertEquals(12, noElement1.calculateDamage(noElement2)); // (20-8) * 1.0 = 12
    }
    
    @Test
    public void testReceiveDamage() {
        
        // Partició: DANY POSITIU (health disminueix)
        
        Character char1 = new Character("Test", 100, 10, 10, 10);
        char1.receiveDamage(30);
        assertEquals(70, char1.getHealth()); // 100 - 30 = 70
        
        char1.receiveDamage(20);
        assertEquals(50, char1.getHealth()); // 70 - 20 = 50
        
        // Valor frontera: dany igual a health
        char1.receiveDamage(50);
        assertEquals(0, char1.getHealth()); // 50 - 50 = 0
        
        // Partició: DANY MAJOR QUE HEALTH (health no pot ser negatiu)
        
        Character char2 = new Character("Test2", 50, 10, 10, 10);
        char2.receiveDamage(100);
        assertEquals(0, char2.getHealth()); // 50 - 100 = -50 → 0
        
        char2.receiveDamage(20); // Ja està mort, segueix a 0
        assertEquals(0, char2.getHealth());
        
        // Partició: DANY ZERO (no afecta health)
        
        Character char3 = new Character("Test3", 80, 10, 10, 10);
        char3.receiveDamage(0);
        assertEquals(80, char3.getHealth());
        
        // Partició: DANY NEGATIU (no afecta health)
        
        char3.receiveDamage(-10);
        assertEquals(80, char3.getHealth());
    }
    
    @Test
    public void testIsDead() {
        
        // Partició: HEALTH POSITIU (viu)
        
        Character char1 = new Character("Alive", 100, 10, 10, 10);
        assertFalse(char1.isDead());
        
        char1.receiveDamage(50);
        assertFalse(char1.isDead()); // health = 50
        
        // Valor frontera: health = 1
        char1.receiveDamage(49);
        assertFalse(char1.isDead()); // health = 1
        
        // Partició: HEALTH ZERO (mort)
        
        char1.receiveDamage(1);
        assertTrue(char1.isDead()); // health = 0
        
        // Partició: HEALTH NEGATIU (mort, però forçat a 0)
        
        Character char2 = new Character("Dead", 30, 10, 10, 10);
        char2.receiveDamage(100);
        assertTrue(char2.isDead()); // health = 0
    }
}