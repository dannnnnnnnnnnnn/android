package e.dan.uitest;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameLogic implements Parcelable {
    public ArrayList<Button> buttons;
    private Button emptyButton;
    private Context context;
    private int moves = 0;
    private ArrayList<String> buttonTexts = new ArrayList<>();
    private TextView movesTextView;

    public GameLogic(ArrayList<Button> buttons, Context context) {
        this.buttons = buttons;
        for (Button b: buttons) {
            if (b.getText().equals("")) {
                emptyButton = b;
            }
        }
        this.context = context;
        this.movesTextView = (TextView) ((Activity) context).findViewById(R.id.textViewScore);
        ScrambleGameBoard();
        this.moves = 0;
        movesTextView.setText(String.valueOf(0));
    }

    public void onUpdate(ArrayList<Button> buttons, Context context, int moves) {
        this.buttons = buttons;
        this.context = context;
        this.moves = moves;
        this.movesTextView = (TextView) ((Activity) context).findViewById(R.id.textViewScore);
        setEmptyButton();
    }


    public void Move(Button b) {
        int emptyButtonRow = GetRow(buttons.indexOf(emptyButton));
        int emptyButtonCol = GetCol(buttons.indexOf(emptyButton));
        int bRow = GetRow(buttons.indexOf(b));
        int bCol = GetCol(buttons.indexOf(b));
        if (emptyButtonRow == bRow) {
            increaseMoves();
            int distance = GetDistance(emptyButtonCol, bCol);
            if (distance == 1) {
                SwapButtons(emptyButton, b);
                emptyButton = b;
            } else {
                if (emptyButtonCol < bCol) {
                    for (int i = 1; i <= distance; i++) {
                        int index = buttons.indexOf(emptyButton);
                        Button buttonToReplace = buttons.get(index + 1);
                        SwapButtons(emptyButton, buttonToReplace);
                        emptyButton = buttonToReplace;
                    }
                } else if (bCol < emptyButtonCol) {
                    for (int i = distance; 0 < distance; distance--) {
                        int index = buttons.indexOf(emptyButton);
                        Button buttonToReplace = buttons.get(index - 1);
                        SwapButtons(buttonToReplace, emptyButton);
                        emptyButton = buttonToReplace;

                    }
                }
            }
        } else if (emptyButtonCol == bCol) {
            increaseMoves();
            int distance = GetDistance(emptyButtonRow, bRow);
            if (distance == 1) {
                SwapButtons(emptyButton, b);
                emptyButton = b;
            } else {
                if (bRow < emptyButtonRow) {
                    for (int i = distance; 0 < distance; distance--) {
                        int index = buttons.indexOf(emptyButton);
                        Button buttonToReplace = buttons.get(index-4);
                        SwapButtons(buttonToReplace, emptyButton);
                        emptyButton = buttonToReplace;
                    }
                } else if ( emptyButtonRow < bRow) {
                    for (int i = 1; i <= distance; i++) {
                        int index = buttons.indexOf(emptyButton);
                        Button buttonToReplace = buttons.get(index+4);
                        SwapButtons(buttonToReplace, emptyButton);
                        emptyButton = buttonToReplace;
                    }
                }
            }
        }
    }
    public int GetRow(int i) {
        return i/4;
    }
    public int GetCol(int i) {
        return i%4;
    }
    public int GetDistance(int a, int b) {
        return Math.abs(a-b);
    }
    public int GetIntValue (Button b) {
        return Integer.valueOf(String.valueOf(b.getText()));
    }
    public void SwapButtons(Button a, Button b) {
        String s = String.valueOf(a.getText());
        a.setText(b.getText());
        b.setText(s);
    }
    public boolean isFinished() {

        for (int i = 0; i < buttons.size() - 2; i++) {
            if (String.valueOf(buttons.get(15).getText()).equals("")) {
                if (GetIntValue(buttons.get(i)) + 1 != GetIntValue(buttons.get(i + 1))) {
                    return false;
                }
            }
            if(!String.valueOf(buttons.get(15).getText()).equals("")) {
                return false;
            }
        }
        return true;
    }
    public void ScrambleGameBoard() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            Move(buttons.get(rand.nextInt(15)));
        }
        this.moves = 0;
    }
    public int getMoves() {
        return moves;
    }
    public ArrayList<String> getButtonTexts() {return buttonTexts;}
    public void increaseMoves() {
        moves++;
        movesTextView.setText(String.valueOf(moves));
    }

    public void setEmptyButton() {
        for (Button b: buttons) {
            if (b.getText().equals("")) {
                emptyButton = b;
            }
        }
    }
    public void fillButtonTexts() {
        buttonTexts = new ArrayList<>();
        for(int i = 0; i < buttons.size(); i++) {
            buttonTexts.add(String.valueOf(buttons.get(i).getText()));
        }
    }


    protected GameLogic(Parcel in) {
        if (in.readByte() == 0x01) {
            buttons = new ArrayList<Button>();
            in.readList(buttons, Button.class.getClassLoader());
        } else {
            buttons = null;
        }
        emptyButton = (Button) in.readValue(Button.class.getClassLoader());
        context = (Context) in.readValue(Context.class.getClassLoader());
        moves = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(moves);
        dest.writeList(buttonTexts);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GameLogic> CREATOR = new Parcelable.Creator<GameLogic>() {
        @Override
        public GameLogic createFromParcel(Parcel in) {
            return new GameLogic(in);
        }

        @Override
        public GameLogic[] newArray(int size) {
            return new GameLogic[size];
        }
    };
}