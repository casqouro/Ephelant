package com.mygdx.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class WordHandler {
    private String word; 
    private String user;
    private int[] state;
    private Map<Integer, String> shuffled;    

    private static final int CLEAR = 0;
    private static final int GRAY = 1;
    private static final int WHITE = 2;
    private static final int RED = 3;    
    private int position = 1;
       
    // Return a random word from the wordlist
    public String selectNewWord() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("..\\assets\\words\\wordlist.txt")); 
        Random rand = new Random();
        word = "";
        user = "";
        position = 1;
        
        int total = Integer.parseInt(br.readLine());
        int size = rand.nextInt(Integer.parseInt(br.readLine())) + 1;
                
        // Traverse the list until you find the word
        if (size > 0 ) {
            while (size > 0) {
                word = br.readLine();
                String[] token;
                token = word.split(" ");            
                int available = Integer.parseInt((token[0]));

                if (available == 1) {
                    word = token[1];
                    size--;
                }
            }
        } else {
            System.out.println("The list is empty, and I should be doing something useful like handling an exception.");
        }
                
        state = new int[word.length()];
        
        for (int a = 0; a < state.length; a++) {
            changeLetterColor(a, CLEAR);
        }
        
        shuffleLetters();
        
        return word;
    }         
    
    private void changeLetterColor(int index, int color) {
        if (color == CLEAR) {
            state[index] = 0;            
        }        
        
        if (color == GRAY) {
            state[index] = 1;
        }
        
        if (color == WHITE) {
            state[index] = 2;
        }
        
        if (color == RED) {
            state[index] = 3;
        }        
    } 
    
    // Uses a Fisher-Yates shuffle on an int array, used to build a shuffled map.    
    private void shuffleLetters() {
        Random rand = new Random();
        shuffled = new LinkedHashMap<>();
        
        int[] place = new int[word.length()];
        for (int a = 0; a < place.length; a++) {
            int r = rand.nextInt(a + 1);
            place[a] = place[r];
            place[r] = a;
        }
        
        for (int a = 0; a < place.length; a++) {
            shuffled.put(place[a], String.valueOf(word.charAt(place[a])));
        }
        
        state[(int) shuffled.keySet().toArray()[0]] = GRAY;
        state[(int) shuffled.keySet().toArray()[1]] = RED;    
        
        user = "";
        for (int a = 0; a < shuffled.size(); a++) {
            user += (String) shuffled.values().toArray()[a];
        }
                
        updateUserLabel(); // should be done when "ready" button pressed, will be moved when Screen class built
        //wordLabel.setText(""); // used to empty out the wordLabel
    }    
        
    // Should happen after a player left/right movement
    public String updateWordLabel() {
        if (position < word.length()) {
            return "[RED]" + String.valueOf(user.charAt(position));
        } 
        
        return "[WHITE]" + word;
    }    
    
    // Should happen after a player left/right movement
    public String updateUserLabel() {
        String updatedLabel = "[GRAY]";
        
        int whitey = (int) shuffled.keySet().toArray()[position - 1];
        
        for (int a = 0; a < state.length; a++) {
            if (a == whitey) {
                updatedLabel += "[WHITE]" + String.valueOf(word.charAt(a)) + "[GRAY]";
            } else if (state[a] == GRAY) {
                updatedLabel += String.valueOf(word.charAt(a));
            }  
        }
        
        if (position == word.length()) {
            updatedLabel = "[WHITE]" + word;
        }
        
        return updatedLabel;
    }   

    public String getWord() {
        return word;
    }
    
    public Boolean handleLeft() {                       
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = (int) shuffled.keySet().toArray()[position - 1]; // Gets the index of the last placed letter (from the SHUFFLED index)
        
        for (int a = priorIndex - 1; a >= 0; a--) {
            if (redLetter.equals(String.valueOf(word.charAt(a))) && state[a] != GRAY) {
                state[a] = GRAY;
                position++;
                updateUserLabel();
                updateWordLabel(); 
                return true;
            }            
        }
        
        return false;
    }  
    
    public Boolean handleRight() {
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = (int) shuffled.keySet().toArray()[position - 1]; // Gets the index of the last placed letter (from the SHUFFLED index)

        for (int a = priorIndex; a < word.length(); a++) {
            if (redLetter.equals(String.valueOf(word.charAt(a))) && state[a] != GRAY) {
                state[a] = GRAY;
                position++;
                updateUserLabel();
                updateWordLabel();                
                return true;
            }            
        }
        
        return false;     
    }    
    
    public Boolean isComplete() {
        return position == word.length();
    }        
    
    public String restart() {        
        user = "";
        position = 1;        
        
        state = new int[word.length()];
        
        for (int a = 0; a < state.length; a++) {
            changeLetterColor(a, CLEAR);
        }
        
        shuffleLetters();
        
        return word;
    }
}
