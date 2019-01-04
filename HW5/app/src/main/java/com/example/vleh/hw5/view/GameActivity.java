package com.example.vleh.hw5.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vleh.hw5.R;
import com.example.vleh.hw5.common.Constants;
import com.example.vleh.hw5.controller.Controller;
import com.example.vleh.hw5.net.OutputHandler;

import java.io.IOException;

public class GameActivity extends AppCompatActivity implements OutputHandler {

    private Controller contr;

    // ui related
    private TextView statusField;
    private TextView scoreField;
    private TextView guessField;
    private TextView stateField;
    private TextView attempField;
    private Button guessButton;
    private Button startButton;
    private Button disconnectButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // connect to server
        contr = new Controller();
        new ConnectTask().execute(contr);

        // wire the ui
        setupActivity();
    }

    // Setup activity
    private void setupActivity() {

        // get ui controls
        statusField = (TextView) findViewById(R.id.statusField);
        scoreField = (TextView) findViewById(R.id.scoreField);
        stateField = (TextView) findViewById(R.id.stateField);
        guessField = (TextView) findViewById(R.id.guessField);
        attempField = (TextView) findViewById(R.id.attempField);
        guessButton = (Button) findViewById(R.id.guessButton);
        startButton = (Button) findViewById(R.id.startButton);
        disconnectButton = (Button) findViewById(R.id.disconnectButton);

        // initially disable start and guess buttons and guessField
        guessButton.setEnabled(false);
        guessField.setFocusable(false);
        startButton.setEnabled(false);

        // set listener for guessButton
        guessButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                boolean hasErrors = false;

                // check hostname is not empty
                if (TextUtils.isEmpty(guessField.getText())) {
                    guessField.setError("Please specify a letter or word to guess");
                    hasErrors = true;
                }

                if (hasErrors) {
                    return;
                }

                // if no errors, send guess to server using controller
                new GuessTask().execute(guessField.getText().toString());

                // empty the textbox
                guessField.setText("");
            }
        });

        // set listener for startButton
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new StartTask().execute();
            }
        });

        // set listener for disconnectButton
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // cut the connection
                new DisconnectTask().execute();

                // go back to main activity
                GameActivity.super.onBackPressed();
            }
        });
    }

    /*
     * Asynctask that get called when this activity is created
     */


    private class ConnectTask extends AsyncTask<Controller, Integer, String> {
        @Override
        protected String doInBackground(Controller... params) {
            Bundle extras = getIntent().getExtras();

            // connect the controller
            contr =  params[0];
            try {
                contr.connect(extras.getString(Constants.EXTRA_HOSTNAME), extras.getInt(Constants.EXTRA_PORT), GameActivity.this);
            } catch (IOException e) {
                return Constants.ASYNC_ERROR;
            }

            return "";
        }

        @Override
        protected void onPostExecute(String res) {

            // check if error connecting
            if (res.equals(Constants.ASYNC_ERROR)) {

                // show error message on dialog and then exit this activity
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Error connecting to server");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                GameActivity.super.onBackPressed();
                            }
                        });
                alertDialog.show();
                return;
            }

            startButton.setEnabled(true);
            statusField.setText("Connected");
        }
    }

    /*
     * Asynctasks that get called when buttons are clicked
     */

    // send guess
    private class GuessTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... guess) {
            contr.sendGuess(guess[0], GameActivity.this);
            return "";
        }
    }

    // send start
    private class StartTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... guess) {
            contr.sendStart(GameActivity.this);
            return "";
        }
    }

    // send disconnect
    private class DisconnectTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... guess) {
            try {
                contr.disconnect();
            } catch (IOException e) {

            }
            return "";
        }
    }


    /*
     * Callbacks that get called when messages are received from server
     */


    public void notStarted() {
        runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                statusField.setText("No game ongoing.");
                guessButton.setEnabled(false);
                guessField.setFocusable(false);
            }
        });
    }

    public void win(final String word, final int score) {
        runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                statusField.setText("You win!");
                stateField.setText(word);
                scoreField.setText(Integer.toString(score));
                guessButton.setEnabled(false);
                guessField.setFocusable(false);
            }
        });
    }

    public void ongoing(final String word, final int attempts) {
        runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                statusField.setText("Game ongoing.");
                stateField.setText(word);
                attempField.setText(Integer.toString(attempts));
                guessButton.setEnabled(true);
                guessField.setFocusableInTouchMode(true);
            }
        });
    }

    public void lose(final String word, final int score) {
        runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                statusField.setText("You lost.");
                stateField.setText(word);
                scoreField.setText(Integer.toString(score));
                attempField.setText(Integer.toString(0));
                guessButton.setEnabled(false);
                guessField.setFocusable(false);
            }
        });
    }

    public void error() {
        runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                statusField.setText("Error occurred.");
                guessButton.setEnabled(false);
                startButton.setEnabled(false);
                guessField.setFocusable(false);
            }
        });
    }
}