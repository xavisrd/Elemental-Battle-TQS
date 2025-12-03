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
}