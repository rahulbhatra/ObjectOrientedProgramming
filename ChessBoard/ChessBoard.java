package ChessBoard;

import java.util.ArrayList;
import java.util.List;

enum GameStatus {
    RUNNING, BLACK_WIN, WHITE_WIN;
}

class Player {
    private String name;
    private boolean isWhite = true;

    public boolean isWhite() {
        return isWhite;
    }

    public Player(String name, boolean isWhite) {
        this.name = name;
        this.isWhite = isWhite;
    }

    public String getName() {
        return name;
    }
}

class ChessBoard {
    private Square[][] squares;
    private List<Move> moveHistory;
    private GameStatus gameStatus;
    private Player player1;
    private Player player2;
    private boolean isWhitesTurn = true;

    public void trackMove(Move move) {
        moveHistory.add(move);
    }

    public void startGame(Player player1, Player player2) {
        if (player1.isWhite() && player2.isWhite()) {
            System.out.println("Both player can't be white!");
            return;
        }

        this.moveHistory = new ArrayList<>();
        this.squares = new Square[8][8];
        this.gameStatus = GameStatus.RUNNING;
        this.player1 = player1;
        this.player2 = player2;

        // assign white pieces first col
        this.squares[0][0] = new Square(0, 0, new RookPiece(true));
        this.squares[7][0] = new Square(7, 0, new RookPiece(true));

        this.squares[1][0] = new Square(1, 0, new BishopPiece(true));
        this.squares[6][0] = new Square(6, 0, new BishopPiece(true));

        this.squares[2][0] = new Square(2, 0, new KnightPiece(true));
        this.squares[5][0] = new Square(5, 0, new KnightPiece(true));

        this.squares[3][0] = new Square(3, 0, new QueenPiece(true));
        this.squares[4][0] = new Square(4, 0, new KingPiece(true));

        // assign white pawn second column
        for(int i = 0; i < 8; i++) {
            this.squares[i][1] = new Square(i, 1, new PawnPiece(true));
        }
        
        // assign black pieces
        this.squares[0][7] = new Square(0, 7, new RookPiece(false));
        this.squares[7][7] = new Square(7, 7, new RookPiece(false));

        this.squares[1][7] = new Square(1, 7, new BishopPiece(false));
        this.squares[6][7] = new Square(6, 7, new BishopPiece(false));

        this.squares[2][7] = new Square(2, 7, new KnightPiece(false));
        this.squares[5][7] = new Square(5, 7, new KnightPiece(false));

        this.squares[3][7] = new Square(3, 7, new QueenPiece(false));
        this.squares[4][7] = new Square(4, 7, new KingPiece(false));

        // assign black pawn second last column
        for(int i = 0; i < 8; i++) {
            this.squares[i][6] = new Square(i, 6, new PawnPiece(false));
        }
    }

    public boolean makeMove(Square start, Square end, Piece piece, Player player) {
        if (isWhitesTurn && !player.isWhite()) {
            System.out.println("Current player can't make this move!");
            return false;
        }

        Move move = new Move(start, end, piece, player);
        if (!move.isValidMove()) {
            System.out.println("Current player can't make this move!");
            return false;
        }
        move.makeMove();
        moveHistory.add(move);

        isWhitesTurn = !isWhitesTurn;
        return true;
    }
}

class Square {
    private int x;
    
    public int getX() {
        return x;
    }

    private int y;

    public int getY() {
        return y;
    }

    private Piece piece;

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Square(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
}

class Move {
    private Square start;
    private Square end;
    private Piece piece;
    private Player player;


    public Move(Square start, Square end, Piece piece, Player player) {
        this.start = start;
        this.end = end;
        this.piece = piece;
        this.player = player;
    }

    public boolean isValidMove() {
        // can't play other player pieces
        if (start.getPiece() == null) {
            return false;
        }
        if (piece.isWhite() != player.isWhite()) {
            return false;
        }
        if (this.piece.canMove(start, end)) {
            Piece startPiece = start.getPiece();
            Piece endPiece = end.getPiece();
            if (endPiece == null || startPiece.isWhite() != endPiece.isWhite()) {
                return true;
            }
        }
        return false;
    }

    public void makeMove() {
        start.setPiece(null);
        end.setPiece(piece);
    }
}

abstract class Piece {
    private boolean isWhite = false;
    public boolean isWhite() {
        return isWhite;
    }

    abstract boolean canMove(Square start, Square end);

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }
}

class PawnPiece extends Piece {
    public PawnPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean canMove(Square start, Square end) {
        // white will be on left
        if (isWhite()) {
            if (end.getY() + 1 == start.getY() &&  Math.abs(end.getX() - end.getY()) >= 0) {
                return true;
            }
        } else {
            if (end.getY() - 1 == start.getY() &&  Math.abs(end.getX() - end.getY()) >= 0) {
                return true;
            }
        }
        return false;
    }

}

class RookPiece extends Piece {

    public RookPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean canMove(Square start, Square end) {
        return start.getX() == end.getX() || start.getY() == end.getY();
    }
    
}

class KingPiece extends Piece {

    public KingPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean canMove(Square start, Square end) {
        // can be implemented for more detail for keeping this empty.
        return true;
    }

}

class QueenPiece extends Piece {

    public QueenPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean canMove(Square start, Square end) {
        // can be implemented for more detail for keeping this empty.
        return true;
    }

}

class BishopPiece extends Piece {

    public BishopPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean canMove(Square start, Square end) {
        // can be implemented for more detail for keeping this empty.
        return true;
    }
    
}

class KnightPiece extends Piece {
    public KnightPiece(boolean isWhite) {
        super(isWhite);
    }

    @Override
    boolean canMove(Square start, Square end) {
        // can be implemented for more detail for keeping this empty.
        return true;
    }
}