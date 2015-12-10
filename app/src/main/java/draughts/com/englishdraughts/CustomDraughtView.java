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
    private int intialValue = 0;
    private int redCoinValue = 1;
    private int whiteCoinValue = 2;
    private int selectedPositionValue = 3;
    private int possibleMoveValue = 4;
    private int selectedCoinValue;
    private boolean isValidTouch;


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

    private void init(Context context) {
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
        black.setStrokeWidth(10);
        black.setStyle(Paint.Style.STROKE);

        greenCell.setStrokeWidth(3);
        greenCell.setColor(Color.GREEN);
        Resources r = getResources();
        minYPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        initiateGame();
    }

    private void initiateGame() {
        game = new int[noOfCells][noOfCells];
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game.length; j++) {
                if (i < 3) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        game[i][j] = whiteCoinValue;
                    }
                } else if (i > game.length - 4) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        game[i][j] = redCoinValue;
                    }
                } else {
                    game[i][j] = intialValue;
                }
            }
        }
    }


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
                    if (selectedCoinValue == whiteCoinValue) {
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                    } else if (selectedCoinValue == redCoinValue) {
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                    }
                }
                if (game[i][j] == whiteCoinValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                } else if (game[i][j] == redCoinValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
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
            Log.i(TAG, "Value:: "+game[xPosition][yPosition]);
            if (yPosition >= 0 && xPosition < noOfCells) {
                // Coin is not selected
                if (mPreviousYPosition == -1) {
                    if (game[xPosition][yPosition] != whiteCoinValue && game[xPosition][yPosition] != redCoinValue) {
                        return true;
                    }
                    // Is white turn
                    if (isWhiteTurn && game[xPosition][yPosition] == whiteCoinValue) {
                        setWhiteSelectedPosition(xPosition, yPosition);
                        isValidTouch = true;
                    } else if (!isWhiteTurn && game[xPosition][yPosition] == redCoinValue) {
                        setRedSelectedPosition(xPosition, yPosition);
                        getPossibleMovementsForRed(xPosition, yPosition);
                        invalidate();
                    }
                }
                // Coin is selected
                else if (game[xPosition][yPosition] == selectedPositionValue) {
                    resetCurrentPosition(xPosition, yPosition);
                    isValidTouch = true;
                    invalidate();
                    return true;
                }
                // Check for coin movement and move to that position
                if (game[xPosition][yPosition] == possibleMoveValue) {
                    // TODO: If valid movement, turn the player from white to red and vice versa
                    if (!isWhiteTurn) {
                        game[xPosition][yPosition] = redCoinValue;

                    } else {

                    }
                    isValidTouch = true;
                    game[mPreviousXPosition][mPreviousYPosition] = intialValue;
                    isWhiteTurn = !isWhiteTurn;
                    mPreviousYPosition = -1;
                }
                if (isValidTouch) {
                    invalidate();
                }
            }
        }
        return true;
    }
    private void getPossibleMovementsForWhite(int xPosition, int yPosition) {
        // Is King
        if (false) {

        } else {
            // Check for right side moves
            checkRightRedPossibleMoves(xPosition, yPosition);
            // Check for left side moves
            checkLeftRedPossibleMoves(xPosition, yPosition);
        }
    }

    private void getPossibleMovementsForRed(int xPosition, int yPosition) {
        // Is King
        if (false) {

        } else {
            // Check for right side moves
            checkRightRedPossibleMoves(xPosition, yPosition);
            // Check for left side moves
            checkLeftRedPossibleMoves(xPosition, yPosition);
        }
    }

    private void checkRightRedPossibleMoves(int xPosition, int yPosition) {
        for (int i = xPosition - 1; i >= 0; i--) {
            for (int j = yPosition + 1; j < noOfCells; j++) {

                Log.i(TAG, " X right:: " + i);
                // If white coin exists
                if (game[i][j] == whiteCoinValue) {
                    // Check for empty space cross to this
                }
                // else, red coin exists
                else if (game[i][j] == redCoinValue) {
                    return;
                }
                // Empty space
                else if (game[i][j] != possibleMoveValue) {
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

    private void checkLeftRedPossibleMoves(int xPosition, int yPosition) {
        for (int i = xPosition - 1; i >= 0; i--) {
            for (int j = yPosition - 1; j >= 0; j--) {
                Log.i(TAG, " X left:: " + i);
                // If white coin exists
                if (game[i][j] == whiteCoinValue) {
                    // Check for empty space cross to this
                }
                // else, red coin exists
                else if (game[i][j] == redCoinValue) {
                    return;
                }
                // Empty space;
                else if (game[i][j] != possibleMoveValue) {
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

    private void resetCurrentPosition(int xPosition, int yPosition) {
        if (isWhiteTurn) {
            game[xPosition][yPosition] = whiteCoinValue;
        } else {
            game[xPosition][yPosition] = redCoinValue;
        }
        for (int i = 0; i < noOfCells; i++) {
            for (int j = 0; j < noOfCells; j++) {
                if (game[i][j] == possibleMoveValue) {
                    game[i][j] = intialValue;
                }
            }
        }
        mPreviousXPosition = -1;
        mPreviousYPosition = -1;
        selectedCoinValue = -1;
    }

    private void setWhiteSelectedPosition(int xPosition, int yPosition) {
        mPreviousXPosition = xPosition;
        mPreviousYPosition = yPosition;
        game[xPosition][yPosition] = 3;
        selectedCoinValue = whiteCoinValue;
    }

    private void setRedSelectedPosition(int xPosition, int yPosition) {
        mPreviousXPosition = xPosition;
        mPreviousYPosition = yPosition;
        game[xPosition][yPosition] = 3;
        selectedCoinValue = redCoinValue;
    }

}
