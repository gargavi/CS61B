package tablut;

import static java.lang.Math.*;

import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Avi Garg
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
            Move themove = findMove();
            if (board().turn() != myPiece() || board().winner() != null) {
                _controller.reportError("misplaced move");
                continue;
            } else {
                if (themove == null || !board().isLegal(themove)) {
                    _controller.reportError("Invalid move. "
                            + "Please try again.");
                    if (themove == null) {
                        System.out.println("What the fuc");
                    } else {
                        System.out.println(themove.toString());
                    }
                    continue;
                }
            }
            _controller.reportMove(themove);
            return themove.toString();
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
        if (myPiece() == WHITE) {
            sense = 1;
        } else {
            sense = -1;
        }
        findMove(b, maxDepth(b), true, sense, -1 * INFTY, INFTY);
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
        if (board.winner() != null || depth == 0) {
            if (board.winner() == BLACK) {
                return -1 * WINNING_VALUE - depth;
            } else if (board.winner() == WHITE) {
                return WINNING_VALUE + depth;
            } else if (depth == 0) {
                return staticScore(board);
            } else {
                return staticScore(board);
            }
        } else {
            int best = INFTY * -1 * sense;
            int newd = depth - 1;
            int news = sense * -1;
            Move bestpos = null;
            if (sense == 1) {
                for (Move m: board.legalMoves(WHITE)) {
                    board.makeMove(m);
                    int temp = findMove(board, newd, false, news, alpha, beta);
                    board.undo();
                    if (temp > best) {
                        best = temp;
                        bestpos = m;
                        alpha = max(alpha, temp);
                        if (beta <= alpha) {
                            break;
                        }
                    }

                }
            } else {
                for (Move m: board.legalMoves(BLACK)) {
                    board.makeMove(m);
                    int temp = findMove(board, newd, false, news, alpha, beta);
                    board.undo();
                    if (temp < best) {
                        best = temp;
                        bestpos = m;
                        beta = min(beta, temp);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            if (saveMove) {
                _lastFoundMove = bestpos;
            }

            return best;
        }

    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return min(4, board.getLimit());
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int whites = board.getWhite();
        int blacks = board.getBlack();
        int numberking = board.legalMoves(KING).size();
        return whites * 2 - blacks + 4 * numberking + _controller.randInt(5);
    }

}
