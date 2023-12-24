package com.template.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.template.R;


public class EndGameDialog {


    public interface GameRestartListener {
        void onGameRestart();
    }

    public static AlertDialog getDialog(Activity activity, GameRestartListener gameRestartListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.end_game_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();

        Button replayButton = view.findViewById(R.id.replay_button);
        Button endButton = view.findViewById(R.id.end_button);

        replayButton.setOnClickListener((v) -> {
            alertDialog.dismiss();
            gameRestartListener.onGameRestart();
        });

        endButton.setOnClickListener((v) -> {
            alertDialog.dismiss();
            activity.finish();

        });

        return alertDialog;


    }


}
