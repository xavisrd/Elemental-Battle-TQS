package com.uab.tq.model;

/**
 * Representa una batalla entre dos personatges
 */
public class Battle {
    
    private Character character1;
    private Character character2;
    
    /**
     * Constructor de la batalla
     * 
     * @param character1 Primer personatge
     * @param character2 Segon personatge
     */
    public Battle(Character character1, Character character2) {
        this.character1 = character1;
        this.character2 = character2;
    }
    
    public Character getCharacter1() {
        return character1;
    }
    
    public Character getCharacter2() {
        return character2;
    }
}
