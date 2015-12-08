package draughts.com.englishdraughts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
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
    private int mPreviousXPosition;
    private int mPreviousYPosition;

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

       /* redCoin.setShader(new LinearGradient(0.40f, 0.0f, 100.60f, 100.0f,
                Color.BLACK,
                Color.BLACK,
                Shader.TileMode.CLAMP));*/

        greenCell.setStrokeWidth(3);
        greenCell.setColor(Color.GREEN);
        initiateGame();
    }

    private void initiateGame() {
        game = new int[noOfCells][noOfCells];
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game.length; j++) {
                if (i < 3) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        game[i][j] = 1;
                    }
                } else if (i > game.length - 4) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        game[i][j] = 2;
                    }
                } else {
                    game[i][j] = 0;
                }
            }
        }
    }


    private void drawTable(Canvas canvas) {
        //Horizontal lines
        /*canvas.drawLine(0, 0, heightOfView, 0, whiteLine);
        for (int i = 1; i <= 10; i++) {
            canvas.drawLine(0, cellHeight * i, heightOfView, cellHeight * i, whiteLine);
        }*/
        //Vertical lines
       /* canvas.drawLine(0, 0, 0, heightOfView, whiteLine);
        for (int i = 1; i <= 10; i++) {
            canvas.drawLine(cellHeight * i, 0, cellHeight * i, heightOfView, whiteLine);
        }*/
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game.length; j++) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        rect = new Rect(cellHeight * i, cellHeight * j, cellHeight * i + cellHeight, cellHeight * j + cellHeight);
                        canvas.drawRect(rect, greenCell);
                    }
            }
        }
        /*for (int i = 0; i < noOfCells; i++, i++) {
            for (int j = 0; j < noOfCells; j++, j++) {
            //Left = where, top=where, right=width of rect, bottom=height of rect
                rect = new Rect(cellHeight * i, cellHeight * j, cellHeight * i + cellHeight, cellHeight * j + cellHeight);
                canvas.drawRect(rect, greenCell);
            }
        }

        for (int i = 1; i < noOfCells; i++, i++) {
            for (int j = 1; j < noOfCells; j++, j++) {
                //Left = where, top=where, right=width of rect, bottom=height of rect
                rect = new Rect(cellHeight * i, cellHeight * j, cellHeight * i + cellHeight, cellHeight * j + cellHeight);

                canvas.drawRect(rect, greenCell);
            }
        }*/
        for (int i = 0; i < noOfCells; i++) {
            for (int j = 0; j < noOfCells; j++) {
                // Selected cell
                if (game[i][j] == 3) {
                    rect = new Rect(cellHeight * j, cellHeight * i, cellHeight * j + cellHeight, cellHeight * i + cellHeight);
                    canvas.drawRect(rect, black);
                }
                if (game[i][j] == 1) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, whiteCoin);
                } else if (game[i][j] == 2) {
                    canvas.drawCircle(cellHeight / 2 * (j * 2) + cellHeight / 2, cellHeight / 2 * (i * 2) + cellHeight / 2, cellHeight / 4, redCoin);
                }

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // startX, float startY, float stopX, float stopY, Paint paint
        drawTable(canvas);
        // rect = new Rect(0, 0, cellHeight, cellHeight);
        // canvas.drawRect(rect, redCoin);
        // draw circle CX, XY, radius, paint
        // canvas.drawCircle(cellHeight / 2, cellHeight / 2, cellHeight / 4, whiteLine);
        //rect = new Rect(cellHeight, cellHeight, cellHeight * 2, cellHeight * 2);
        //canvas.drawRect(rect, redCoin);
        //left <= right and top <= bottom).
        //Left = where, top=where, right=width of rect, bottom=height of rect
        //  rect = new Rect(cellHeight * 2, cellHeight * 2+cellHeight, cellHeight * 2 , cellHeight * 2 + cellHeight);

        // canvas.drawRect(rect, redCoin);
        //canvas.drawLine(0, 0, heightOfView, heightOfView, whiteLine);
        // rect = new Rect(cellHeight, 0, cellHeight*2, cellHeight);
        // canvas.drawRect(rect, redCoin);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int touchXmax = cellHeight * 2 * noOfCells;
        int touchYmax = cellHeight * 2 * noOfCells;
        int touchXmin = 4;
        int touchYmin = 150;
        float touchX = event.getRawX();
        float touchY = event.getRawY() - 150;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.i("srikanth", "Cell ::x:: " + (int) (touchX / (cellHeight))
                    + " Y:: " + (int) (touchY / (cellHeight)));
            Log.i("srikanth", "touch up" + event.getRawX() + "touch Y :: "
                    + event.getRawY());

            if (touchX > touchXmin && touchX < touchXmax && touchY > touchYmin
                    && touchY < touchYmax) {
                Log.i("srikanth", "Rect size:: " + cellHeight);
            }
            int yPosition = (int) (touchX / (cellHeight));
            int xPosition = (int) (touchY / (cellHeight));
            if (yPosition >= 0 && xPosition <= 7) {

                Log.i("srikanth", "Rect x:: " + yPosition +" y:: "+xPosition);
                mPreviousXPosition = xPosition;
                mPreviousYPosition = yPosition;
                game[xPosition][yPosition] = 3;

               invalidate();
            }
        }
        return true;
    }


}
