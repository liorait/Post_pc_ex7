package huji.postpc.y2021.liorait.post_pc_ex7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LocalDataBase {

    private final Context context;
    private final SharedPreferences sp;
    private String currentOrderId = null;
    private String state = null;
    private Sandwich sandwich;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LocalDataBase(Context context){
        this.context = context;
        this.sp = context.getSharedPreferences("local_db_order_id", Context.MODE_PRIVATE);
        initializeSp();
    }

    // initialize the shared preferences
    private void initializeSp(){
        Set<String> keys = sp.getAll().keySet();
        for (String key: keys){
            String state = sp.getString(key, null);
            if (!state.equals("done")){
                currentOrderId = key;
            }
        }
    }

    public boolean existsCurrentOrder(){
        return currentOrderId != null;
    }

    public Sandwich getSandwich(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // todo id should be saves in sp
        firestore.collection("orders").document(currentOrderId).get().addOnSuccessListener(documentSnapshot -> {
            sandwich = documentSnapshot.toObject(Sandwich.class);
           // sandwich = new Sandwich(currentOrderId, )
        });
        return sandwich;
    }

    public String getId(){
        return currentOrderId;
    }

    private void getCurrentOrderStateFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // todo id should be saves in sp
        firestore.collection("orders").document(currentOrderId).get().addOnSuccessListener(documentSnapshot -> {
            Sandwich sandwich = documentSnapshot.toObject(Sandwich.class);
            state = sandwich.getStatus();
        });
      //  return state;
    }

    public String getState(){
        getCurrentOrderStateFromFirestore();
        return state;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addNewOrder(Sandwich newOrder){
       // String orderId = UUID.randomUUID().toString();
        String state = "waiting";

        // update sp of the changes
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newOrder.getId(), state);
        editor.apply();

        // add to firestore firebase
        addToFirestore(newOrder);
    }

    private void addToFirestore(Sandwich newOrder){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("orders").add(newOrder).addOnSuccessListener(documentReference -> {
            System.out.println("addition done!");
        });
    }

    private void deleteFromFirestore(String collection, String id){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(collection).document(id).delete().addOnSuccessListener(aVoid -> {
            System.out.println("delete done!");
        });
    }

    private void updateOrderToFireStore(Sandwich sandwich){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String id = sandwich.getId();
        firestore.collection("orders").document(id).set(sandwich).addOnSuccessListener(aVoid -> {
            System.out.println("update done!");
        });
    }

    public void updateOrder(Sandwich sandwich){
        updateOrderToFireStore(sandwich);
    }

    public void deleteOrder(String orderId){
       // String orderId = order.getId();

        SharedPreferences.Editor editor = sp.edit();
        editor.remove(orderId); // remove the key
        editor.apply();
        currentOrderId = null;

        deleteFromFirestore("orders", orderId);
    }

}
