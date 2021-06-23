package huji.postpc.y2021.liorait.post_pc_ex7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LocalDataBase {

    private final Context context;
    private final SharedPreferences sp;
    private String currentOrderId = null; // ID for SP
    private String current_state = "waiting";

    ArrayList<Sandwich> sandwiches_list = new ArrayList<>();
    private HashMap<String, Sandwich> all_orders = new HashMap<>();

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

    // notify that the user got the order (change the status of the order to 'done' and remove it from sp)
    public void gotOrder(){

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference document = firestore.collection("orders").document(currentOrderId);

        document.get().addOnSuccessListener(documentSnapshot -> {
            Sandwich sandwich = documentSnapshot.toObject(Sandwich.class);
            sandwich.setStatus("done");
            document.set(sandwich).addOnSuccessListener(aVoid -> {
                Log.i("tag", "sandwich state updated in firestore");
            });
        });

        // update the sp
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(currentOrderId); // remove the key
        editor.apply();

        currentOrderId = null;
    }

    // get sandwich with the current order id that is saved in local all_orders map
    public Sandwich getSandwich(){
        if (all_orders.size() != 0) {
            return all_orders.get(currentOrderId);
        }
        else
        {
            return null;
        }
    }

    // get the id of the current order
    public String getId(){
        return currentOrderId;
    }

   // private void getCurrentOrderStateFromFirestore(){
     ///   FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        //firestore.collection("orders").document(currentOrderId).get().addOnSuccessListener(documentSnapshot -> {
         //   Sandwich sandwich = documentSnapshot.toObject(Sandwich.class);
          //  current_state = sandwich.getStatus();
        //});
      //  return state;
    //}

    // returns the current state of the order
    public String getState(){
        return current_state;
    }

    // set the current state of the order
    public void setState(String state){
        if (state.equals("waiting") || state.equals("ready") || state.equals("in_progress")){
            current_state = state;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addNewOrder(Sandwich newOrder){

        // update sp of the changes
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newOrder.getId(), newOrder.getStatus());
        editor.apply();

        all_orders.put(newOrder.getId(), newOrder);
        currentOrderId = newOrder.getId();
        current_state = "waiting";

        // add to firestore firebase
        addToFirestore(newOrder);
    }
/**
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getAllDocuments(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("orders").get().addOnSuccessListener(querySnapshot -> {
            ArrayList<DocumentSnapshot> list = (ArrayList<DocumentSnapshot>) querySnapshot.getDocuments(); // get all documents
            ArrayList<Sandwich> sandwiches = new ArrayList<>();
            HashMap<String, Sandwich> orders = new HashMap<>();
            list.forEach(documentSnapshot -> {
                Sandwich sandwich = documentSnapshot.toObject(Sandwich.class);
                if (sandwich != null) {
                    orders.put(sandwich.getId(), sandwich);
                    sandwiches.add(sandwich);
                    System.out.println("found sandwich: " + sandwich.toString());
                }
            });
            sandwiches_list = new ArrayList<>(sandwiches);
            all_orders = new HashMap<>(orders);
            System.out.println(sandwiches_list);

        });
        // got all sandwiches currently saved in firestore
        System.out.println(sandwiches_list);
    }
*/
    // Adds a new order to firestore database
    private void addToFirestore(Sandwich newOrder){

        // get the firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String orderId = newOrder.getId();

        DocumentReference document = firestore.collection("orders").document(orderId);
        document.set(newOrder).addOnSuccessListener(aVoid -> {
            Log.i("tag", "added a new order to firestore db");
        });
    }

    // delete order from fire store
    private void deleteFromFirestore(String collection, String id){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(collection).document(id).delete().addOnSuccessListener(aVoid -> {
            Log.i("tag", "deleted order from firestore");
        });
    }

    private void updateOrderToFireStore(Sandwich sandwich){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String id = sandwich.getId();
        DocumentReference document = firestore.collection("orders").document(id);
        document.set(sandwich).addOnSuccessListener(aVoid -> {
            Log.i("tag", "updated order in firestore");
        });
    }

    // updates the current order
    public void updateOrder(Sandwich sandwich){

        // update order in local db
        all_orders.put(sandwich.getId(), sandwich);
        updateOrderToFireStore(sandwich);
    }

    // deleted the current order
    public void deleteOrder(String orderId){

        deleteFromFirestore("orders", orderId);

        SharedPreferences.Editor editor = sp.edit();
        editor.remove(orderId); // removes the key
        // editor.clear(); // delete all info from sp
        editor.apply();
        currentOrderId = null;
    }
}
