package org.alcalaesmusica.app.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import org.alcalaesmusica.app.R;

/**
 * Created by julio on 31/05/17.
 */

public class DialogShowNotification {


    private final Context context;

    public DialogShowNotification(Context context) {
        this.context = context;
    }

    public static DialogShowNotification newInstace(Context context) {
        return new DialogShowNotification(context);
    }

    public void show(String title, String message, final String btnText, final String btnLink) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        if (title != null) {
            ab.setTitle(title);
        }

        ab.setMessage(message);
        String buttonText = btnText != null ? btnText : context.getString(R.string.continue_str);
        ab.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (btnText != null && btnLink != null) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(btnLink)));
                }
            }
        });
        ab.show();
    }
}
