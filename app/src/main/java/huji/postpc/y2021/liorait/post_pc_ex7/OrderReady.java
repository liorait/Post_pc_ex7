package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class OrderReady extends Activity {
    public LocalDataBase dataBase = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_ready_screen);

        // find view
        Button gotItButton = findViewById(R.id.gotTheOrderButton);
        Context context = this;

        if (dataBase == null) {
            dataBase = SandwichOrderApplication.getInstance().getDataBase();
        }

        gotItButton.setOnClickListener(v -> {
            dataBase.gotOrder(); // notify that received the order (change the order state to 'done' and delete the order from sp)
            Intent newOrder = new Intent(OrderReady.this, newOrderActivity.class);
            startActivity(newOrder);
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(OrderReady.this,"There is no option to go to previous screen",Toast.LENGTH_LONG).show();
    }
}

