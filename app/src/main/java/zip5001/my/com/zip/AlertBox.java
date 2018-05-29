package zip5001.my.com.zip;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertBox {

    public static void builder(Context con, String title, String message, String posButton, String negButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(posButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(negButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    public static void builder(Context con, String title, String message, String posButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(posButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public static AlertDialog.Builder builder(Context con, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        return builder.setTitle(title).setMessage(message);
    }
}
