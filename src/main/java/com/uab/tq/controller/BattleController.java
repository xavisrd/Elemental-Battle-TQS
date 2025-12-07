package com.uab.tq.controller;

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
}
