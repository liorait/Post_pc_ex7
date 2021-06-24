package huji.postpc.y2021.liorait.post_pc_ex7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public LocalDataBase dataBase = null;
    String READY = "ready";
    String DONE = "done";
    String WAITING = "waiting";
    String IN_PROGRESS = "in progress";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (dataBase == null) {
            dataBase = SandwichOrderApplication.getInstance().getDataBase();
        }

        String orderState;

        if (!dataBase.existsCurrentOrder()) {

            // open edit activity screen
            Intent editIntent = new Intent(MainActivity.this, newOrderActivity.class);
            startActivity(editIntent);

        } else {

            String orderId = dataBase.getId();
            orderState = dataBase.getState();
            if (orderState.equals(WAITING)){
                // edit order
                Intent editIntent = new Intent(MainActivity.this, editActivity.class);
                startActivity(editIntent);
                this.finish();
            }
            else if (orderState.equals(IN_PROGRESS)){
               // Intent editIntent = new Intent(MainActivity.this, OrderInTheMaking.class);
               // startActivity(editIntent);
                setContentView(R.layout.order_in_the_making);
            }
            else if (orderState.equals(READY)){
                setContentView(R.layout.order_ready_screen);
               // Intent editIntent = new Intent(MainActivity.this, OrderReady.class);
               // startActivity(editIntent);
            }
        }
    }
}