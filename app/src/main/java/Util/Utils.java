package Util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nic.HousingWorkMonitoringSystemWithGeoFensing.R;

/**
 * Created by NIC on 12-12-2018.
 */

public class Utils {


    public static void showAlert(Context context, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.alert_dialog, null);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setView(dialogView, 0, 0, 0, 0);
            alertDialog.setCancelable(false);
            alertDialog.show();

            TextView tv_message = (TextView) dialogView.findViewById(R.id.tv_message);
            tv_message.setText(message);

            Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
        }
    }
}
