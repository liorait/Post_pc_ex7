package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

public class OrderReady extends Activity {
    public LocalDataBase dataBase = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_ready_screen);

        Context context = this;
        if (dataBase == null) {
            dataBase = SandwichOrderApplication.getInstance().getDataBase();
        }

        Button gotItButton = findViewById(R.id.gotTheOrderButton);

        gotItButton.setOnClickListener(v -> {
          //  Sandwich updatedOrder = new Sandwich()
            dataBase.gotOrder();
            Intent newOrder = new Intent(OrderReady.this, newOrderActivity.class);
            startActivity(newOrder);
           // SandwichOrderApplication s = new SandwichOrderApplication();
          //  s.onCreate();
        });
    }
}

