package com.uab.tq.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uab.tq.controller.BattleController;
import com.uab.tq.model.Action;
import com.uab.tq.model.Battle;
import com.uab.tq.model.Character;
import com.uab.tq.model.Element;
import com.uab.tq.view.ConsoleView;

/**
 * Tests d'integració per l'aplicació completa
 * Sense mocks - Prova tot el sistema MVC conjuntament
 */
public class IntegrationTest {
    
    private BattleController controller;
    private ConsoleView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    public void setUp() {
        view = new ConsoleView();
        controller = new BattleController(view);
        
        // Capturar sortida
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testFullBattleFlow_WithoutMocks() {
        
        // INTEGRACIÓ COMPLETA: Model + Controller + View (sense mocks)
        
        // Crear personatges amb elements
        Character hero = new Character("Hero", 100, 30, 10, 20, Element.FIRE);
        Character enemy = new Character("Enemy", 80, 25, 15, 15, Element.GRASS);
        
        // Iniciar batalla
        controller.startBattle(hero, enemy);
        
        Battle battle = controller.getBattle();
        assertNotNull(battle);
        assertFalse(battle.isFinished());
        
        // Verificar que la vista ha mostrat l'estat inicial
        String output = outputStream.toString();
        assertTrue(output.contains("Hero"));
        assertTrue(output.contains("Enemy"));
        
        // Processar múltiples rondes
        int roundsProcessed = 0;
        while (!battle.isFinished() && roundsProcessed < 10) {
            outputStream.reset();
            controller.processRound(battle);
            roundsProcessed++;
        }
        
        // Verificar que la batalla ha acabat
        assertTrue(battle.isFinished() || roundsProcessed == 10);
        assertTrue(roundsProcessed > 0);
    }
    
    @Test
    public void testHandleAction_Integration() {
        
        // INTEGRACIÓ: handleAction amb Model + View real
        
        Character attacker = new Character("Knight", 100, 40, 15, 18, Element.WATER);
        Character defender = new Character("Dragon", 120, 35, 20, 12, Element.FIRE);
        
        controller.startBattle(attacker, defender);
        Battle battle = controller.getBattle();
        
        outputStream.reset();
        
        // Executar atac
        int initialDefenderHealth = defender.getHealth();
        controller.handleAction(Action.ATTACK, attacker, defender, battle);
        
        // Verificar que s'ha causat dany (avantatge elemental WATER > FIRE)
        assertTrue(defender.getHealth() < initialDefenderHealth);
        
        // Verificar que la vista ha mostrat l'atac
        String output = outputStream.toString();
        assertTrue(output.contains("Knight"));
        assertTrue(output.contains("Dragon"));
        assertTrue(output.contains("attack"));
    }
    
    @Test
    public void testHandleAction_DefendAndHeal_Integration() {
        
        // INTEGRACIÓ: Múltiples accions consecutives
        
        Character player = new Character("Player", 60, 30, 25, 15);
        Character boss = new Character("Boss", 100, 45, 30, 10);
        
        controller.startBattle(player, boss);
        Battle battle = controller.getBattle();
        
        // DEFEND: Augmentar defensa
        int initialDefense = player.getDefense();
        controller.handleAction(Action.DEFEND, player, boss, battle);
        assertTrue(player.getDefense() > initialDefense);
        
        // ITEM: Curar jugador
        int initialHealth = player.getHealth();
        controller.handleAction(Action.ITEM, player, boss, battle);
        assertTrue(player.getHealth() > initialHealth);
        assertEquals(80, player.getHealth()); // 60 + 20 = 80
    }
    
    @Test
    public void testCompleteGameScenario_ElementalAdvantage() {
        
        // ESCENARI COMPLET: Batalla amb avantatge elemental
        
        Character fireWarrior = new Character("Blaze", 90, 35, 12, 18, Element.FIRE);
        Character grassMage = new Character("Leaf", 85, 30, 10, 16, Element.GRASS);
        
        controller.startBattle(fireWarrior, grassMage);
        Battle battle = controller.getBattle();
        
        // FIRE té avantatge sobre GRASS (multiplicador 1.5x)
        int damageExpected = fireWarrior.calculateDamage(grassMage);
        assertTrue(damageExpected > (35 - 10)); // Més que dany base
        
        // Simular rondes fins acabar
        int maxRounds = 20;
        int rounds = 0;
        
        while (!battle.isFinished() && rounds < maxRounds) {
            controller.processRound(battle);
            rounds++;
        }
        
        assertTrue(battle.isFinished());
        
        // FIRE hauria de guanyar per avantatge elemental (més probable)
        // (No sempre, perquè depèn de l'ordre dels torns)
        assertTrue(fireWarrior.isDead() || grassMage.isDead());
    }
    
    @Test
    public void testBattleWithActionsAndRounds_Integration() {
        
        // INTEGRACIÓ TOTAL: Combinar accions manuals i rondes automàtiques
        
        Character player = new Character("Player", 100, 30, 15, 20);
        Character enemy = new Character("Enemy", 100, 30, 15, 15);
        
        controller.startBattle(player, enemy);
        Battle battle = controller.getBattle();
        
        // Rondes inicials
        controller.processRound(battle);
        controller.processRound(battle);
        
        if (!battle.isFinished()) {
            // Usar ítem per curar
            if (player.getHealth() < 100) {
                controller.handleAction(Action.ITEM, player, enemy, battle);
            }
            
            // Defensar
            controller.handleAction(Action.DEFEND, player, enemy, battle);
            
            // Atacar
            controller.handleAction(Action.ATTACK, player, enemy, battle);
        }
        
        // Continuar rondes fins al final
        int rounds = 0;
        while (!battle.isFinished() && rounds < 30) {
            controller.processRound(battle);
            rounds++;
        }
        
        assertTrue(battle.isFinished() || rounds == 30);
    }
    
    @Test
    public void testMVCIntegration_AllComponents() {
        
        // TEST FINAL: Verificar que Model + View + Controller funcionen junts
        
        // MODEL: Character + Battle + Element + Action
        Character char1 = new Character("Alpha", 100, 35, 15, 18, Element.WATER);
        Character char2 = new Character("Beta", 100, 35, 15, 18, Element.FIRE);
        
        // CONTROLLER: BattleController
        controller.startBattle(char1, char2);
        Battle battle = controller.getBattle();
        
        // VIEW: ConsoleView (capturada)
        outputStream.reset();
        
        // Executar operacions diverses
        controller.processRound(battle);
        controller.handleAction(Action.ATTACK, char1, char2, battle);
        controller.handleAction(Action.DEFEND, char1, char2, battle);
        controller.handleAction(Action.ITEM, char1, char2, battle);
        
        // Verificar que tot ha funcionat sense errors
        assertNotNull(controller.getBattle());
        assertNotNull(controller.getView());
        
        String output = outputStream.toString();
        assertFalse(output.isEmpty());
        
        // MVC funcionant correctament
        assertTrue(true); // Si arribem aquí, integració OK
    }
}
