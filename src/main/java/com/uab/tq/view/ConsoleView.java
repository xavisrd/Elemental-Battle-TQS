package com.uab.tq.view;

import com.uab.tq.model.Character;

/**
 * Vista per mostrar informació de la batalla a la consola
 * Versió 2: Refactoritzada amb templates de missatges
 */
public class ConsoleView {
    
    // Templates de missatges
    private static final String BATTLE_STATUS_TEMPLATE = "%s [%d HP] vs %s [%d HP]";
    private static final String ATTACK_TEMPLATE = "%s attacks %s for %d damage!";
    private static final String WINNER_TEMPLATE = "%s wins the battle!";
    private static final String SEPARATOR = "========================================";
    
    /**
     * Mostra l'estat de la batalla amb format consistent
     * Format: "Character1 [HP] vs Character2 [HP]"
     * 
     * @param character1 Primer personatge
     * @param character2 Segon personatge
     */
    public void displayBattleStatus(Character character1, Character character2) {
        String status = String.format(BATTLE_STATUS_TEMPLATE,
            character1.getName(), character1.getHealth(),
            character2.getName(), character2.getHealth());
        
        System.out.println(SEPARATOR);
        System.out.println("BATTLE STATUS:");
        System.out.println(status);
        System.out.println(SEPARATOR);
    }
    
    /**
     * Mostra el resultat d'un atac amb format consistent
     * Format: "Attacker attacks Defender for X damage!"
     * 
     * @param attacker Personatge atacant
     * @param defender Personatge defensor
     * @param damage Dany causat
     */
    public void displayAttack(Character attacker, Character defender, int damage) {
        String attackMessage = String.format(ATTACK_TEMPLATE,
            attacker.getName(), defender.getName(), damage);
        
        System.out.println(attackMessage);
    }
    
    /**
     * Mostra el guanyador de la batalla amb format consistent
     * Format: "Winner wins the battle!"
     * 
     * @param winner Personatge guanyador
     */
    public void displayWinner(Character winner) {
        String winnerMessage = String.format(WINNER_TEMPLATE, winner.getName());
        
        System.out.println(SEPARATOR);
        System.out.println("🏆 VICTORY! 🏆");
        System.out.println(winnerMessage);
        System.out.println(SEPARATOR);
    }
}
