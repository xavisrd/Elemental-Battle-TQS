package com.uab.tq.view;

import com.uab.tq.model.Character;

/**
 * Vista per mostrar informació de la batalla a la consola
 */
public class ConsoleView {
    
    /**
     * Mostra l'estat de la batalla
     * 
     * @param character1 Primer personatge
     * @param character2 Segon personatge
     */
    public void displayBattleStatus(Character character1, Character character2) {
        System.out.println("=== ESTAT DE LA BATALLA ===");
        System.out.println(character1.getName() + ": " + character1.getHealth() + " HP");
        System.out.println(character2.getName() + ": " + character2.getHealth() + " HP");
        System.out.println("===========================");
    }
    
    /**
     * Mostra el resultat d'un atac
     * 
     * @param attacker Personatge atacant
     * @param defender Personatge defensor
     * @param damage Dany causat
     */
    public void displayAttack(Character attacker, Character defender, int damage) {
        System.out.println(attacker.getName() + " ataca " + defender.getName() + " i causa " + damage + " de dany!");
    }
    
    /**
     * Mostra el guanyador de la batalla
     * 
     * @param winner Personatge guanyador
     */
    public void displayWinner(Character winner) {
        System.out.println("==========================");
        System.out.println("🏆 " + winner.getName() + " ha guanyat la batalla!");
        System.out.println("==========================");
    }
}
