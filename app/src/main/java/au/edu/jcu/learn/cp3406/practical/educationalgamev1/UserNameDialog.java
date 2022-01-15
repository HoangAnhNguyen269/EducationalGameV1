package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UserNameDialog extends AppCompatDialogFragment {

    private EditText editTextUserName;
    private UserNameDialogListener listener;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view).setTitle("Enter your user name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    String username = editTextUserName.getText().toString();
                    listener.applyText(username);
            }
        });

        editTextUserName = view.findViewById(R.id.edit_username);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =(UserNameDialogListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+" must implement UserNameDialogListener");
        }
    }

    public interface UserNameDialogListener{
        void applyText(String username);
    }
}
