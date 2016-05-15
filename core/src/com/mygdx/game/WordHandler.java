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
    public int[] state;
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
        
        FileHandle words = Gdx.files.internal(wordList + ".txt");
        BufferedReader br = new BufferedReader(words.reader()); 
        
        /* checks for duplicates
        for (int a = 0; a < 308; a++) {
            word = br.readLine();
            
            for (int b = 0; b < word.length(); b++) {
                for (int c = b+1; c < word.length(); c++) {
                    if (word.charAt(b) == word.charAt(c)) {
                        System.out.println(word);
                    }
                }
            }
        }
        */
        
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
               
        state[Integer.valueOf(shuffled.keySet().toArray()[0].toString())] = WHITE;
        
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
  
        return "";
    }    
    
    // Should happen after a player left/right movement
    public String[] updateUserLabel() { 
        String[] updatedLabel = new String[word.length()];
                
        for (int a = 0; a < updatedLabel.length; a++) {
            switch (state[a]) {
                case GRAY:
                    updatedLabel[a] = "[GRAY]" + word.charAt(a);  // may need to be String.valueOf(word.charAt(a))  
                    break;                     
                case CLEAR:
                    updatedLabel[a] = "";  
                    break;
                case WHITE:
                    updatedLabel[a] = "[WHITE]" + word.charAt(a);    
                    break;                    
            } 
        } 

        if (position == word.length()) {
            for (int a = 0; a < state.length; a++) {
                updatedLabel[a] = "[WHITE]" + word.charAt(a);                
            }
        }      
        
        return updatedLabel;
    }
    
    /* faster, but still doesn't resolve the problem
    private String[] userLabel;
    public void updateUserLabelS() {
        // wrong values, needs to get next value per shuffled group
        int current = Integer.valueOf(shuffled.keySet().toArray()[position].toString());
        int previous = Integer.valueOf(shuffled.keySet().toArray()[position - 1].toString());
        userLabel[previous] = "[GRAY]" + word.charAt(previous);
        userLabel[current] = "[WHITE]" + word.charAt(current); 
        
        for (int a = 0; a < userLabel.length; a++) {
            System.out.println(userLabel[a]);
        } System.out.println();
    }
    
    public String[] getUserLabel() {
        return userLabel;
    }
    */   
    
    public String getWord() {
        return word;
    }
    
    public Boolean handleLeft() {                       
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = Integer.valueOf(shuffled.keySet().toArray()[position - 1].toString()); // Gets the index of the last placed letter (from the SHUFFLED index)
               
        for (int a = priorIndex - 1; a >= 0; a--) {          
            if (redLetter.equals(String.valueOf(word.charAt(a))) && (state[a] != GRAY && state[a] != WHITE)) {               
                state[Integer.valueOf(shuffled.keySet().toArray()[position].toString())] = WHITE;                
                state[priorIndex] = GRAY;               
                position++;
                return true;
            }            
        }
        
        return false;
    }  
    
    public Boolean handleRight() {
        String redLetter = String.valueOf(user.charAt(position)); // Gets the current to-Sort letter
        int priorIndex = Integer.valueOf(shuffled.keySet().toArray()[position - 1].toString()); // Gets the (unshuffled) index of the last placed letter
        
        for (int a = priorIndex; a < word.length(); a++) {             
            if (redLetter.equals(String.valueOf(word.charAt(a))) && (state[a] != GRAY && state[a] != WHITE)) {  
                state[Integer.valueOf(shuffled.keySet().toArray()[position].toString())] = WHITE;
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
    
    public int getPosition() {
        return position;
    }
    
    public Map getShuffledMap() {
        return shuffled;
    }
}