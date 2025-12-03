package com.uab.tq.model;

/**
 * Classe Character - Model (MVC)
 * Representa un personatge de combat
 * 
 * VERSIÓ 1: Constructor + getName()
 */
public class Character {
    
    private String name;
    
    public Character(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
