import java.io.*;
import java.util.*;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc22.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.winnerScore(input));
        //Part 2
        System.out.println(aoc.winnerScoreRecursiveCombat(input));
    }
}

class AOC {
    static class Player{
        Queue<Integer> deck = new LinkedList<>();
        String playerId;

        public Player(String playerId) {
            this.playerId = playerId;
        }


        public String getPlayerId() {
            return playerId;
        }

        public void addCard(int card) {
            deck.add(card);
        }

        public int play(){
            int card= deck.remove();
            System.out.printf("%s plays: %d%n",playerId,card);
            return card;
        }

        public boolean hasCards(){
            return !deck.isEmpty();
        }

        public void print(){
            System.out.println(playerId + " 's deck:" + deck + "  n="+deck.size());
        }

        public long getScore(){
            long score=0;
            var cards = deck.stream().mapToInt(Integer::intValue).toArray();
            for (int i = 0; i < cards.length; i++) {
                score+=cards[i]*(cards.length-i);
            }
            return score;
        }

        public int cardsLeft(){
            return deck.size();
        }

        public Queue<Integer> copyDeck() {
            return new LinkedList<>(deck);
        }

        public String getSnapshot() {
            return playerId + "-" + deck;
        }

        public Player getPlayerForSubGame(int numberOfCards){
            var clonedPlayer = new Player(playerId);
            var cards = deck.stream().mapToInt(Integer::intValue).toArray();
            for (int i = 0; i < numberOfCards; i++) {
                clonedPlayer.addCard(cards[i]);
            }

            return clonedPlayer;
        }

    }
    static class Game {
        private final List<Player> players = new ArrayList<>();
        Player winner;

        public Player getWinner() {
            return winner;
        }

        public void addPlayer(Player player) {
            this.players.add(player);
        }

        public void playRound(int round){
            System.out.printf("-- Round %d --%n",round);
            print();
            int winningScore = 0;
            List<Integer> cards= new ArrayList<>();
            for (Player player:
                 players) {
                int cardPlayed = player.play();
                if(cardPlayed>winningScore){
                    winningScore = cardPlayed;
                    winner = player;
                }
                cards.add(cardPlayed);
            }
            for (int i = players.indexOf(winner); i < players.size(); i++) {
                winner.addCard(cards.get(i));
            }

            for (int i = 0; i < players.indexOf(winner); i++) {
                winner.addCard(cards.get(i));
            }

            System.out.println(winner.getPlayerId() +  " wins the round!");
        }

        private boolean isGameOver(){
            int nonZero=0;
            for (var player:
                 this.players) {
                if(player.hasCards()){
                    nonZero++;
                }

                if(nonZero>1){
                    return false;
                }
            }
            System.out.println("GAME OVER!!");
            System.out.printf("WINNER IS %s%n",winner.playerId);
            return true;
        }

        public void playGame(){
            int round=0;
            while (!isGameOver()){
                round++;
                playRound(round);
            }
        }

        private String getSnapshot(Player player1,Player player2) {
            return player1.getSnapshot() + ":" + player2.getSnapshot();
        }

        public long getWinnerScore(){
            return winner.getScore();
        }

        public void print(){
            for (var player:
                 players) {
                player.print();
            }
        }

        public void print(Player player1,Player player2){
            player1.print();
            player2.print();
        }

        public Player getPlayer1(){
            return players.get(0);
        }

        public Player getPlayer2(){
            return players.get(1);
        }

        public Player playRecursiveCombat(Player player1, Player player2, int game) {
            HashSet<String> history = new HashSet<>();
            int round=1;
            while (player1.hasCards() && player2.hasCards()) {
                System.out.printf("Game %d - Round %d%n",game,round);
                if (!history.add(getSnapshot(player1, player2))) {
                    System.out.println("REPEAT");
                    return player1;
                }
                player1.print();
                player2.print();
                var card1 = player1.play();
                var card2 = player2.play();
                if (player1.cardsLeft() >= card1 && player2.cardsLeft() >= card2) {
                    if (playRecursiveCombat(player1.getPlayerForSubGame(card1), player2.getPlayerForSubGame(card2),game+1)
                            .getPlayerId().equals(player1.getPlayerId())) {
                        player1.addCard(card1);
                        player1.addCard(card2);
                    } else {
                        player2.addCard(card2);
                        player2.addCard(card1);
                    }
                } else {
                    if (card1 > card2) {
                        player1.addCard(card1);
                        player1.addCard(card2);
                    } else {
                        player2.addCard(card2);
                        player2.addCard(card1);
                    }
                }
                round++;
            }

            if (!player2.hasCards()) {
                printWinner(player1, game);
                this.winner = player1;
                return player1;
            }
            printWinner(player2, game);
            this.winner = player2;
            return player2;
        }

        private void printWinner(Player player1, int game) {
            System.out.printf("WINNER - GAME %d - %s%n", game, player1.getPlayerId());
        }

    }

    Game game= new Game();
    public long winnerScore(List<String> input) {
        parseInput(input);
        game.print();
        game.playGame();
        return game.getWinnerScore();
    }

    public long winnerScoreRecursiveCombat(List<String> input) {
        parseInput(input);
        var winner = game.playRecursiveCombat(game.getPlayer1(),game.getPlayer2(),1);
        System.out.println("GAME OVER!!!");
        System.out.println("Winner:" +winner.getPlayerId());
        return game.getWinnerScore();
    }

    private void parseInput(List<String> input) {
        game = new Game();
        Player player=null;
        for (String line:
             input) {
            if(line.isEmpty()){
                continue;
            }

            if(line.startsWith("Player")){
                player= new Player(line.substring(0, line.length() - 1));
                game.addPlayer(player);
                continue;
            }

            assert player != null;
            player.addCard(Integer.parseInt(line));
        }
    }
}
