package com.uab.tq;

import java.util.Scanner;

import com.uab.tq.controller.BattleController;
import com.uab.tq.model.Action;
import com.uab.tq.model.Battle;
import com.uab.tq.model.Character;
import com.uab.tq.model.Element;
import com.uab.tq.view.ConsoleView;

/**
 * Classe principal per executar el simulador de batalles
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static ConsoleView view = new ConsoleView();
    private static BattleController controller = new BattleController(view);
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    SIMULADOR DE BATALLES ELEMENTAL     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("Selecciona el mode de joc:");
        System.out.println("1. Batalla Ràpida (Demo Automàtica)");
        System.out.println("2. Jugador vs Enemic (Interactiu)");
        System.out.print("Tria [1-2]: ");
        
        int mode = getIntInput(1, 2);
        System.out.println();
        
        if (mode == 1) {
            quickBattle();
        } else {
            playerBattle();
        }
        
        scanner.close();
    }
    
    /**
     * Mode 1: Demostració automàtica d'una batalla
     */
    private static void quickBattle() {
        System.out.println("MODE BATALLA RÀPIDA - Demo Automàtica");
        System.out.println();
        
        // Crear personatges predefinits amb stats balancejats
        Character hero = new Character("Blaze", 100, 40, 10, 15, Element.FIRE);
        Character enemy = new Character("Aqua", 100, 35, 12, 13, Element.WATER);
        
        System.out.println("Blaze (FOC) vs Aqua (AIGUA)");
        System.out.println("Avantatge elemental: AIGUA > FOC");
        System.out.println();
        
        // Iniciar batalla
        controller.startBattle(hero, enemy);
        Battle battle = controller.getBattle();
        
        // Executar rondes automàtiques
        int round = 1;
        while (!battle.isFinished() && round <= 20) {
            System.out.println("\n--- RONDA " + round + " ---");
            controller.processRound(battle);
            
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            round++;
        }
        
        // Mostrar guanyador
        System.out.println();
        if (hero.getHealth() <= 0) {
            view.displayWinner(enemy);
        } else {
            view.displayWinner(hero);
        }
    }
    
    /**
     * Mode 2: Batalla interactiva amb accions del jugador
     */
    private static void playerBattle() {
        System.out.println("MODE JUGADOR vs ENEMIC - Interactiu");
        System.out.println();
        
        // Crear el personatge del jugador
        System.out.println("CREA EL TEU PERSONATGE:");
        System.out.print("Introdueix el nom: ");
        String playerName = scanner.nextLine();
        
        System.out.println("\nTria l'element:");
        System.out.println("1. FOC (Fort vs HERBA, Dèbil vs AIGUA)");
        System.out.println("2. AIGUA (Fort vs FOC, Dèbil vs HERBA)");
        System.out.println("3. HERBA (Fort vs AIGUA, Dèbil vs FOC)");
        System.out.print("Tria [1-3]: ");
        
        Element playerElement = getElement(getIntInput(1, 3));
        
        Character player = new Character(playerName, 120, 35, 10, 15, playerElement);
        
        // Crear l'enemic amb avantatge elemental
        Element enemyElement = getCounterElement(playerElement);
        Character enemy = new Character("Shadow", 120, 30, 12, 13, enemyElement);
        
        System.out.println("\nL'enemic té element " + enemyElement + " (element contrari!)");
        System.out.println();
        
        // Iniciar batalla
        controller.startBattle(player, enemy);
        Battle battle = controller.getBattle();
        
        // Loop de combat interactiu
        int round = 1;
        while (!battle.isFinished()) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           RONDA " + round + "                    ║");
            System.out.println("╚══════════════════════════════════════╝");
            
            // Torn del jugador
            System.out.println("\nEL TEU TORN:");
            System.out.println("1. ATACAR - Fer dany a l'enemic");
            System.out.println("2. DEFENSAR - Augmentar defensa x1.5 (si DEF < 50)");
            System.out.println("3. OBJECTE - Curar +20 HP (si HP < 100)");
            System.out.print("Tria l'acció [1-3]: ");
            
            Action playerAction = getAction(getIntInput(1, 3));
            controller.handleAction(playerAction, player, enemy, battle);
            
            if (battle.isFinished()) {
                break;
            }
            
            // Torn de l'enemic (IA simple)
            System.out.println("\nTORN DE L'ENEMIC:");
            Action enemyAction = simpleAI(enemy);
            System.out.println("L'enemic usa " + enemyAction + "!");
            controller.handleAction(enemyAction, enemy, player, battle);
            
            if (battle.isFinished()) {
                break;
            }
            
            // Mostrar estat després de la ronda
            System.out.println("\n========================================");
            System.out.println("ESTAT DE LA BATALLA:");
            System.out.println(player.getName() + " [" + player.getHealth() + " HP] vs " 
                             + enemy.getName() + " [" + enemy.getHealth() + " HP]");
            System.out.println("========================================");
            
            System.out.println("\nPrem ENTER per continuar...");
            scanner.nextLine();
            
            round++;
        }
        
        // Mostrar guanyador
        System.out.println();
        if (player.getHealth() <= 0) {
            System.out.println("DERROTA! " + enemy.getName() + " guanya!");
        } else {
            System.out.println("VICTÒRIA! " + player.getName() + " guanya!");
        }
    }
    
    /**
     * IA simple per l'enemic
     */
    private static Action simpleAI(Character enemy) {
        // Si té poca vida i pot curar-se, usar ITEM
        if (enemy.getHealth() < 40) {
            return Action.ITEM;
        }
        
        // Si té poca defensa, defensar-se ocasionalment
        if (enemy.getDefense() < 40 && Math.random() < 0.2) {
            return Action.DEFEND;
        }
        
        // Altrament, atacar
        return Action.ATTACK;
    }
    
    /**
     * Validació d'entrada d'enters
     */
    private static int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.print("Entrada invàlida. Tria [" + min + "-" + max + "]: ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada invàlida. Tria [" + min + "-" + max + "]: ");
            }
        }
    }
    
    /**
     * Converteix int a Element
     */
    private static Element getElement(int choice) {
        switch (choice) {
            case 1: return Element.FIRE;
            case 2: return Element.WATER;
            case 3: return Element.GRASS;
            default: return Element.FIRE;
        }
    }
    
    /**
     * Retorna l'element contrari (per l'enemic)
     */
    private static Element getCounterElement(Element element) {
        switch (element) {
            case FIRE: return Element.WATER;
            case WATER: return Element.GRASS;
            case GRASS: return Element.FIRE;
            default: return Element.FIRE;
        }
    }
    
    /**
     * Converteix int a Action
     */
    private static Action getAction(int choice) {
        switch (choice) {
            case 1: return Action.ATTACK;
            case 2: return Action.DEFEND;
            case 3: return Action.ITEM;
            default: return Action.ATTACK;
        }
    }
}
