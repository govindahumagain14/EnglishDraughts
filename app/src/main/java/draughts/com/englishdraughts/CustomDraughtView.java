package draughts.com.englishdraughts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class CustomDraughtView extends View implements View.OnTouchListener {
    private int heightOfView;
    private int cellHeight;
    private Paint whiteLine = new Paint();
    private Paint redCoin = new Paint();
    private Paint whiteCoin = new Paint();
    private Rect rect;
    private Paint greenCell = new Paint();
    private Paint black = new Paint();
    private int[][] game;
    private int noOfCells = 8;
    private int mPreviousXPosition = -1;
    private int mPreviousYPosition = -1;
    private boolean isWhiteTurn = false;
    private String TAG = "View";
    private float minYPixels;
    private int inValidMoveValue = -1;
    private int intialValue = 0;
    private int redCoinValue = 1;
    private int whiteCoinValue = 2;
    private int selectedPositionValue = 3;
    private int possibleMoveValue = 4;
    private int possibleWhiteEatingValue = 5;
    private int possibleRedEatingValue = 6;
    private int redKingValue = 7;
    private int whiteKingValue = 8;
    private int selectedCoinValue;
    private boolean isValidTouch;
    private ArrayList<Position> possibleEatablePositions;
    private Context mContext;


    public CustomDraughtView(Context context) {
        super(context);
        init(context);
    }

    public CustomDraughtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomDraughtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the custom draught view
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        mPreviousXPosition = -1;
        mPreviousYPosition = -1;
        isWhiteTurn = false;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        heightOfView = display.getWidth();
        this.setOnTouchListener(this);
        cellHeight = heightOfView / noOfCells;

        whiteLine.setStrokeWidth(3);
        whiteLine.setColor(Color.WHITE);

        redCoin.setStrokeWidth(3);
        redCoin.setColor(Color.RED);
        redCoin.setStrokeCap(Paint.Cap.ROUND);

        whiteCoin.setStrokeWidth(3);
        whiteCoin.setColor(Color.WHITE);
        whiteCoin.setStrokeCap(Paint.Cap.ROUND);

        black.setColor(Color.BLACK);
        black.setStrokeWidth(8);
        black.setStyle(Paint.Style.STROKE);

        greenCell.setStrokeWidth(3);
        greenCell.setColor(Color.GREEN);
        Resources r = getResources();
        minYPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        initiateGame();
    }

    /**
     * Initializes the game with basic values.
     */
    private void initiateGame() {
        possibleEatablePositions = new ArrayList<Position>();
        game = new int[noOfCells][noOfCells];
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game.length; j++) {
                // Possible values for initial white coins
                if (i < (noOfCells / 2 - 1)) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1))
                        game[i][j] = whiteCoinValue;
                    else {
                        game[i][j] = inValidMoveValue;
                    }
                }
                // Possible values for initial red coins
                else if (i > game.length - noOfCells / 2) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1))
                        game[i][j] = redCoinValue;
                    else {
                        game[i][j] = inValidMoveValue;
                    }
                }
                // Either empty positions or invalid positions
                else {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        game[i][j] = intialValue;
                    } else {
                        game[i][j] = inValidMoveValue;
                    }
                }
            }
        }
    }

    /**
     * Draws the table and coins initially. This is responsible to draw table along with coins respectively.
     *
     * @param canvas
     */
    private void drawTable(Canvas canvas) {
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game.length; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                    rect = new Rect(cellHeight * i, cellHeight * j, cellHeight * i + cellHeight, cellHeight * j + cellHeight);
                    canvas.drawRect(rect, greenCell);
                }
            }
        }
        for (int i = 0; i < noOfCells; i++) {
            for (int j = 0; j < noOfCells; j++) {
                // Selected cell
                if (game[i][j] == selectedPositionValue) {
                    rect = new Rect(cellHeight * j, cellHeight * i, cellHeight * j + cellHeight, cellHeight * i + cellHeight);
                    canvas.drawRect(rect, black);
                    if (selectedCoinValue == whiteCoinValue || selectedCoinValue == possibleWhiteEatingValue) {
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                    } else if (selectedCoinValue == redCoinValue || selectedCoinValue == possibleRedEatingValue) {
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                    } else if (selectedCoinValue == redKingValue) {
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, black);
                    } else if (selectedCoinValue == whiteKingValue) {
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, black);
                    }
                }
                // Circles based on their values
                if (game[i][j] == whiteCoinValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                } else if (game[i][j] == redCoinValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                } else if (game[i][j] == whiteKingValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, black);
                } else if (game[i][j] == redKingValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, black);
                }

                if (game[i][j] == possibleMoveValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 6, black);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTable(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int touchXmax = cellHeight * 2 * noOfCells;
        int touchYmax = cellHeight * 2 * noOfCells;
        int touchXmin = 4;
        float touchYmin = minYPixels;
        float touchX = event.getRawX();
        float touchY = event.getRawY() - touchYmin;
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (touchX > touchXmin && touchX < touchXmax && touchY > touchYmin
                    && touchY < touchYmax) {
            }
            int yPosition = (int) (touchX / (cellHeight));
            int xPosition = (int) (touchY / (cellHeight));
            Log.i(TAG, "X :: " + xPosition + " Y:: " + yPosition);
            if (xPosition >= noOfCells || yPosition >= noOfCells)
                return true;
            Log.i(TAG, "Value:: " + game[xPosition][yPosition]);
            if (yPosition >= 0 && xPosition < noOfCells) {
                // Coin is not selected
                if (mPreviousYPosition == -1) {
                    if (game[xPosition][yPosition] != whiteCoinValue && game[xPosition][yPosition] != redCoinValue
                            && game[xPosition][yPosition] != whiteKingValue && game[xPosition][yPosition] != redKingValue) {
                        return true;
                    }
                    // Is white turn
                    if (isWhiteTurn) {
                        if (game[xPosition][yPosition] == whiteCoinValue || game[xPosition][yPosition] == whiteKingValue) {

                            setWhiteSelectedPosition(xPosition, yPosition);
                            getPossibleMovementsForWhite(xPosition, yPosition);
                            isValidTouch = true;
                        }
                    } else if (!isWhiteTurn) {
                        if (game[xPosition][yPosition] == redCoinValue || game[xPosition][yPosition] == redKingValue) {
                            setRedSelectedPosition(xPosition, yPosition);
                            getPossibleMovementsForRed(xPosition, yPosition);
                            invalidate();
                        }
                    }
                }
                // Reset the position, when Coin is selected and selected the same again
                else if (game[xPosition][yPosition] == selectedPositionValue) {
                    resetCurrentPosition(xPosition, yPosition);
                    isValidTouch = true;
                    invalidate();
                    return true;
                }
                // Check for coin movement and move to that position
                if (game[xPosition][yPosition] == possibleMoveValue) {
                    if (!isWhiteTurn) {
                        //if (game[xPosition][yPosition] != redKingValue)
                        game[xPosition][yPosition] = selectedCoinValue;
                        removeEatableWhitePositions(xPosition, yPosition);
                        removeBackwardEatableWhitePosition(xPosition, yPosition);
                        if (xPosition == 0) {
                            game[xPosition][yPosition] = redKingValue;
                        }
                    } else {
                        //if (game[xPosition][yPosition] != whiteKingValue)
                        game[xPosition][yPosition] = selectedCoinValue;
                        removeEatableRedPositions(xPosition, yPosition);
                        removeBackwardEatableRedPosition(xPosition, yPosition);
                        if (xPosition == noOfCells - 1) {
                            game[xPosition][yPosition] = whiteKingValue;
                        }
                    }
                    isValidTouch = true;
                    game[mPreviousXPosition][mPreviousYPosition] = intialValue;
                    resetPossibleValues();
                    isWhiteTurn = !isWhiteTurn;
                    mPreviousYPosition = -1;
                }
                // Invalidating when it is a valid touch
                if (isValidTouch) {
                    invalidate();
                }
            }
        }
        return true;
    }

    /**
     * To remove white coin and white king based on move of red king.
     *
     * @param xPosition
     * @param yPosition
     */
    private void removeBackwardEatableWhitePosition(int xPosition, int yPosition) {
        if (selectedCoinValue != redKingValue) {
            return;
        }
        int j = yPosition;
        for (int i = xPosition; i <= mPreviousXPosition; i = i + 2) {
            // If red moves to right
            if (mPreviousYPosition > yPosition && i + 1 < noOfCells && j + 1 < noOfCells) {
                game[i + 1][j + 1] = intialValue;
                j = j + 2;
            }
            // If red moves to left
            else if (mPreviousYPosition < yPosition && i + 1 < noOfCells && j - 1 >= 0) {
                game[i + 1][j - 1] = intialValue;
                j = j - 2;
            }
            // If Previous and present x position is same, then take right as priority and take that route
            else if (mPreviousYPosition == yPosition) {
                if (i + 2 < noOfCells && j + 2 < noOfCells) {
                    if (game[i + 2][j + 2] == possibleMoveValue
                            || game[i + 2][j + 2] == redKingValue) {
                        game[i + 1][j + 1] = intialValue;
                        removeBackwardEatableWhitePosition(i + 2, j + 2);
                    }
                } else if (i + 2 < noOfCells && j - 2 >= 0) {
                    if (game[i + 2][j - 2] == possibleMoveValue
                            || game[i + 2][j - 2] == redKingValue) {
                        game[i + 1][j - 1] = intialValue;
                        removeBackwardEatableWhitePosition(i + 2, j - 2);
                    }
                }
            }
        }
    }

    /**
     * To remove red coin and red king based on white king move.
     *
     * @param xPosition
     * @param yPosition
     */
    private void removeBackwardEatableRedPosition(int xPosition, int yPosition) {
        if (selectedCoinValue != whiteKingValue) {
            return;
        }
        int j = yPosition;
        for (int i = xPosition; i <= mPreviousXPosition; i = i + 2) {
            // If white moves to right
            if (mPreviousYPosition > yPosition && i + 1 < noOfCells && j + 1 < noOfCells) {
                game[i + 1][j + 1] = intialValue;
                j = j + 2;
            }
            // If white moves to left
            else if (mPreviousYPosition < yPosition && i + 1 < noOfCells && j - 1 >= 0) {
                game[i + 1][j - 1] = intialValue;
                j = j - 2;
            }
            // If Previous and present x position is same, then take right as priority and take that route
            else if (mPreviousYPosition == yPosition) {
                if (i + 2 < noOfCells && j + 2 < noOfCells) {
                    if (game[i + 2][j + 2] == possibleMoveValue
                            || game[i + 2][j + 2] == whiteKingValue) {
                        game[i + 1][j + 1] = intialValue;
                        removeBackwardEatableRedPosition(i + 2, j + 2);
                    }
                } else if (i + 2 < noOfCells && j - 2 >= 0) {
                    if (game[i + 2][j - 2] == possibleMoveValue
                            || game[i + 2][j - 2] == whiteKingValue) {
                        game[i + 1][j - 1] = intialValue;
                        removeBackwardEatableRedPosition(i + 2, j - 2);
                    }
                }
            }
        }
    }

    /**
     * This method removes red coin and red king based on white move.
     *
     * @param xPosition
     * @param yPosition
     */
    private void removeEatableRedPositions(int xPosition, int yPosition) {
        int j = yPosition;
        for (int i = xPosition; i >= mPreviousXPosition; i = i - 2) {
            // If white moves to right
            if (yPosition < mPreviousYPosition && i - 1 >= 0 && j + 1 < noOfCells
                    && game[i - 1][j + 1] != whiteCoinValue) {
                game[i - 1][j + 1] = intialValue;
                j = j + 2;
            }
            // If white moves to left
            else if (yPosition > mPreviousYPosition && i - 1 >= 0 && j - 1 >= 0
                    && game[i - 1][j - 1] != whiteCoinValue) {
                game[i - 1][j - 1] = intialValue;
                j = j - 2;
            } else if (mPreviousYPosition == yPosition) {
                if (i - 2 >= 0 && j + 2 < noOfCells) {
                    if (game[i - 2][j + 2] == possibleMoveValue
                            || game[i - 2][j + 2] == whiteCoinValue) {
                        game[i - 1][j + 1] = intialValue;
                        removeEatableRedPositions(i - 2, j + 2);
                    }
                } else if (i - 2 >= 0 && j - 2 >= 0) {
                    if (game[i - 2][j - 2] == possibleMoveValue
                            || game[i - 2][j - 2] == whiteCoinValue) {
                        game[i - 1][j - 1] = intialValue;
                        removeEatableRedPositions(i - 2, j - 2);
                    }
                }
            }
        }
    }

    /**
     * This method removes the white coin and king based on red move.
     *
     * @param xPosition
     * @param yPosition
     */
    private void removeEatableWhitePositions(int xPosition, int yPosition) {
        int j = yPosition;
        for (int i = xPosition; i <= mPreviousXPosition; i = i + 2) {
            // If red moves to right
            if (mPreviousYPosition > yPosition && i + 1 < noOfCells && j + 1 < noOfCells) {
                game[i + 1][j + 1] = intialValue;
                j = j + 2;
            }
            // If red moves to left
            else if (mPreviousYPosition < yPosition && i + 1 < noOfCells && j - 1 >= 0) {
                game[i + 1][j - 1] = intialValue;
                j = j - 2;
            }
            // If Previous and present x position is same, then take right as priority and take that route
            else if (mPreviousYPosition == yPosition) {
                if (i + 2 < noOfCells && j + 2 < noOfCells) {
                    if (game[i + 2][j + 2] == possibleMoveValue
                            || game[i + 2][j + 2] == redCoinValue) {
                        game[i + 1][j + 1] = intialValue;
                        removeEatableWhitePositions(i + 2, j + 2);
                    }
                } else if (i + 2 < noOfCells && j - 2 >= 0) {
                    if (game[i + 2][j - 2] == possibleMoveValue
                            || game[i + 2][j - 2] == redCoinValue) {
                        game[i + 1][j - 1] = intialValue;
                        removeEatableWhitePositions(i + 2, j - 2);
                    }
                }
            }
        }
    }

    /**
     * Gets the possible moves for white coin.
     *
     * @param xPosition
     * @param yPosition
     */
    private void getPossibleMovementsForWhite(int xPosition, int yPosition) {
        // Is King
        if (selectedCoinValue == whiteKingValue) {
            checkbackwardLeftWhitePossibleMoves(xPosition, yPosition, false);
            checkbackwardRightWhitePossibleMoves(xPosition, yPosition, false);
        }
        // Check for right side moves
        checkRightWhitePossibleMoves(xPosition, yPosition, false);
        // Check for left side moves
        checkLeftWhitePossibleMoves(xPosition, yPosition, false);
    }

    /**
     * This method gets possible left moves for white king.
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossiblePosition
     */
    private void checkbackwardLeftWhitePossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossiblePosition) {
        for (int i = xPosition - 1; i >= 0; i--) {
            for (int j = yPosition - 1; j >= 0; j--) {
                Log.i(TAG, " X left:: " + i);
                // If white coin exists
                if (game[i][j] == redCoinValue || game[i][j] == redKingValue) {
                    if (i - 1 >= 0 && j - 1 >= 0) {
                        // Check for empty space cross to this
                        if ((game[i - 1][j - 1] == whiteCoinValue || game[i - 1][j - 1] == redCoinValue
                                || game[i - 1][j - 1] == redKingValue || game[i - 1][j - 1] == redKingValue)) {
                            return;
                        } else {
                            game[i - 1][j - 1] = possibleMoveValue;
                            checkbackwardLeftWhitePossibleMoves(i - 1, j - 1, true);
                            checkbackwardRightWhitePossibleMoves(i - 1, j - 1, true);
                            return;
                        }
                    }
                }
                // else, red coin or red king exists
                else if (game[i][j] == whiteCoinValue || game[i][j] == whiteKingValue) {
                    return;
                }
                // Empty space;
                else if (game[i][j] != possibleMoveValue && game[i][j] != inValidMoveValue
                        && game[i][j] != whiteKingValue && !isAlreadyPossiblePosition) {
                    Log.i(TAG, "pOSS left: X:: " + (i) + " Y:: " + (j));
                    game[i][j] = possibleMoveValue;
                    return;
                }
                // Possible value
                else if (game[i][j] == possibleMoveValue) {
                    return;
                }
            }
        }
    }

    /**
     * This method gets the possible right moves for white king.
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossibleMove
     */
    private void checkbackwardRightWhitePossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossibleMove) {
        int j = yPosition;
        for (int i = xPosition; i >= 0; i = i - 2) {
            if (i - 1 >= 0 && j + 1 < noOfCells) {
                // If red coin exists
                if (game[i - 1][j + 1] == redCoinValue || game[i - 1][j + 1] == redKingValue) {
                    if (i - 2 >= 0 && j + 2 < noOfCells && game[i - 2][j + 2] == intialValue) {
                        game[i - 2][j + 2] = possibleMoveValue;
                        checkbackwardRightWhitePossibleMoves(i - 2, j + 2, true);
                        checkbackwardLeftWhitePossibleMoves(i - 2, j + 2, true);
                        return;
                    }
                } else if (isAlreadyPossibleMove || game[i - 1][j + 1] == whiteCoinValue || game[i - 1][j + 1] == whiteKingValue) {
                    return;
                } else {
                    game[i - 1][j + 1] = possibleMoveValue;
                    return;
                }
            }
            j = j + 2;
            if (j > noOfCells - 1 && i < 0)
                return;
        }
    }

    /**
     * This methods gets the possible moves for white coin.
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossiblePosition
     */
    private void checkLeftWhitePossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossiblePosition) {
        for (int i = xPosition + 1; i < noOfCells; i++) {
            for (int j = yPosition - 1; j >= 0; j--) {
                Log.i(TAG, " X left:: " + i);
                // If red coin exists
                if (game[i][j] == redCoinValue || game[i][j] == whiteKingValue) {
                    // Check for empty space cross to this
                    if (i + 1 < noOfCells && j - 1 >= 0) {
                        // Check for empty space cross to this
                        if (game[i + 1][j - 1] == whiteCoinValue || game[i + 1][j - 1] == redCoinValue
                                || game[i + 1][j - 1] == whiteKingValue || game[i + 1][j - 1] == redKingValue) {
                            return;
                        } else {
                            Position position = new Position();
                            position.xPosition = i;
                            position.yPosition = j;
                            possibleEatablePositions.add(position);
                            //game[i][j] = possibleRedEatingValue;
                            game[i + 1][j - 1] = possibleMoveValue;
                            checkLeftWhitePossibleMoves(i + 1, j - 1, true);
                            checkRightWhitePossibleMoves(i + 1, j - 1, true);
                            return;
                        }
                    }
                }
                // else, white coin exists
                else if (game[i][j] == whiteCoinValue || game[i][j] == whiteKingValue) {
                    return;
                }
                // Empty space;
                else if (game[i][j] != possibleMoveValue && game[i][j] != inValidMoveValue
                        && game[i][j] != whiteKingValue && !isAlreadyPossiblePosition) {
                    Log.i(TAG, "pOSS left: X:: " + (i) + " Y:: " + (j));
                    game[i][j] = possibleMoveValue;
                    return;
                }
                // Possible value
                else if (game[i][j] == possibleMoveValue) {
                    return;
                }
            }
        }
    }

    /**
     * This method gets the possible moves for white coin.
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossiblePosition
     */
    private void checkRightWhitePossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossiblePosition) {
        for (int i = xPosition + 1; i < noOfCells; i++) {
            for (int j = yPosition + 1; j < noOfCells; j++) {

                Log.i(TAG, " X right:: " + i);
                // If red coin exists
                if (game[i][j] == redCoinValue || game[i][j] == whiteKingValue) {
                    // Check for empty space cross to this
                    if (i + 1 < noOfCells && j + 1 < noOfCells) {
                        // Check for empty space cross to this
                        if (game[i + 1][j + 1] == whiteCoinValue || game[i + 1][j + 1] == redCoinValue
                                || game[i + 1][j + 1] == whiteKingValue || game[i + 1][j + 1] == redKingValue) {
                            return;
                        } else {
                            Position position = new Position();
                            position.xPosition = i;
                            position.yPosition = j;
                            possibleEatablePositions.add(position);
                            game[i + 1][j + 1] = possibleMoveValue;
                            checkLeftWhitePossibleMoves(i + 1, j + 1, true);
                            checkRightWhitePossibleMoves(i + 1, j + 1, true);
                            return;
                        }
                    }
                }
                // else, white coin exists
                else if (game[i][j] == whiteCoinValue || game[i][j] == whiteKingValue) {
                    return;
                }
                // Empty space
                else if (game[i][j] != possibleMoveValue && game[i][j] != inValidMoveValue
                        && game[i][j] != whiteKingValue && !isAlreadyPossiblePosition) {
                    Log.i(TAG, "pOSS right: X:: " + (i) + " Y:: " + (j));
                    game[i][j] = possibleMoveValue;
                    return;
                }
                // Possible value
                else if (game[i][j] == possibleMoveValue) {
                    return;
                }
            }
        }
    }

    /**
     * Gets the possible moves for red coin
     *
     * @param xPosition
     * @param yPosition
     */
    private void getPossibleMovementsForRed(int xPosition, int yPosition) {
        // Is King
        if (selectedCoinValue == redKingValue) {
            checkbackwardLeftRedPossibleMoves(xPosition, yPosition, false);
            checkbackwardRightRedPossibleMoves(xPosition, yPosition, false);
        }
        // Check for right side moves
        checkRightRedPossibleMoves(xPosition, yPosition, false);
        // Check for left side moves
        checkLeftRedPossibleMoves(xPosition, yPosition, false);
    }

    /**
     * This method checks the possible backward right moves for red king.
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossibleMove
     */
    private void checkbackwardRightRedPossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossibleMove) {

        int j = yPosition;
        for (int i = xPosition; i < noOfCells; i = i + 2) {
            if (i + 1 < noOfCells && j + 1 < noOfCells) {
                Log.i(TAG, "X backward right X:: " + i + " Y:: " + j);
                // If white coin exists
                if (game[i + 1][j + 1] == whiteCoinValue || game[i + 1][j + 1] == whiteKingValue) {
                    if (i + 2 < noOfCells && j + 2 < noOfCells && game[i + 2][j + 2] == intialValue) {
                        game[i + 2][j + 2] = possibleMoveValue;
                        checkbackwardRightRedPossibleMoves(i + 2, j + 2, true);
                        checkbackwardLeftRedPossibleMoves(i + 2, j + 2, true);
                        return;
                    }
                } else if (isAlreadyPossibleMove || game[i + 1][j + 1] == redCoinValue || game[i + 1][j + 1] == redKingValue)
                    return;
                else {
                    game[i + 1][j + 1] = possibleMoveValue;
                    return;
                }
            }
            j = j + 2;
            if (j > noOfCells - 1 && i > noOfCells - 1)
                return;
        }
    }

    /**
     * This method checks the backward left possible red moves for the selected red king.
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossibleMove
     */
    private void checkbackwardLeftRedPossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossibleMove) {

        int j = yPosition;
        for (int i = xPosition; i < noOfCells; i = i + 2) {
            if (i + 1 < noOfCells && j - 1 >= 0) {
                Log.i(TAG, "X backward left X:: " + i + " Y:: " + j);
                // If white coin exists
                if (game[i + 1][j - 1] == whiteCoinValue || game[i + 1][j - 1] == whiteKingValue) {
                    if (i + 2 < noOfCells && j - 2 >= 0 && game[i + 2][j - 2] == intialValue) {
                        game[i + 2][j - 2] = possibleMoveValue;
                        checkbackwardRightRedPossibleMoves(i + 2, j - 2, true);
                        checkbackwardLeftRedPossibleMoves(i + 2, j - 2, true);
                        return;
                    }
                } else if (isAlreadyPossibleMove || game[i + 1][j - 1] == redCoinValue || game[i + 1][j - 1] == redKingValue)
                    return;
                else {
                    game[i + 1][j - 1] = possibleMoveValue;
                    return;
                }
            }
            j = j - 2;
            if (j < 0 && i > noOfCells - 1)
                return;
        }
    }

    /**
     * This method checks possible right red moves for the selected position
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossiblePosition
     */
    private void checkRightRedPossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossiblePosition) {
        for (int i = xPosition - 1; i >= 0; i--) {
            for (int j = yPosition + 1; j < noOfCells; j++) {

                Log.i(TAG, " X right:: " + i);
                // If white coin exists
                if (game[i][j] == whiteCoinValue) {
                    if (i - 1 >= 0 && j + 1 < noOfCells) {
                        // Check for empty space cross to this
                        if (game[i - 1][j + 1] == whiteCoinValue || game[i - 1][j + 1] == redCoinValue) {
                            return;
                        } else {
                            Position position = new Position();
                            position.xPosition = i;
                            position.yPosition = j;
                            possibleEatablePositions.add(position);
                            game[i - 1][j + 1] = possibleMoveValue;
                            checkLeftRedPossibleMoves(i - 1, j + 1, true);
                            checkRightRedPossibleMoves(i - 1, j + 1, true);
                            return;
                        }
                    }
                }
                // else, red coin exists
                else if (game[i][j] == redCoinValue) {
                    return;
                }
                // Empty space
                else if (game[i][j] != possibleMoveValue && game[i][j] != inValidMoveValue && !isAlreadyPossiblePosition) {
                    Log.i(TAG, "pOSS right: X:: " + (i) + " Y:: " + (j));
                    game[i][j] = possibleMoveValue;
                    return;
                }
                // Possible value
                else if (game[i][j] == possibleMoveValue) {
                    return;
                }
            }
        }
    }

    /**
     * This method checks possible left moves for normal red coin
     *
     * @param xPosition
     * @param yPosition
     * @param isAlreadyPossiblePosition
     */
    private void checkLeftRedPossibleMoves(int xPosition, int yPosition, boolean isAlreadyPossiblePosition) {
        for (int i = xPosition - 1; i >= 0; i--) {
            for (int j = yPosition - 1; j >= 0; j--) {
                Log.i(TAG, " X left:: " + i);
                // If white coin exists
                if (game[i][j] == whiteCoinValue) {
                    if (i - 1 >= 0 && j - 1 >= 0) {
                        // Check for empty space cross to this
                        if ((game[i - 1][j - 1] == whiteCoinValue || game[i - 1][j - 1] == redCoinValue)) {
                            return;
                        } else {
                            Position position = new Position();
                            position.xPosition = i;
                            position.yPosition = j;
                            possibleEatablePositions.add(position);
                            game[i - 1][j - 1] = possibleMoveValue;
                            checkLeftRedPossibleMoves(i - 1, j - 1, true);
                            checkRightRedPossibleMoves(i - 1, j - 1, true);
                            return;
                        }
                    }
                }
                // else, red coin exists
                else if (game[i][j] == redCoinValue) {
                    return;
                }
                // Empty space;
                else if (game[i][j] != possibleMoveValue && game[i][j] != inValidMoveValue && !isAlreadyPossiblePosition) {
                    Log.i(TAG, "pOSS left: X:: " + (i) + " Y:: " + (j));
                    game[i][j] = possibleMoveValue;
                    return;
                }
                // Possible value
                else if (game[i][j] == possibleMoveValue) {
                    return;
                }
            }
        }
    }

    /**
     * To reset the selected position
     *
     * @param xPosition
     * @param yPosition
     */
    private void resetCurrentPosition(int xPosition, int yPosition) {
        game[xPosition][yPosition] = selectedCoinValue;
        resetPossibleValues();
        mPreviousXPosition = -1;
        mPreviousYPosition = -1;
        selectedCoinValue = -1;
        possibleEatablePositions.clear();
    }

    /**
     * To reset the possible values of the selected coin
     */
    private void resetPossibleValues() {
        for (int i = 0; i < noOfCells; i++) {
            for (int j = 0; j < noOfCells; j++) {
                if (game[i][j] == possibleMoveValue) {
                    game[i][j] = intialValue;
                }
            }
        }
    }

    /**
     * Sets the white selected position
     *
     * @param xPosition
     * @param yPosition
     */
    private void setWhiteSelectedPosition(int xPosition, int yPosition) {
        mPreviousXPosition = xPosition;
        mPreviousYPosition = yPosition;
        selectedCoinValue = game[xPosition][yPosition];
        game[xPosition][yPosition] = 3;
    }

    /**
     * Sets the red selected position
     *
     * @param xPosition
     * @param yPosition
     */
    private void setRedSelectedPosition(int xPosition, int yPosition) {
        mPreviousXPosition = xPosition;
        mPreviousYPosition = yPosition;
        selectedCoinValue = game[xPosition][yPosition];
        game[xPosition][yPosition] = 3;
    }

    /**
     * Resets the game with desired board size
     *
     * @param boardSize
     */
    public void resetGame(int boardSize) {
        noOfCells = boardSize;
        init(mContext);
        initiateGame();
        possibleEatablePositions.clear();
        invalidate();
    }
}
