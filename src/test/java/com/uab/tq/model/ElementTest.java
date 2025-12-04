package com.uab.tq.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests per a l'enum Element (Model - MVC)
 * 
 * Prova el sistema de tipus elementals i les seves interaccions
 * Triangle de debilitats: FIRE > GRASS > WATER > FIRE
 */
public class ElementTest {

    /**
     * TEST 1 - TDD VERSION 1
     * Cas de prova: Verificar que existeixen els tres elements
     * 
     * CAIXA NEGRA - Partició equivalent: Cada tipus d'element existent
     */
    @Test
    public void testElementValues() {
        // Verifiquem que els tres elements existeixen
        assertNotNull(Element.FIRE);
        assertNotNull(Element.WATER);
        assertNotNull(Element.GRASS);
        
        // Verifiquem que són diferents
        assertNotEquals(Element.FIRE, Element.WATER);
        assertNotEquals(Element.WATER, Element.GRASS);
        assertNotEquals(Element.GRASS, Element.FIRE);
    }
    
    /**
     * TEST 2 - TDD VERSION 1
     * Cas de prova: Verificar avantatges de tipus
     * 
     * CAIXA NEGRA - Particions equivalents del mètode isStrongAgainst():
     *   - Element fort contra un altre (retorna true)
     *   - Element dèbil contra un altre (retorna false)
     *   - Element neutral / mateix tipus (retorna false)
     * 
     * Triangle de força: FIRE > GRASS, GRASS > WATER, WATER > FIRE
     */
    @Test
    public void testIsStrongAgainst() {
        
        // PROVES PER A FIRE
        
        // FIRE és fort contra GRASS
        assertTrue(Element.FIRE.isStrongAgainst(Element.GRASS));
        
        // FIRE NO és fort contra WATER
        assertFalse(Element.FIRE.isStrongAgainst(Element.WATER));
        
        // FIRE NO és fort contra ell mateix
        assertFalse(Element.FIRE.isStrongAgainst(Element.FIRE));
        
        // PROVES PER A WATER
        
        // WATER és fort contra FIRE
        assertTrue(Element.WATER.isStrongAgainst(Element.FIRE));
        
        // WATER NO és fort contra GRASS
        assertFalse(Element.WATER.isStrongAgainst(Element.GRASS));
        
        // WATER NO és fort contra ell mateix
        assertFalse(Element.WATER.isStrongAgainst(Element.WATER));
        
        // PROVES PER A GRASS
        
        // GRASS és fort contra WATER
        assertTrue(Element.GRASS.isStrongAgainst(Element.WATER));
        
        // GRASS NO és fort contra FIRE
        assertFalse(Element.GRASS.isStrongAgainst(Element.FIRE));
        
        // GRASS NO és fort contra ell mateix
        assertFalse(Element.GRASS.isStrongAgainst(Element.GRASS));
    }
    
    /**
     * TEST 3 - TDD VERSION 1
     * Cas de prova: Verificar debilitats de tipus
     * 
     * CAIXA NEGRA - Particions equivalents del mètode isWeakAgainst():
     *   - Element dèbil contra un altre (retorna true)
     *   - Element fort contra un altre (retorna false)
     *   - Element neutral / mateix tipus (retorna false)
     * 
     * Triangle de debilitat: FIRE < WATER, WATER < GRASS, GRASS < FIRE
     */
    @Test
    public void testIsWeakAgainst() {
        
        // PROVES PER A FIRE
        
        // FIRE és dèbil contra WATER
        assertTrue(Element.FIRE.isWeakAgainst(Element.WATER));
        
        // FIRE NO és dèbil contra GRASS
        assertFalse(Element.FIRE.isWeakAgainst(Element.GRASS));
        
        // FIRE NO és dèbil contra ell mateix
        assertFalse(Element.FIRE.isWeakAgainst(Element.FIRE));
        
        // PROVES PER A WATER
        
        // WATER és dèbil contra GRASS
        assertTrue(Element.WATER.isWeakAgainst(Element.GRASS));
        
        // WATER NO és dèbil contra FIRE
        assertFalse(Element.WATER.isWeakAgainst(Element.FIRE));
        
        // WATER NO és dèbil contra ell mateix
        assertFalse(Element.WATER.isWeakAgainst(Element.WATER));
        
        // PROVES PER A GRASS
        
        // GRASS és dèbil contra FIRE
        assertTrue(Element.GRASS.isWeakAgainst(Element.FIRE));
        
        // GRASS NO és dèbil contra WATER
        assertFalse(Element.GRASS.isWeakAgainst(Element.WATER));
        
        // GRASS NO és dèbil contra ell mateix
        assertFalse(Element.GRASS.isWeakAgainst(Element.GRASS));
    }
}
