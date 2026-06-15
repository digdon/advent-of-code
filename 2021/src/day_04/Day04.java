package day_04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> inputLines = reader.lines().collect(Collectors.toList());
        reader.close();
        
        // Parse out called numbers
        String[] parts = inputLines.get(0).split(",");
        int[] calledNumbers = new int[parts.length];
        
        for (int i = 0; i < parts.length; i++) {
            calledNumbers[i] = Integer.parseInt(parts[i]);
        }

        // Build the cards
        List<CardNumber[]> cardList = new ArrayList<>();
        CardNumber[] card = null;
        int cardPos = 0;
        
        for (int i = 2; i < inputLines.size(); i++) {
            String line = inputLines.get(i);
            
            if (line.length() == 0) {
                // End of a card
                card = null;
            } else {
                String[] items = line.trim().split("\\s+");
                
                if (card == null) {
                    // Start building a new card
                    card = new CardNumber[items.length * items.length];
                    cardPos = 0;
                    cardList.add(card);
                }

                for (int j = 0; j < items.length; j++) {
                    card[cardPos++] = new CardNumber(Integer.parseInt(items[j]));
                }
            }
        }

        // Draw the numbers and start looking for a winning card
        CardNumber[] winningCard = null;
        int firstWinningCardScore = 0;
        int lastWinningCardScore = 0;
        
        int lastCalled = 0;
        
        for (int i = 0; i < calledNumbers.length; i++) {
            int number = calledNumbers[i];
            lastCalled = number;
            markCards(number, cardList);
            
            while ((winningCard = lookForWinningCard(cardList)) != null) {
                lastWinningCardScore = calculateCardValue(winningCard) * lastCalled;
                
                if (firstWinningCardScore == 0) {
                    firstWinningCardScore = lastWinningCardScore;
                }
                
                cardList.remove(winningCard);
                winningCard = null;
            }
        }

        System.out.println("Part 1: " + firstWinningCardScore);
        System.out.println("Part 2: " + lastWinningCardScore);
    }
    
    private static void markCards(int number, List<CardNumber[]> cardList) {
        for (int i = 0; i < cardList.size(); i++) {
            CardNumber[] card = cardList.get(i);
            
            for (int j = 0; j < card.length; j++) {
                if (card[j].value == number) {
                    card[j].marked = true;
                    break;
                }
            }
        }
    }

    private static CardNumber[] lookForWinningCard(List<CardNumber[]> cardList) {
        for (CardNumber[] card : cardList) {
            // Look for horizontal lines first
            int cardSize = (int)Math.sqrt(card.length);
            
            for (int row = 0; row < cardSize; row++) {
                boolean rowComplete = true;
                
                for (int col = 0; col < cardSize; col++) {
                    if (card[(row * cardSize) + col].marked == false) {
                        rowComplete = false;
                        break;
                    }
                }
                
                if (rowComplete) {
                    return card;
                }
            }
            
            // Now look for vertical lines
            for (int col = 0; col < cardSize; col++) {
                boolean colComplete = true;
                
                for (int row = 0; row < cardSize; row++) {
                    if (card[(row * cardSize) + col].marked == false) {
                        colComplete = false;
                        break;
                    }
                }
                
                if (colComplete) {
                    return card;
                }
            }
        }
        
        return null;
    }

    private static int calculateCardValue(CardNumber[] card) {
        int total = 0;
        
        for (int i = 0; i < card.length; i++) {
            if (card[i].marked == false) {
                total += card[i].value;
            }
        }
        
        return total;
    }
    
    private static void displayCard(CardNumber[] card) {
        int rowSize = (int)Math.sqrt(card.length);
        
        for (int i = 0; i < card.length; i++) {
            if (i > 0 && (i % rowSize == 0)) {
                System.out.println();
            }
            
            System.out.print(String.format("%3d%c ", card[i].value, card[i].marked ? '*' : ' '));
        }
        
        System.out.println();
    }
    
    static class CardNumber {
        int value;
        boolean marked;
        
        CardNumber(int number) {
            value = number;
        }
    }
}
