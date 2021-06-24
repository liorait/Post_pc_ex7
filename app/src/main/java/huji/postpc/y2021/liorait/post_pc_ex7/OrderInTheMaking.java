package huji.postpc.y2021.liorait.post_pc_ex7;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class OrderInTheMaking extends Activity {
    public LocalDataBase dataBase = null;
    ListenerRegistration listener;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_in_the_making);
        Context context = this;
        if (dataBase == null) {
            dataBase = SandwichOrderApplication.getInstance().getDataBase();
        }
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference document = firestore.collection("orders").document(dataBase.getId());
        document.get().addOnSuccessListener(documentSnapshot -> {
            Sandwich currentOrder = documentSnapshot.toObject(Sandwich.class);

        }).addOnCompleteListener(task -> {
            Log.i("tag", "completed task");
        });


        listener = document.addSnapshotListener((value, error) -> {
            if ((value != null) && (value.exists())) {
                Sandwich currentOrder = value.toObject(Sandwich.class);
                if (currentOrder.getStatus().equals("ready")) {
                    Intent orderReadyIntent = new Intent(OrderInTheMaking.this, OrderReady.class);
                    dataBase.setState("ready");
                    startActivity(orderReadyIntent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(OrderInTheMaking.this,"There is no option to go to previous screen",Toast.LENGTH_LONG).show();
    }
}