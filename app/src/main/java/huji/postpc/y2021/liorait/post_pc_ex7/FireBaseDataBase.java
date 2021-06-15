package huji.postpc.y2021.liorait.post_pc_ex7;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FireBaseDataBase {



    public static ArrayList<Sandwich> getCollection(String collection){
        // todo decide what to return here
        return null;
    }

    public static Sandwich getDocument(String collection, String docId){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(collection).document(docId).get().addOnSuccessListener(documentSnapshot -> {
            Sandwich sandwich = documentSnapshot.toObject(Sandwich.class);
        });
        return null;
    }
}
