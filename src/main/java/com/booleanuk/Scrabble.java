package com.booleanuk;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*

    My thoughts:
        The method for validating brackets should probably have been better planned. Felt like I followed the tests and
        hard-coded conditions a bit too much. The solution was also used completely without use of internet or AI.

        After some research I have found that using a stack implemented with an ArrayDeque could have been a better
        way of solving the problem.

 */

public class Scrabble {
    String word;
    public Scrabble(String word) {
        this.word = word;
    }

    public int score() {
        Map<Character, Integer> scoreMap = getCharacterIntegerMap();

        // Validate the input
        if (!isValidBracketSymmetry(word)) {
            return 0;
        }

        String wordUppercase = word.toUpperCase();
        int sum = 0;
        int multiplier = 1;
        int startIndex = 0;
        int endIndex = wordUppercase.length();

        // Full word multiplication
        if (wordUppercase.startsWith("{") && wordUppercase.endsWith("}") &&
                notFakeFullWrap(wordUppercase, '}')) {
            multiplier = 2;
            startIndex++;
            endIndex--;
            if (wordUppercase.charAt(1) == '[' && wordUppercase.charAt(endIndex - 1) == ']') {
                multiplier = 6; // Full word wrapped in both curly and square brackets
                startIndex++;
                endIndex--;
            }
        } else if (wordUppercase.startsWith("[") && wordUppercase.endsWith("]") &&
                notFakeFullWrap(wordUppercase, ']')) {
            multiplier = 3;
            startIndex++;
            endIndex--;
        }

        // Iterate through word
        for (int i = startIndex; i < endIndex; i++) {
            char c = wordUppercase.charAt(i);
            if (c == '{' || c == '[') {
                // Letter curly brackets -> next letter double score
                int letterMultiplier = (c == '{') ? 2 : 3;
                sum += scoreMap.get(wordUppercase.charAt(i + 1)) * letterMultiplier * multiplier;
                i += 2;
            } else {
                sum += scoreMap.get(c) * multiplier;
            }
        }
        // Return score
        return sum;
    }

    private boolean isValidBracketSymmetry(String word) {
        if (word.length() < 3 && word.matches("^[a-zA-Z]+$")) {
            return true;
        }
        if (word.isEmpty() || !word.matches("^[a-zA-Z{}\\[\\]]*$")) {
            return false;
        }

        int first = word.charAt(0);
        int last = word.charAt(word.length() - 1);
        int third = word.charAt(2);
        int thirdLast = word.charAt(word.length() - 3);

        // if the full word is wrapped, remove these brackets to check the rest
        if (((first == '{' && last == '}') && third != '}' && thirdLast != '{') || ((first == '[' && last == ']') && third != ']' && thirdLast != '[')) {
            word = word.substring(1, word.length() - 1);
        }
        first = word.charAt(0);
        last = word.charAt(word.length() - 1);
        if ((first == '[' && third != ']') && (last == ']' && thirdLast != '[')) {
            word = word.substring(1, word.length() - 1);
        }

        // loop through the rest to check for incorrect use of brackets
        for (int i = 0; i < word.length() - 2; i++) {
            if (word.charAt(i) == '{' && word.charAt(i + 2) != '}') {
                return false;
            }
            if (word.charAt(i) == '[' && word.charAt(i + 2) != ']') {
                return false;
            }
            if ((word.charAt(i) == ']' && i < 2) || (word.charAt(i) == '}' && i < 2)) {
                return false;
            }
        }

        for (int i = word.length() - 1; i > 2; i--) {
            if (word.charAt(i) == '}' && word.charAt(i - 2) != '{') {
                return false;
            }
            if (word.charAt(i) == ']' && word.charAt(i - 2) != '[') {
                return false;
            }
        }

        return true;
    }

    private boolean notFakeFullWrap(String word, char close) {
        int closingPos = word.indexOf(close);
        return closingPos == word.length() - 1;
    }

    private static Map<Character, Integer> getCharacterIntegerMap() {
        Map<Character, Integer> scoreMap = new HashMap<>();
        for (char c : new char[]{'A', 'E', 'I', 'O', 'U', 'L', 'N', 'R', 'S', 'T'}) {
            scoreMap.put(c, 1);
        }
        for (char c : new char[]{'D', 'G'}) {
            scoreMap.put(c, 2);
        }
        for (char c : new char[]{'B', 'C', 'M', 'P'}) {
            scoreMap.put(c, 3);
        }
        for (char c : new char[]{'F', 'H', 'V', 'W', 'Y'}) {
            scoreMap.put(c, 4);
        }
        for (char c : new char[]{'K'}) {
            scoreMap.put(c, 5);
        }
        for (char c : new char[]{'J', 'X'}) {
            scoreMap.put(c, 8);
        }
        for (char c : new char[]{'Q', 'Z'}) {
            scoreMap.put(c, 10);
        }
        return scoreMap;
    }
}
