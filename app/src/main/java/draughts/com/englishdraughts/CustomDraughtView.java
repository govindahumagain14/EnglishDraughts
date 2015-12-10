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
    int whiteCoinValue = 2;
    int redCoinValue = 1;
    private int selectedPositionValue = 3;
    private int selectedCoinValue;


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
                    game[i][j] = 0;
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
                    if(selectedCoinValue == whiteCoinValue){
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                    } else if (selectedCoinValue == redCoinValue){
                        canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                    }
                }
                if (game[i][j] == whiteCoinValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                } else if (game[i][j] == redCoinValue) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
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
            Log.i(TAG, "X :: "+xPosition+" Y:: "+yPosition);
            if (yPosition >= 0 && xPosition < noOfCells) {
                // Coin is not selected
                if(mPreviousYPosition == -1) {
                    if(game[xPosition][yPosition] != whiteCoinValue && game[xPosition][yPosition] != redCoinValue){
                        return true;
                    }
                    // Is white turn
                   // if (isWhiteTurn && game[xPosition][yPosition] == whiteCoinValue){
                        setSelectedPosition(xPosition, yPosition);
                        invalidate();
                   /* } else if(!isWhiteTurn && game[xPosition][yPosition] == redCoinValue){
                        resetCurrentPosition();
                        invalidate();
                    }*/
                }
                // Coin is selected
                else {
                    if (mPreviousXPosition == xPosition && mPreviousYPosition == yPosition){
                        resetCurrentPosition();
                        invalidate();
                    }
                }
                /*if( ){

                } game[xPosition][yPosition] == whiteCoinValue) {
                    // If coin is not selected
                    if(mPreviousXPosition == -1) {

                    }
                    // If previous selected position is same selection, deselect the coin
                    else if (mPreviousXPosition == xPosition && mPreviousYPosition == yPosition){
                        resetCurrentPosition();
                        invalidate();
                    }
                    // Check is valid move and move the coin
                    else {

                    }
                }
                // Is Red turn
                else if (!isWhiteTurn && game[xPosition][yPosition] == redCoinValue) {
                    // If coin is not selected
                    if(mPreviousXPosition == -1) {
                        setSelectedPosition(xPosition, yPosition);
                        invalidate();
                    }
                    // If previous selected position is same selection, deselect the coin
                    else if (mPreviousXPosition == xPosition && mPreviousYPosition == yPosition){
                        resetCurrentPosition();
                        invalidate();
                    }
                    // Check is valid move and move the coin
                    else {

                    }
                }*/
            }
        }
        return true;
    }

    private void resetCurrentPosition() {
        if(isWhiteTurn){
            game[mPreviousXPosition][mPreviousYPosition] = whiteCoinValue;
        } else {
            game[mPreviousXPosition][mPreviousYPosition] = redCoinValue;
        }
        mPreviousXPosition = -1;
        mPreviousYPosition = -1;
        selectedCoinValue = -1;
    }

    private void setSelectedPosition(int xPosition, int yPosition) {
        mPreviousXPosition = xPosition;
        mPreviousYPosition = yPosition;
        game[xPosition][yPosition] = 3;
        if(isWhiteTurn){
            selectedCoinValue = whiteCoinValue;
        } else {
            selectedCoinValue = redCoinValue;
        }

    }
}
