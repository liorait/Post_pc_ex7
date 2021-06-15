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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocalDataBase dataBase = null;
    boolean existsOrder = false;
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
            //dataBase.getOrderId();
      //  if (dataBase.existsCurrentOrder()) {
      //      existsOrder = true;
      //  } else {
      //      existsOrder = false;
      //  }


    //    dataBase.deleteOrder(dataBase.getId()); // todo delete


        if (!dataBase.existsCurrentOrder()) {
            //setContentView(R.layout.new_order);
            // todo open new screen
            // open edit activity screen
            Intent editIntent = new Intent(MainActivity.this, newOrderActivity.class);
          //  startActivity(editIntent);
            // todo check if current order id is not null
            // put the relevant data into the intent
            String orderId = dataBase.getId();

            if (orderId != null) {
                editIntent.putExtra("id", dataBase.getId());
            }
            startActivity(editIntent);
           // setContentView(R.layout.new_order);
        } else {
            // todo get the state of the order
            String orderId = dataBase.getId();
            String orderState = dataBase.getSandwich().getStatus(); //todo fix
           // String  orderState="waiting";
            if (orderState.equals(WAITING)){
              //  System.out.println("error in state");
                // edit order
                Intent editIntent = new Intent(MainActivity.this, editActivity.class);
                startActivity(editIntent);
            }
            else if (orderState.equals(IN_PROGRESS)){
                // todo show in progress screen
            }

           // setContentView(R.layout.activity_main);
        }

    }
}