package e.dan.uitest;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TIMECOUNT_KEY = "timer";
    TextView movesTextView;
    Button createNewGameButton;
    GameLogic gl;
    ArrayList<Button> buttons;
    Timer timer;
    TextView timeTextView;
    int timeCount;


    private final String GAMELOGIC_KEY = "gamelogic";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNewGameButton = (Button) findViewById(R.id.newGameButton);
        createNewGameButton.setVisibility(View.GONE);
        timeTextView = findViewById(R.id.textViewTime);
        if (savedInstanceState != null) {
            timeCount = savedInstanceState.getInt(TIMECOUNT_KEY);
            gl = savedInstanceState.getParcelable(GAMELOGIC_KEY);
            int moves = gl.getMoves();
            movesTextView = (TextView) findViewById(R.id.textViewScore);
            movesTextView.setText(String.valueOf(moves));
            ArrayList<String> buttonTexts = gl.getButtonTexts();
            buttons = new ArrayList<>();
            for (int i = 1; i <= 16; i++) {
                    int id = getResources().getIdentifier("button" + i, "id", getPackageName());
                    Button b = ((Button) findViewById(id));
                    b.setText(buttonTexts.get(i-1));
                    buttons.add(b);
            }
            gl.onUpdate(buttons, this, moves);
            createTimer();

        } else {
            createNewGame();
        }
        }

    public void MakeMove(View view) {
        gl.Move((Button) view);
        if (gl.isFinished()) {
            createNewGameButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, "You win!", Toast.LENGTH_LONG).show();
            timer.cancel();
        }
    }
    public void createNewGame() {
        createTimer();
        timeCount = 0;
        buttons = new ArrayList<Button>();
        for (int i = 1; i <= 16; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            buttons.add((Button) findViewById(id));
        }
        gl = new GameLogic(buttons, this);
        createNewGameButton.setVisibility(View.GONE);
    }
    public void createNewGameButtonOnClick(View view) {
       createNewGame();
    }
    public void createTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  timeTextView.setText(String.valueOf(timeCount));
                                                  timeCount++;
                                              }
                                          });
                                      }
                                  }, 1000, 1000
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
                gl.fillButtonTexts();
                outState.putParcelable(GAMELOGIC_KEY, gl);
                outState.putInt(TIMECOUNT_KEY, timeCount);
                super.onSaveInstanceState(outState);
    }

}
