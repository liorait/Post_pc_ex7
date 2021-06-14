package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class SandwichOrderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseApp.getApps(this);
        FirebaseFirestore.getInstance();
    }
}
