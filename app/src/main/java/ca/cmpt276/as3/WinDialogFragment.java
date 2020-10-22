package ca.cmpt276.as3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ca.cmpt276.as3.model.GameHistory;

public class WinDialogFragment extends DialogFragment {

    private static final String BEST_SCORE = "BEST_SCORE";
    private static final String SCAN_USED = "SCAN";
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_win_dialog, null);

        setScoreText();
        builder.setView(view)
                .setTitle(R.string.congratulation)
                .setPositiveButton(R.string.ok, (dialog, id) -> getActivity().finish());
        return builder.create();
    }

    private void setScoreText() {
        assert getArguments() != null;
        int best = getArguments().getInt(BEST_SCORE, 0);
        int curr = getArguments().getInt(SCAN_USED, 0);
        String str = getString(R.string.win_page_current_scan, curr);
        if (best < curr) {
            str += getString(R.string.win_page_best_score, best);
        } else {
            str += getString(R.string.win_page_same_score);
        }

        TextView textView = view.findViewById(R.id.win_page_score);
        textView.setText(str);
    }

    public static WinDialogFragment getInstance(GameHistory history, int score) {
        WinDialogFragment dialog = new WinDialogFragment();
        int bestScore = history.getBestScore();

        Bundle args = new Bundle();
        args.putInt(BEST_SCORE, bestScore);
        args.putInt(SCAN_USED, score);
        dialog.setArguments(args);

        return dialog;
    }
}