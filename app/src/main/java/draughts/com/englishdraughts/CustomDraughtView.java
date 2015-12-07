package draughts.com.englishdraughts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class CustomDraughtView extends View {
    private int heightOfView;
    private int cellHeight;
    private Paint whiteLine = new Paint();
    private Paint redCoin = new Paint();
    private Rect rect;
    private Paint greenCell = new Paint();
    private ArrayList<Coin><Coin> game;

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
        cellHeight = heightOfView / 10;

        whiteLine.setStrokeWidth(3);
        whiteLine.setColor(Color.WHITE);

        redCoin.setStrokeWidth(3);
        redCoin.setColor(Color.RED);

        greenCell.setStrokeWidth(3);
        greenCell.setColor(Color.GREEN);
        initiateGame();
    }

    private void initiateGame() {
        game = new ArrayList<>();
        for(int i=0;i<10;i++) {
            for (int j = 0; j < 10; j++) {
game.get[j][i]
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

        for(int i=0;i<10;i++,i++){
            for(int j=0;j<10;j++,j++){
//Left = where, top=where, right=width of rect, bottom=height of rect
                rect = new Rect(cellHeight * i, cellHeight * j , cellHeight * i+cellHeight , cellHeight * j + cellHeight);

                canvas.drawRect(rect, greenCell);
            }
        }

        for(int i=1;i<10;i++,i++){
            for(int j=1;j<10;j++,j++){
//Left = where, top=where, right=width of rect, bottom=height of rect
                rect = new Rect(cellHeight * i, cellHeight * j , cellHeight * i+cellHeight , cellHeight * j + cellHeight);

                canvas.drawRect(rect, greenCell);
            }
        }
        for(int i=0;i<10;i++) {
            for (int j = 0; j < 10; j++) {

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
        // draw circle
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


}
