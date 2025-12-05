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
    
    /**
     * Determina qui ataca primer basant-se en la velocitat
     * 
     * @return El personatge que ataca primer
     */
    public Character determineTurnOrder() {
        int speed1 = character1.getSpeed();
        int speed2 = character2.getSpeed();
        
        if (speed1 > speed2) {
            return character1;
        } else if (speed1 < speed2) {
            return character2;
        } else {
            // Mateixa velocitat: decisió aleatòria
            return Math.random() < 0.5 ? character1 : character2;
        }
    }
    
    /**
     * Executa un torn de combat
     * 
     * Path Coverage (5 paths):
     * - PATH 1: Attacker viu + Defender viu + Defender sobreviu
     * - PATH 2: Attacker viu + Defender viu + Defender mor
     * - PATH 3: Attacker mort → early return
     * - PATH 4: Attacker viu + Defender mort → early return
     * - PATH 5: Ambdós morts → early return
     */
    public void executeTurn() {
        Character attacker = determineTurnOrder();
        Character defender = (attacker == character1) ? character2 : character1;
        
        // PATH 3: Attacker mort abans d'atacar
        if (attacker.isDead()) {
            return;
        }
        
        // PATH 4: Defender ja mort
        if (defender.isDead()) {
            return;
        }
        
        // PATH 1 i 2: Atac normal
        attacker.attack(defender);
    }
}
