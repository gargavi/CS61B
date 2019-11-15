package tablut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.lang.Math.*;

import static tablut.Square.SQUARE_LIST;
import static tablut.Square.sq;
import static tablut.Board.THRONE;
import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            Move the_move = findMove();
            if (board().turn() != myPiece() || board().winner() != null) {
                _controller.reportError("misplaced move");
                continue;
            } else {
                if (the_move == null || !board().isLegal(the_move)){
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                    if (the_move == null){
                        System.out.println("What the fuc");
                    } else {
                        System.out.println(the_move.toString());
                    }continue;
                }
            }
            String name;
            String from = the_move.from().toString();
            String to = the_move.to().toString();
            if (from.charAt(0) == to.charAt(0)){
                name = "* " + from + "-" + to.charAt(1);
            } else {
                name = "* " + from + "-" + to.charAt(0);
            }
            System.out.println(name);
            return the_move.toString();
        }

    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        _lastFoundMove = null;
        int sense;
        if (myPiece() == WHITE){
            sense = 1;
        } else {
            sense = -1;
        }
        findMove(b, maxDepth(b), true, sense, -1 * INFTY, INFTY );
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (board.winner() != null || depth == 0){
            if (board.winner() == BLACK){
                return -1*WINNING_VALUE - depth ;
            } else if (board.winner() == WHITE){
                return WINNING_VALUE + depth;
            } else if (depth == 0){
                return staticScore(board);
            } else {
                return staticScore(board);
            }
        } else {
            int best = INFTY*-1*sense;
            Move best_pos = null;
            if (sense == 1){
                for (Move m: board.legalMoves(WHITE)){
                    board.makeMove(m);
                    int temp = findMove(board, depth - 1, false, sense*-1, alpha, beta);
                    board.undo();
                    if (temp > best){
                        best = temp;
                        best_pos = m;
                        alpha = max(alpha, temp);
                        if (beta <= alpha){
                            break;
                        }
                    }

                }
            } else {
                for (Move m: board.legalMoves(BLACK)){
                    board.makeMove(m);
                    int temp = findMove(board, depth -1, false, sense*-1, alpha, beta);
                    board.undo();
                    if (temp < best){
                        best = temp;
                        best_pos = m;
                        beta = min(beta, temp);
                        if (beta <= alpha){
                            break;
                        }
                    }
                }
            }
            if (saveMove){
                //System.out.println(best);
                _lastFoundMove = best_pos;
            }

            return best;
        }

    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board){
        return min(4, board.getLimit());
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int whites = board.get_white();
        int blacks = board.get_black();
//        Square b = board.kingPosition();
//        if (board.get(sq(b.col(), 8)) == EMPTY){
//            return WILL_WIN_VALUE;
//        } else if (board.get(sq(b.col(), 0))== EMPTY){
//            return WILL_WIN_VALUE;
//        } else if (board.get(sq(0, b.row())) == EMPTY){
//            return WILL_WIN_VALUE;
//        } else if (board.get(sq(8, b.row())) == EMPTY){
//            return WILL_WIN_VALUE;
//        }
        Square king_pos = board.kingPosition();
        int sub = 0;
        for (int i = 0; i < 4; i ++){
            Square temp = king_pos.rookMove(i, 1);
            if (board.get(temp) == BLACK){
                sub = sub - 8;
            }
        }
        int dis = Math.min(Math.min(9 - king_pos.col(), king_pos.col() - 0), Math.min(9 - king_pos.row(), king_pos.row()));
        int number_white = board.legalMoves(WHITE).size();
        int number_black = board.legalMoves(BLACK).size();
        int number_king = board.legalMoves(KING).size();
        return whites*2 - blacks + number_white/8 + number_black/4 + 4*number_king + _controller.randInt(5);
    }

}
