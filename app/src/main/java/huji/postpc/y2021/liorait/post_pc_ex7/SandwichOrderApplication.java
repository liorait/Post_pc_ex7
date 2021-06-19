package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

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
    }
    public static SandwichOrderApplication getInstance(){
        return instance;
    }
    public LocalDataBase getDataBase(){
        return dataBase;
    }

}
