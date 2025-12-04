package com.uab.tq.model;

/**
 * Enum Element - Model (MVC)
 * 
 * Representa els tipus elementals del joc amb les seves interaccions
 * Triangle de força: FIRE > GRASS > WATER > FIRE
 * 
 * VERSIÓ 1: Enum amb mètodes isStrongAgainst() i isWeakAgainst()
 */
public enum Element {
    FIRE,
    WATER,
    GRASS;
    
    /**
     * Comprova si aquest element és fort contra un altre
     * 
     * @param other L'element contra el qual es compara
     * @return true si aquest element té avantatge, false altrament
     */
    public boolean isStrongAgainst(Element other) {
        if (this == FIRE && other == GRASS) {
            return true;
        }
        if (this == WATER && other == FIRE) {
            return true;
        }
        if (this == GRASS && other == WATER) {
            return true;
        }
        return false;
    }
    
    /**
     * Comprova si aquest element és dèbil contra un altre
     * 
     * @param other L'element contra el qual es compara
     * @return true si aquest element té desavantatge, false altrament
     */
    public boolean isWeakAgainst(Element other) {
        // Un element és dèbil contra un altre si l'altre és fort contra ell
        return other.isStrongAgainst(this);
    }
}
