package com.uab.tq.controller;

import com.uab.tq.model.Action;
import com.uab.tq.model.Battle;
import com.uab.tq.model.Character;
import com.uab.tq.view.ConsoleView;

/**
 * Controlador que gestiona la lògica de la batalla
 */
public class BattleController {
    
    private Battle battle;
    private ConsoleView view;
    
    /**
     * Constructor del controlador
     * 
     * @param view Vista per mostrar informació
     */
    public BattleController(ConsoleView view) {
        this.view = view;
    }
    
    public Battle getBattle() {
        return battle;
    }
    
    public ConsoleView getView() {
        return view;
    }
    
    /**
     * Inicia una nova batalla entre dos personatges
     * 
     * @param character1 Primer personatge
     * @param character2 Segon personatge
     */
    public void startBattle(Character character1, Character character2) {
        this.battle = new Battle(character1, character2);
        view.displayBattleStatus(character1, character2);
    }
    
    /**
     * Processa una ronda de batalla
     * Executa una ronda completa i mostra l'estat resultant
     * 
     * @param battle Batalla a processar
     */
    public void processRound(Battle battle) {
        // Si la batalla ja està acabada, només mostrem l'estat
        if (!battle.isFinished()) {
            battle.executeRound();
        }
        
        // Mostrar l'estat després de la ronda
        view.displayBattleStatus(battle.getCharacter1(), battle.getCharacter2());
    }
    
    /**
     * Gestiona una acció del jugador en combat
     * 
     * @param action Acció a executar (ATTACK, DEFEND, ITEM)
     * @param source Personatge que executa l'acció
     * @param target Personatge objectiu de l'acció
     * @param battle Batalla actual
     */
    public void handleAction(Action action, Character source, Character target, Battle battle) {
        switch (action) {
            case ATTACK:
                // PATH 1 i 2: ATTACK + Target viu/mort (Decision/Condition Coverage)
                if (!target.isDead()) {
                    int damage = source.calculateDamage(target);
                    source.attack(target);
                    view.displayAttack(source, target, damage);
                }
                break;
                
            case DEFEND:
                // PATH 3 i 4: DEFEND + Defense <50 / >=50 (Decision/Condition Coverage)
                if (source.getDefense() < 50) {
                    // Boost temporal de defensa (×1.5)
                    int newDefense = (int)(source.getDefense() * 1.5);
                    // Necessitem un setter per modificar defense
                    source.setDefense(newDefense);
                }
                break;
                
            case ITEM:
                // PATH 5 i 6: ITEM + Health <100 / =100 (Decision/Condition Coverage)
                if (source.getHealth() < 100) {
                    // Curació de +20 HP, màxim 100
                    int newHealth = Math.min(source.getHealth() + 20, 100);
                    source.setHealth(newHealth);
                }
                break;
        }
    }
}
