package com.uab.tq.model;

/**
 * Classe Character - Model (MVC)
 * Representa un personatge de combat amb atributs
 * 
 * VERSIÓ 2: Constructor amb stats + getters
 * Validació: Els atributs negatius s'ajusten a 0
 */
public class Character {
    
    private String name;
    private int health;
    private int attack;
    private int defense;
    private int speed;
    
    /**
     * Constructor simple amb nom (versió 1)
     */
    public Character(String name) {
        this.name = name;
    }
    
    /**
     * Constructor amb atributs de combat (versió 2)
     * 
     * @param name Nom del personatge
     * @param health Punts de vida (si negatiu, s'ajusta a 0)
     * @param attack Punts d'atac (si negatiu, s'ajusta a 0)
     * @param defense Punts de defensa (si negatiu, s'ajusta a 0)
     * @param speed Punts de velocitat (si negatiu, s'ajusta a 0)
     */
    public Character(String name, int health, int attack, int defense, int speed) {
        this.name = name;
        // Validació: no permetre valors negatius
        this.health = Math.max(0, health);
        this.attack = Math.max(0, attack);
        this.defense = Math.max(0, defense);
        this.speed = Math.max(0, speed);
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getHealth() {
        return this.health;
    }
    
    public int getAttack() {
        return this.attack;
    }
    
    public int getDefense() {
        return this.defense;
    }
    
    public int getSpeed() {
        return this.speed;
    }
}
