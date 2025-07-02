package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

enum Direction {
    LEFT, RIGHT, UP, DOWN
}

class Tile {
    int value;

    public Tile(int value) {
        this.value = value;
    }
}

interface MoveStrategy {
    Tile[][] move(Tile[][] tiles, int GAME_SIZE);
}

class LeftMoveStrategy implements MoveStrategy {

    @Override
    public Tile[][] move(Tile[][] tiles, int GAME_SIZE) {
        for (int row = 0; row < GAME_SIZE; row ++) {
            List<Tile> tilesWithoutGap = new ArrayList<>();
            for (int col = 0; col < GAME_SIZE; col ++) {
                if (tiles[row][col] != null) {
                    tilesWithoutGap.add(tiles[row][col]);
                }
            }

            Tile[] updatedRows = new Tile[GAME_SIZE];
            int index = 0;
            for (int i = 0; i < tilesWithoutGap.size(); i++) {
                if (i + 1 < tilesWithoutGap.size() && tilesWithoutGap.get(i).value == tilesWithoutGap.get(i + 1).value) {
                    int doubleValue = 2 * tilesWithoutGap.get(i).value;
                    updatedRows[index++] = new Tile(doubleValue);
                    i ++;
                } else {
                    int value = tilesWithoutGap.get(i).value;
                    updatedRows[index++] = new Tile(value);
                }
            }
            for (int col = 0; col < GAME_SIZE; col++) {
                tiles[row][col] = updatedRows[col];
            }
        }
        return tiles;
    }

}

class RightMoveStrategy implements MoveStrategy {

    @Override
    public Tile[][] move(Tile[][] tiles, int GAME_SIZE) {
        for (int row = 0; row < GAME_SIZE; row ++) {
            List<Tile> tilesWithoutGap = new ArrayList<>();
            for (int col = 0; col < GAME_SIZE; col ++) {
                if (tiles[row][col] != null) {
                    tilesWithoutGap.add(tiles[row][col]);
                }
            }

            Tile[] updatedRows = new Tile[GAME_SIZE];
            int index = GAME_SIZE - 1;
            for (int i = 0; i < tilesWithoutGap.size(); i++) {
                if (i + 1 < tilesWithoutGap.size() && tilesWithoutGap.get(i).value == tilesWithoutGap.get(i + 1).value) {
                    int doubleValue = 2 * tilesWithoutGap.get(i).value;
                    updatedRows[index--] = new Tile(doubleValue);
                    i ++; // increase i value
                } else {
                    int value = tilesWithoutGap.get(i).value;
                    updatedRows[index--] = new Tile(value);
                }
            }
            for (int col = 0; col < GAME_SIZE; col++) {
                tiles[row][col] = updatedRows[col];
            }
        }
        return tiles;
    }

}

class UpMoveStrategy implements MoveStrategy {

    @Override
    public Tile[][] move(Tile[][] tiles, int GAME_SIZE) {
        for (int col = 0; col < GAME_SIZE; col ++) {
            List<Tile> tilesWithoutGap = new ArrayList<>();
            for (int row = 0; row < GAME_SIZE; row ++) {
                if (tiles[row][col] != null) {
                    tilesWithoutGap.add(tiles[row][col]);
                }
            }

            Tile[] updatedRows = new Tile[GAME_SIZE];
            int index = 0;
            for (int i = 0; i < tilesWithoutGap.size(); i++) {
                if (i + 1 < tilesWithoutGap.size() && tilesWithoutGap.get(i).value == tilesWithoutGap.get(i + 1).value) {
                    int doubleValue = 2 * tilesWithoutGap.get(i).value;
                    updatedRows[index++] = new Tile(doubleValue);
                    i ++; // increase i value
                } else {
                    int value = tilesWithoutGap.get(i).value;
                    updatedRows[index++] = new Tile(value);
                }
            }
            for (int row = 0; row < GAME_SIZE; row++) {
                tiles[row][col] = updatedRows[row];
            }
        }
        return tiles;
    }

}

class DownMoveStrategy implements MoveStrategy {

    @Override
    public Tile[][] move(Tile[][] tiles, int GAME_SIZE) {
        for (int col = 0; col < GAME_SIZE; col ++) {
            List<Tile> tilesWithoutGap = new ArrayList<>();
            for (int row = 0; row < GAME_SIZE; row ++) {
                if (tiles[row][col] != null) {
                    tilesWithoutGap.add(tiles[row][col]);
                }
            }

            Tile[] updatedRows = new Tile[GAME_SIZE];
            int index = GAME_SIZE - 1;
            for (int i = 0; i < tilesWithoutGap.size(); i++) {
                if (i + 1 < tilesWithoutGap.size() && tilesWithoutGap.get(i).value == tilesWithoutGap.get(i + 1).value) {
                    int doubleValue = 2 * tilesWithoutGap.get(i).value;
                    updatedRows[index--] = new Tile(doubleValue);
                    i ++; // increase i value
                } else {
                    int value = tilesWithoutGap.get(i).value;
                    updatedRows[index--] = new Tile(value);
                }
            }
            for (int row = 0; row < GAME_SIZE; row++) {
                tiles[row][col] = updatedRows[row];
            }
        }
        return tiles;
    }

}

public class Game {
    private final Integer GAME_SIZE;
    private Tile[][] tiles;
    private final Map<Direction, MoveStrategy> strategies = Map.of(
        Direction.LEFT, new LeftMoveStrategy(),
        Direction.RIGHT, new RightMoveStrategy(),
        Direction.UP, new UpMoveStrategy(),
        Direction.DOWN, new DownMoveStrategy()
    );

    public Game() {
        GAME_SIZE = 4;
        this.tiles = new Tile[GAME_SIZE][GAME_SIZE];
        spawnRandomTile();
        printGame();
    }

    public void merge(Direction direction) {
        strategies.get(direction).move(tiles, GAME_SIZE);
    }

    public void printGame() {
        System.out.println("----------------------");
        for (int row = 0; row < GAME_SIZE; row ++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                Tile tile = this.tiles[row][col];
                System.out.print((tile != null ? String.format("%4d", tile.value) : "   -") + " ");
            }
        }
        System.out.println("----------------------");
    }

    private void spawnRandomTile() {
        List<int[]> emptyLocations = new ArrayList<>();
        for (int row = 0; row < GAME_SIZE; row ++) {
            for (int col = 0; col < GAME_SIZE; col++) {
                if (tiles[row][col] == null) {
                    emptyLocations.add(new int[]{row, col});
                }
            }
        }
        if (!emptyLocations.isEmpty()) {
            int[] cell = emptyLocations.get((int)(Math.random() * emptyLocations.size()));
            tiles[cell[0]][cell[1]] = new Tile(2);
        }
    }

    public void makeNextMove(Direction direction) {
        merge(direction);
        spawnRandomTile();
        printGame();
    }
}

class Run {
    public static void main(String[] args) {
        Game game = new Game();

        Scanner scanner = new Scanner(System.in);
        String input = "start";
        while (!"end".equals(input)) {
            System.out.println("Choose direction");
            System.out.println("\n Left: 1 \n Right: 2 \n Up: 3 \n Down: 4");
            input = scanner.nextLine();
            System.out.println("Input chosen: " + input);


            switch (input) {
                case "1":
                    game.makeNextMove(Direction.LEFT);
                    break;
                case "2":
                    game.makeNextMove(Direction.RIGHT);
                    break;
                case "3":
                    game.makeNextMove(Direction.UP);
                    break;
                case "4":
                    game.makeNextMove(Direction.DOWN);
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }
}
