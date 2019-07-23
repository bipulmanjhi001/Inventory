package com.broadwaybazar.model;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.broadwaybazar.R;

public class UpdateMeeDialog {

    private ActivityManager am;
    private Context context;
    private Dialog dialog;

    public void showDialogAddRoute(Activity activity, final String packageName){
        context=activity;
        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_update);
        am = (ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);

        Button updateDialogue=(Button)dialog.findViewById(R.id.buttonUpdate);
        Button cancelDialogue=(Button)dialog.findViewById(R.id.buttoncancel);
        Log.i("package name",packageName);

        updateDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
                        context.startActivity(intent);
            }
        });

        cancelDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });
        dialog.show();
    }
}
