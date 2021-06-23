package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class SandwichOrderApplication extends Application {

    private static SandwichOrderApplication instance = null;
    private LocalDataBase dataBase;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseApp.getApps(this);
        FirebaseFirestore.getInstance();
        instance = this;

        dataBase = new LocalDataBase(this); // pass the current context to allow broadcasts

        AtomicReference<String> order_state = new AtomicReference<>("");
        if (dataBase.getId() != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference document = firestore.collection("orders").document(dataBase.getId());

            document.get().addOnSuccessListener(documentSnapshot -> {
                Sandwich currentOrder = documentSnapshot.toObject(Sandwich.class);
                if (currentOrder != null) {
                    dataBase.setState(currentOrder.getStatus());
                }

            }).addOnCompleteListener(task -> {
                Log.i("tag", "completed task");
                order_state.set(dataBase.getState());
            });

            document.addSnapshotListener((value, error) -> {
                if ((value != null) && (value.exists())) {
                    Sandwich currentOrder = value.toObject(Sandwich.class);
                    dataBase.setState(currentOrder.getStatus());
                }
            });
        }
    }
    public static SandwichOrderApplication getInstance(){
        return instance;
    }
    public LocalDataBase getDataBase(){
        return dataBase;
    }
}
