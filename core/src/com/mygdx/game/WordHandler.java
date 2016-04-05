package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.io.BufferedReader;
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
    private int LANG = 0;
    private int position = 1;
           
    // Return a random word from the wordlist
    public String selectNewWord(int NUMBER, int MINUSPLUS) throws IOException {
        String wordList = "";
        switch (LANG) {
            case 0:
                wordList = "en";
                break;
            case 1:
                wordList = "pt";
                break;
        }
        
        FileHandle words = Gdx.files.internal("words\\" + wordList + ".txt");
        BufferedReader br = new BufferedReader(words.reader()); 
        
        Random rand = new Random();
        word = "";
        user = "";
        position = 1;
        
        int groupingSize = 0;
        
        if (MINUSPLUS == 0) { // If set to '-'
            int sentinel = NUMBER;
            while (sentinel >= 5) {
                groupingSize += Integer.valueOf(br.readLine()); // size of random group to choose from
                sentinel--;
            }
            
            while (sentinel != -1) {
                sentinel = Integer.valueOf(br.readLine());
            }
            
            int size = rand.nextInt(groupingSize) + 1;
            
            while (size > 0) {
                word = br.readLine();
                size--;
            }
        }
        
        if (MINUSPLUS == 1) { // If set to '+'
            int sentinel = 5;
            int skipSize = 0;
            
            while (sentinel < NUMBER) {
                skipSize += Integer.valueOf(br.readLine()); // check how far down to skip into the word list
                sentinel++;
            }           
            
            while (sentinel <= 10) {
                groupingSize += Integer.valueOf(br.readLine()); // size of random group to choose from
                sentinel++;
            }
            
            while (skipSize + 1 > 0) { // skip down into the word list
                br.readLine();
                skipSize--;
            }
                        
            int size = rand.nextInt(groupingSize) + 1;
            
            while (size > 0) { // find your random word in the later grouping
                word = br.readLine();                
                size--;
            }
        }        
                 
        state = new int[word.length()];
        
        for (int a = 0; a < state.length; a++) {
            state[a] = CLEAR;
        }
        
        shuffleLetters();
        
        return word;
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
        
        state[(int) shuffled.keySet().toArray()[0]] = WHITE;  
        
        user = "";
        for (int a = 0; a < shuffled.size(); a++) {
            user += (String) shuffled.values().toArray()[a];
        }
                
        updateUserLabel();
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
        String updatedLabel = "";
                
        for (int a = 0; a < state.length; a++) { 
            if (state[a] == WHITE) {
                updatedLabel += "[WHITE]" + String.valueOf(word.charAt(a));
            } 
            
            if (state[a] == GRAY) {
                updatedLabel += "[GRAY]" + String.valueOf(word.charAt(a));
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
            if (redLetter.equals(String.valueOf(word.charAt(a))) && (state[a] != GRAY && state[a] != WHITE)) {               
                state[(int) shuffled.keySet().toArray()[position]] = WHITE;                
                state[priorIndex] = GRAY;                               
                position++;
                return true;
            }            
        }
        
        return false;
    }  
    
    public Boolean handleRight() {
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = (int) shuffled.keySet().toArray()[position - 1]; // Gets the (unshuffled) index of the last placed letter

        /*
        int num = 0;
        char check = user.charAt(position - 1);     // Get the character being sorted against
        for (int a = 0; a < word.length(); a++) {
            if (check == word.charAt(a)) {
                num++;                              // Find out how many equal characters exist
            }
        }
        
        int[] positions = new int[num];
        int pos = 0;
        for (int a = 0; a < word.length(); a++) {
            if (check == word.charAt(a)) {
                positions[pos] = a;                 // Store their (unshuffled) positions so each can be checked
                pos++;
            }
        }
        
        // uh, if you have THREE identical letters you have to do this for each pair
        // which could be done with nested FOR loops
        // 3 letters would need 2 runs, and you'd do pos[0], pos[0+1] where 0 advances
        // thereby advancing through each pair, returning true when something is found
        
        boolean duplicates = true;
        
        if (num > 1) {
            for (int a = positions[0] + 1; a < positions[1]; a++) {
                if (state[a] == GRAY) {
                    duplicates = false;
                }
            }
        }
        
        if (duplicates) {
            //System.out.println("PROBLEM");
        }
        
        for (int a = priorIndex; a < word.length(); a++) {
            if (redLetter.equals(String.valueOf(word.charAt(a))) && (state[a] != GRAY && state[a] != WHITE)) {  
                //state[(int) shuffled.keySet().toArray()[position]] = WHITE;     // this causes whites on the LEFT when you press 
                state[(int) shuffled.keySet().toArray()[a]] = WHITE;            // this causes double whites and whites on the left...                
                state[priorIndex] = GRAY;
                position++;
                return true;
            }
        }
        */
        
        for (int a = priorIndex; a < word.length(); a++) {             
            if (redLetter.equals(String.valueOf(word.charAt(a))) && (state[a] != GRAY && state[a] != WHITE)) {  
                state[(int) shuffled.keySet().toArray()[position]] = WHITE;     // this causes whites on the LEFT when you press 
                //state[(int) shuffled.keySet().toArray()[a]] = WHITE;            // this causes double whites and whites on the left...
                state[priorIndex] = GRAY;
                position++;
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
            state[a] = CLEAR;
        }
        
        shuffleLetters();
        
        return word;
    }
    
    public void setLang(int a) {
        LANG = a;
    }
}