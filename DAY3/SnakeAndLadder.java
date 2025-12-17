import java.util.*;

interface Dice {
    int roll();
}

interface GameObserver {
    void turnNotifier(Player player);
    void onPlayerMove(Player player, int oldPosition, int newPosition);
    void onSnakeBite(Player player, Snake snake);
    void onLadderClimb(Player player, Ladder ladder);
    void onPlayerWin(Player player);
}

class ObserverLog implements GameObserver {
    public void turnNotifier(Player player) {
        System.out.println("It's " + player.PlayerName + "'s turn.");
    }

    public void onPlayerMove(Player player, int oldPosition, int newPosition) {
        System.out.println(player.PlayerName + " moved from " + oldPosition + " to " + newPosition);
    }

    public void onSnakeBite(Player player, Snake snake) {
        System.out.println(player.PlayerName + " got bitten by a snake from " + snake.StartingCellNumber + " to " + snake.EndingCellNumber);
    }

    public void onLadderClimb(Player player, Ladder ladder) {
        System.out.println(player.PlayerName + " climbed a ladder from " + ladder.StartingCellNumber + " to " + ladder.EndingCellNumber);
    }

    public void onPlayerWin(Player player) {
        System.out.println(player.PlayerName + " has won the game!");
    }
}

class NormalDice implements Dice {
    public int roll() {
        Random rand = new Random();
        int steps = rand.nextInt(6) + 1;
        return steps;
    }
}

class Board {
    private int size;
    private List<Snake> snakes;
    private List<Ladder> ladders;
    
    public Board(int size, List<Snake> snakes, List<Ladder> ladders) {
        this.size = size;
        this.snakes = snakes;
        this.ladders = ladders;
    }

    public int checkSnake(int cellNumber) {
        for (Snake snake : snakes) {
            if (snake.StartingCellNumber == cellNumber) {
                return snake.EndingCellNumber;
            }
        }
        return cellNumber;
    }

    public int checkLadder(int cellNumber) {
        for(Ladder ladder : ladders) {
            if(ladder.StartingCellNumber == cellNumber) {
                return ladder.EndingCellNumber;
            }
        }
        return cellNumber;
    }
}

class Cell {
    public int cellNumber;

    public Cell(int cellNumber) {
        this.cellNumber = cellNumber;
    }
}

class Snake {
    public int snakeNumber;
    public int StartingCellNumber;
    public int EndingCellNumber;

    public Snake(int snakeNumber, int StartingCellNumber, int EndingCellNumber) {
        this.snakeNumber = snakeNumber;
        this.StartingCellNumber = StartingCellNumber;
        this.EndingCellNumber =EndingCellNumber;
    }
}

class Ladder {
    public int ladderNumber;
    public int StartingCellNumber;
    public int EndingCellNumber;

    public Ladder(int ladderNumber, int StartingCellNumber, int EndingCellNumber) {
        this.ladderNumber = ladderNumber;
        this.StartingCellNumber = StartingCellNumber;
        this.EndingCellNumber = EndingCellNumber;
    }
}

class Player {
    public String PlayerName;
    public int PlayerCurrentCellNumber;

    public Player(String PlayerName, int PlayerCurrentCellNumber) {
        this.PlayerName = PlayerName;
        this.PlayerCurrentCellNumber = PlayerCurrentCellNumber;
    }

    public void rollDiceAndMove(Dice dice, Board board) {
        int steps = dice.roll();
        PlayerCurrentCellNumber += steps;
        System.out.println(PlayerName + " moved to cell " + PlayerCurrentCellNumber);

        int snakeBite = board.checkSnake(PlayerCurrentCellNumber);
        int ladderClimb = board.checkLadder(PlayerCurrentCellNumber);

        if(snakeBite != PlayerCurrentCellNumber) {
            PlayerCurrentCellNumber = snakeBite;
            System.out.println(PlayerName + " got bitten by a snake and moved to cell " + PlayerCurrentCellNumber);
        } else if(ladderClimb != PlayerCurrentCellNumber) {
            PlayerCurrentCellNumber = ladderClimb;
            System.out.println(PlayerName + " climbed a ladder and moved to cell " + PlayerCurrentCellNumber);
        }
    }
}

class Game {
    private Board board;
    private Dice dice;
    private Queue<Player> players = new LinkedList<>();
    private int winningCellNumber;
    private List<GameObserver> observers = new ArrayList<>();

    public Game(Board board, Dice dice, int winningCellNumber) {
        this.board = board;
        this.dice = dice;
        this.winningCellNumber = winningCellNumber;
    }

    public void addPlayer(Player p) { players.add(p); }
    public void addObserver(GameObserver o) { observers.add(o); }

    private void notifyTurn(Player p) {
        observers.forEach(o -> o.turnNotifier(p));
    }
    private void notifyMove(Player p, int oldPos, int newPos) {
        observers.forEach(o -> o.onPlayerMove(p, oldPos, newPos));
    }
    private void notifySnake(Player p, Snake s) {
        observers.forEach(o -> o.onSnakeBite(p, s));
    }
    private void notifyLadder(Player p, Ladder l) {
        observers.forEach(o -> o.onLadderClimb(p, l));
    }
    private void notifyWin(Player p) {
        observers.forEach(o -> o.onPlayerWin(p));
    }

    public void start() {
        System.out.println("Game Started!");
        while (players.size() > 1) {
            Player current = players.poll();
            notifyTurn(current);

            int oldPos = current.PlayerCurrentCellNumber;
            int steps = dice.roll();
            int newPos = oldPos + steps;

            int snakePos = board.checkSnake(newPos);
            int ladderPos = board.checkLadder(newPos);

            if (snakePos != newPos) {
                notifySnake(current, new Snake(-1, newPos, snakePos));
                newPos = snakePos;
            } else if (ladderPos != newPos) {
                notifyLadder(current, new Ladder(-1, newPos, ladderPos));
                newPos = ladderPos;
            }

            current.PlayerCurrentCellNumber = newPos;
            notifyMove(current, oldPos, newPos);

            if (newPos >= winningCellNumber) {
                notifyWin(current);
                break;
            }
            players.add(current);
        }
    }
}

public class SnakeAndLadder {
    public static void main(String args[]) {
        List<Snake> snakes = new ArrayList<>();
        snakes.add(new Snake(1, 16, 6));
        snakes.add(new Snake(1, 47, 26));
        snakes.add(new Snake(1, 49, 11));
        snakes.add(new Snake(1, 56, 53));
        snakes.add(new Snake(1, 62, 19));
        snakes.add(new Snake(1, 64, 60));
        snakes.add(new Snake(1, 87, 24));
        snakes.add(new Snake(1, 93, 73));
        snakes.add(new Snake(1, 95, 75));
        snakes.add(new Snake(1, 98, 78));

        List<Ladder> ladders = new ArrayList<>();
        ladders.add(new Ladder(1, 1, 38));
        ladders.add(new Ladder(1, 4, 14));
        ladders.add(new Ladder(1, 9, 31));
        ladders.add(new Ladder(1, 21, 42));
        ladders.add(new Ladder(1, 28, 84));
        ladders.add(new Ladder(1, 36, 44));
        ladders.add(new Ladder(1, 51, 67));
        ladders.add(new Ladder(1, 71, 91));
        ladders.add(new Ladder(1, 80, 100));

        Board board = new Board(100, snakes, ladders);
        Dice dice = new NormalDice();
        Game game = new Game(board, dice, 100);
        game.addObserver(new ObserverLog());

        game.addPlayer(new Player("Kamalesh", 0));
        game.addPlayer(new Player("Arun", 0));

        game.start();
    }
}