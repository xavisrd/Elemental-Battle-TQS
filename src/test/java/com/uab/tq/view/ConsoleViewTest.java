package com.uab.tq.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uab.tq.model.Character;

/**
 * Tests per ConsoleView amb Data-Driven Testing
 * Commit 28 (RED)
 */
public class ConsoleViewTest {
    
    private ConsoleView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    public void setUp() {
        view = new ConsoleView();
        
        // Capturar la sortida de System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    public void tearDown() {
        // Restaurar System.out original
        System.setOut(originalOut);
    }
    
    @Test
    public void testDisplayBattleStatus_Basic() {
        
        // Partició: MOSTRAR ESTAT BATALLA NORMAL
        
        Character char1 = new Character("Hero", 100, 30, 10, 15);
        Character char2 = new Character("Enemy", 80, 25, 8, 12);
        
        view.displayBattleStatus(char1, char2);
        
        String output = outputStream.toString();
        
        // Verificar que conté els noms i la salut
        assertTrue(output.contains("Hero"));
        assertTrue(output.contains("100"));
        assertTrue(output.contains("Enemy"));
        assertTrue(output.contains("80"));
    }
    
    @Test
    public void testDisplayBattleStatus_OneCharacterDead() {
        
        // Partició: UN PERSONATGE MORT
        
        Character char1 = new Character("Winner", 50, 30, 10, 15);
        Character char2 = new Character("Loser", 0, 25, 8, 12);
        
        view.displayBattleStatus(char1, char2);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("Winner"));
        assertTrue(output.contains("50"));
        assertTrue(output.contains("Loser"));
        assertTrue(output.contains("0"));
    }
    
    @Test
    public void testDisplayBattleStatus_BoundaryHealth() {
        
        // Partició: VALORS FRONTERA (health=1, health=100)
        
        Character char1 = new Character("Critical", 1, 30, 10, 15);
        Character char2 = new Character("Full", 100, 25, 8, 12);
        
        view.displayBattleStatus(char1, char2);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("1"));
        assertTrue(output.contains("100"));
    }
    
    @Test
    public void testDisplayAttack_NormalDamage() {
        
        // Partició: ATAC AMB DAMAGE NORMAL
        
        Character attacker = new Character("Warrior", 100, 30, 10, 15);
        Character defender = new Character("Mage", 80, 25, 8, 12);
        
        view.displayAttack(attacker, defender, 25);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("Warrior"));
        assertTrue(output.contains("Mage"));
        assertTrue(output.contains("25"));
        assertTrue(output.contains("attack"));
    }
    
    @Test
    public void testDisplayAttack_ZeroDamage() {
        
        // Partició: ATAC SENSE DAMAGE (defensa alta)
        
        Character attacker = new Character("Weak", 100, 10, 5, 15);
        Character defender = new Character("Tank", 80, 25, 50, 12);
        
        view.displayAttack(attacker, defender, 0);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("Weak"));
        assertTrue(output.contains("Tank"));
        assertTrue(output.contains("0"));
    }
    
    @Test
    public void testDisplayAttack_HighDamage() {
        
        // Partició: ATAC AMB DAMAGE ALT (>50)
        
        Character attacker = new Character("Striker", 100, 80, 10, 15);
        Character defender = new Character("Victim", 80, 25, 5, 12);
        
        view.displayAttack(attacker, defender, 75);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("Striker"));
        assertTrue(output.contains("Victim"));
        assertTrue(output.contains("75"));
    }
    
    @Test
    public void testDisplayWinner_NormalName() {
        
        // Partició: GUANYADOR AMB NOM NORMAL
        
        Character winner = new Character("Champion", 50, 30, 10, 15);
        
        view.displayWinner(winner);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("Champion"));
        assertTrue(output.contains("win"));
    }
    
    @Test
    public void testDisplayWinner_LongName() {
        
        // Partició: GUANYADOR AMB NOM LLARG
        
        Character winner = new Character("LongNameCharacter", 100, 30, 10, 15);
        
        view.displayWinner(winner);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("LongNameCharacter"));
    }
    
    @Test
    public void testDisplayWinner_ShortName() {
        
        // Partició: GUANYADOR AMB NOM CURT (1 lletra)
        
        Character winner = new Character("A", 1, 30, 10, 15);
        
        view.displayWinner(winner);
        
        String output = outputStream.toString();
        
        assertTrue(output.contains("A"));
    }
    
    // DATA-DRIVEN TESTING: Llegir casos de test des de CSV
    @Test
    public void testConsoleView_DataDriven() throws IOException {
        
        String csvFile = "src/test/resources/consoleView_testcases.csv";
        List<ViewTestCase> testCases = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split ignorant comes dins de cometes
                
                // Netejar cometes dels strings
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("\"", "");
                }
                
                testCases.add(new ViewTestCase(
                    values[0],
                    values[1],
                    Integer.parseInt(values[2]),
                    values[3],
                    values[4].equals("NULL") ? 0 : Integer.parseInt(values[4]),
                    Integer.parseInt(values[5]),
                    values[6]
                ));
            }
        }
        
        // Executar tots els casos de test
        for (ViewTestCase tc : testCases) {
            outputStream.reset(); // Netejar buffer
            
            Character char1 = new Character(tc.char1Name, tc.char1Health, 20, 10, 15);
            Character char2 = tc.char2Name.equals("NULL") ? null : 
                             new Character(tc.char2Name, tc.char2Health, 25, 8, 12);
            
            switch (tc.testType) {
                case "BattleStatus":
                    view.displayBattleStatus(char1, char2);
                    break;
                case "Attack":
                    view.displayAttack(char1, char2, tc.damage);
                    break;
                case "Winner":
                    view.displayWinner(char1);
                    break;
            }
            
            String output = outputStream.toString().trim();
            
            // Verificar que la sortida conté els elements esperats
            if (tc.testType.equals("BattleStatus")) {
                assertTrue(output.contains(tc.char1Name), 
                    "Output should contain: " + tc.char1Name + ", got: " + output);
                assertTrue(output.contains(String.valueOf(tc.char1Health)),
                    "Output should contain: " + tc.char1Health + ", got: " + output);
                assertTrue(output.contains(tc.char2Name),
                    "Output should contain: " + tc.char2Name + ", got: " + output);
            } else if (tc.testType.equals("Attack")) {
                assertTrue(output.contains(tc.char1Name),
                    "Output should contain: " + tc.char1Name + ", got: " + output);
                assertTrue(output.contains(tc.char2Name),
                    "Output should contain: " + tc.char2Name + ", got: " + output);
                assertTrue(output.contains(String.valueOf(tc.damage)),
                    "Output should contain: " + tc.damage + ", got: " + output);
            } else if (tc.testType.equals("Winner")) {
                assertTrue(output.contains(tc.char1Name),
                    "Output should contain: " + tc.char1Name + ", got: " + output);
            }
        }
        
        // Verificar que s'han processat tots els casos
        assertEquals(12, testCases.size());
    }
    
    // Inner class per Data-Driven Testing
    private static class ViewTestCase {
        String testType;
        String char1Name;
        int char1Health;
        String char2Name;
        int char2Health;
        int damage;
        String expectedOutput;
        
        ViewTestCase(String testType, String char1Name, int char1Health, 
                    String char2Name, int char2Health, int damage, String expectedOutput) {
            this.testType = testType;
            this.char1Name = char1Name;
            this.char1Health = char1Health;
            this.char2Name = char2Name;
            this.char2Health = char2Health;
            this.damage = damage;
            this.expectedOutput = expectedOutput;
        }
    }
}
