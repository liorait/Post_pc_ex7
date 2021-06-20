package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.atomic.AtomicReference;

public class SandwichOrderApplication extends Application {

    private static SandwichOrderApplication instance = null;
    private static String orderId = null;
    private LocalDataBase dataBase;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseApp.getApps(this);
        FirebaseFirestore.getInstance();
        instance = this;


        // check if the current order id is stored in sp
        dataBase = new LocalDataBase(this); // pass the current context to allow broadcasts
        // todo update the db info from firestore?

        // checks if there exists current order
      //  boolean existsOrder = dataBase.existsCurrentOrder();
       // if (existsOrder = false){

    //    }
        AtomicReference<String> order_state = new AtomicReference<>("");
        if (dataBase.currentOrderId != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference document = firestore.collection("orders").document(dataBase.currentOrderId);

            document.get().addOnSuccessListener(documentSnapshot -> {
                Sandwich currentOrder = documentSnapshot.toObject(Sandwich.class);
                dataBase.current_state = currentOrder.getStatus();
                System.out.println("the current status is: " + currentOrder.getStatus());

            }).addOnCompleteListener(task -> {
                System.out.println("completed task");
                order_state.set(dataBase.current_state);
            });

            document.addSnapshotListener((value, error) -> {
                Sandwich currentOrder = value.toObject(Sandwich.class);
                dataBase.current_state = currentOrder.getStatus();
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
