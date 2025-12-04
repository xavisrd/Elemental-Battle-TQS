package com.uab.tq.model;

/**
 * Classe Character - Model (MVC)
 * Representa un personatge de combat amb atributs
 * 
 * VERSIÓ 4: Afegit mètode calculateDamage()
 * Validació: Els atributs negatius s'ajusten a 0
 */
public class Character {
    
    private String name;
    private int health;
    private int attack;
    private int defense;
    private int speed;
    private Element element;
    
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
        this.element = null; // Per defecte sense element
    }
    
    /**
     * Constructor amb atributs de combat i element (versió 3)
     * 
     * @param name Nom del personatge
     * @param health Punts de vida (si negatiu, s'ajusta a 0)
     * @param attack Punts d'atac (si negatiu, s'ajusta a 0)
     * @param defense Punts de defensa (si negatiu, s'ajusta a 0)
     * @param speed Punts de velocitat (si negatiu, s'ajusta a 0)
     * @param element Tipus elemental del personatge
     */
    public Character(String name, int health, int attack, int defense, int speed, Element element) {
        this.name = name;
        this.health = Math.max(0, health);
        this.attack = Math.max(0, attack);
        this.defense = Math.max(0, defense);
        this.speed = Math.max(0, speed);
        this.element = element;
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
    
    public Element getElement() {
        return this.element;
    }
    
    /**
     * Calcula el damage que aquest personatge fa a un altre
     * 
     * VERSIÓ 2: Càlcul amb multiplicadors d'elements
     * Fórmula: damage = Math.max(0, (attack - defense) * multiplicador)
     * Multiplicador:
     *   - 1.5 si l'atacant té avantatge elemental
     *   - 0.5 si l'atacant té desavantatge elemental
     *   - 1.0 en cas contrari (neutral)
     * 
     * CAIXA BLANCA: Aquest mètode conté decisions per a Decision/Condition/Path Coverage
     * 
     * @param defender El personatge que rep l'atac
     * @return El damage calculat (sempre >= 0)
     */
    public int calculateDamage(Character defender) {
        int baseDamage = this.attack - defender.getDefense();
        
        // Aplicar multiplicador elemental
        double multiplier = 1.0;
        
        if (this.element != null && defender.getElement() != null) {
            // Comprovar avantatge
            if (this.element.isStrongAgainst(defender.getElement())) {
                multiplier = 1.5;
            }
            // Comprovar desavantatge
            else if (this.element.isWeakAgainst(defender.getElement())) {
                multiplier = 0.5;
            }
            // Si no, es manté 1.0 (neutral)
        }
        
        double finalDamage = baseDamage * multiplier;
        return Math.max(0, (int) Math.round(finalDamage));
    }
    
    /**
     * Rep dany i actualitza la health
     * 
     * @param damage Quantitat de dany a rebre
     */
    public void receiveDamage(int damage) {
        if (damage > 0) {
            this.health -= damage;
            if (this.health < 0) {
                this.health = 0;
            }
        }
    }
    
    /**
     * Comprova si el personatge està mort
     * 
     * @return true si health <= 0, false altrament
     */
    public boolean isDead() {
        return this.health <= 0;
    }
}
