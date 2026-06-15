package day_22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day22 {

    public static void main(String[] args) {
        List<String> cardInputList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine = null;
        
        try {
            while ((inputLine = reader.readLine()) != null) {
                cardInputList.add(inputLine);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        // Build the decks
        List<List<Integer>> masterDeckList = new ArrayList<>();
        List<Integer> playerDeck = new LinkedList<>();
        int cardCount = 0;
        
        for (String item : cardInputList) {
            if (item.startsWith("Player")) {
                continue;
            } else if (item.length() == 0) {
                masterDeckList.add(playerDeck);
                playerDeck = new LinkedList<>();
            } else {
                playerDeck.add(Integer.valueOf(item));
                cardCount++;
            }
        }
        
        masterDeckList.add(playerDeck);

        // Part one
        List<List<Integer>> gameDeckList = new ArrayList<>();
        gameDeckList.add(new ArrayList<>(masterDeckList.get(0)));
        gameDeckList.add(new ArrayList<>(masterDeckList.get(1)));
        playTheGamePartOne(gameDeckList, cardCount);
        
        // Part two
        gameDeckList = new ArrayList<>();
        gameDeckList.add(new ArrayList<>(masterDeckList.get(0)));
        gameDeckList.add(new ArrayList<>(masterDeckList.get(1)));
        playTheGamePartTwo(gameDeckList);
    }
    
    private static void playTheGamePartOne(List<List<Integer>> players, int deckSize) {
        boolean winnerFound = false;
        int roundCount = 0;
        List<Integer> winner = null;
        
        while (winnerFound == false) {
            roundCount++;
//            System.out.println("-- Round " + roundCount + " --");
//            System.out.println("Player 1's deck: " + players.get(0));
//            System.out.println("Player 2's deck: " + players.get(1));
            
            int card1 = players.get(0).remove(0);
            int card2 = players.get(1).remove(0);

//            System.out.println("Player 1 plays: " + card1);
//            System.out.println("Player 2 plays: " + card2);
            
            if (card1 > card2) {
//                System.out.println("Player 1 wins the round");
                players.get(0).add(card1);
                players.get(0).add(card2);
            } else if (card2 > card1) {
//                System.out.println("Player 2 wins the round");
                players.get(1).add(card2);
                players.get(1).add(card1);
            } else {
                players.get(0).add(card1);
                players.get(1).add(card2);
            }
            
            for (List<Integer> player : players) {
                if (player.size() == deckSize) {
                    winnerFound = true;
                    winner = player;
                    break;
                }
            }
        }
        
        int playerScore = 0;
        int multiplier = deckSize;
        
        for (Integer card : winner) {
            playerScore += (card * multiplier);
            multiplier--;
        }
        
        System.out.println("Part one: " + playerScore);
    }

    private static int gameCount = 1;
    
    private static void playTheGamePartTwo(List<List<Integer>> players) {
        int winner = recursiveCombat(players);
        
        List<Integer> winnerDeck = players.get(winner - 1);
        int playerScore = 0;
        int multiplier = winnerDeck.size();
        
        for (Integer card : winnerDeck) {
            playerScore += (card * multiplier);
            multiplier--;
        }

        System.out.println("Part two: " + playerScore);
    }
    
    private static int recursiveCombat(List<List<Integer>> players) {
//        System.out.println();
//        System.out.println("=== Game " + gameCount + " ===");
        
        Set<Integer> previousRounds = new HashSet<>();
        int winner = -1;
        int roundCount = 0;
        int currentGame = gameCount;
        
        while (true) {
            roundCount++;
            
            // Has this card arrangement been seen before?
            if (previousRounds.contains(players.hashCode())) {
                // Player 1 wins the game
                return 1;
            }
            
            // Add this arrangement to the previous rounds set
            previousRounds.add(players.hashCode());

//            System.out.println();
//            System.out.println("-- Round " + roundCount + " (Game " + currentGame + ") --");
//            System.out.println("Player 1's deck: " + players.get(0));
//            System.out.println("Player 2's deck: " + players.get(1));

            int card1 = players.get(0).remove(0);
            int card2 = players.get(1).remove(0);

//            System.out.println("Player 1 plays: " + card1);
//            System.out.println("Player 2 plays: " + card2);

            if (players.get(0).size() < card1 || players.get(1).size() < card2) {
                // A player doesn't have enough cards to recurse - use the drawn cards to determine the winner
                if (card1 > card2) {
//                    System.out.println("Player 1 wins round " + roundCount + " of game " + currentGame);
                    players.get(0).add(card1);
                    players.get(0).add(card2);
                } else if (card2 > card1) {
//                    System.out.println("Player 2 wins round " + roundCount + " of game " + currentGame);
                    players.get(1).add(card2);
                    players.get(1).add(card1);
                } else {
                    players.get(0).add(card1);
                    players.get(1).add(card2);
                }
            } else {
                // Time for a recursive game
//                System.out.println("Playing a sub-game to determine the winner...");
                List<List<Integer>> subGameDecks = new ArrayList<>();

                // Create new player 1 deck
                List<Integer> newDeck = new LinkedList<>();
                    
                for (int i = 0; i < card1; i++) {
                    newDeck.add(players.get(0).get(i));
                }

                subGameDecks.add(newDeck);

                // Create new player 2 deck
                newDeck = new LinkedList<>();
                
                for (int i = 0; i < card2; i++) {
                    newDeck.add(players.get(1).get(i));
                }

                subGameDecks.add(newDeck);
                gameCount++;
                int tempWinner = recursiveCombat(subGameDecks);
//                System.out.println();
//                System.out.println("...anyway, back to game " + currentGame);
//                System.out.println("Player " + tempWinner + " wins round " + roundCount + " of game " + currentGame);

                if (tempWinner == 1) {
                    // Player 1 wins
                    players.get(0).add(card1);
                    players.get(0).add(card2);
                } else {
                    // Player 2 wins
                    players.get(1).add(card2);
                    players.get(1).add(card1);
                }
            }

            if (players.get(0).size() == 0) {
                // Player two wins
                winner = 2;
                break;
            } else if (players.get(1).size() == 0) {
                // Player one wins
                winner = 1;
                break;
            }
        }
        
//        System.out.println("The winner of game " + currentGame + " is player " + winner);
        
        return winner;
    }
}
