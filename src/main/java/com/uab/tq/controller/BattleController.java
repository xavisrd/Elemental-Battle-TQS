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
}
