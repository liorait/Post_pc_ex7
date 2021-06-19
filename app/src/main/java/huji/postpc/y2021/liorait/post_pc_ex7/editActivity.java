package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.UUID;
import android.widget.Toast;
public class editActivity extends Activity {
    public LocalDataBase dataBase = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_order);
        Context context = this;
        if (dataBase == null) {
            dataBase = SandwichOrderApplication.getInstance().getDataBase();
        }

        // find views
        Button MinusBtn = findViewById(R.id.minus);
        Button PlusBtn = findViewById(R.id.plus);
        EditText numberOfPicklesText = findViewById(R.id.editNumberPicklesText);
        ImageView x_image_view = findViewById(R.id.x_image);
        Button saveButton = findViewById(R.id.saveImageButton);
        Button deleteButton = findViewById(R.id.deleteOrderButton);
        CheckBox hummusCB = findViewById(R.id.addHummusCheckBox);
        CheckBox tahiniCB = findViewById(R.id.addTahiniCheckBox);
        EditText commentText = findViewById(R.id.editCommentsEditText);
        TextView costumerName = findViewById(R.id.nameTextView);
        saveButton.setEnabled(false);


        // display information on the screen
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference document = firestore.collection("orders").document(dataBase.currentOrderId);

        document.get().addOnSuccessListener(documentSnapshot -> {
            Sandwich currentOrder = documentSnapshot.toObject(Sandwich.class);

            numberOfPicklesText.setText(currentOrder.getPickles()); // show number of pickles
            commentText.setText(currentOrder.getComment()); // show comment
            if (currentOrder.hummus){
                hummusCB.setChecked(true);
            }
            if (currentOrder.tahini){
                tahiniCB.setChecked(true);
            }
            costumerName.setText(currentOrder.getCostumerName() + " ,Your order is waiting. You can edit your order");

        }).addOnCompleteListener(task -> {
            System.out.println("completed task");
            saveButton.setEnabled(true);
        });

        //DocumentReference document = firestore.collection("orders").document(dataBase.currentOrderId);
        document.addSnapshotListener((value, error) -> {
            Sandwich currentOrder = value.toObject(Sandwich.class);
            if (currentOrder.getStatus().equals("in_progress")){
                setContentView(R.layout.activity_main);
            }
        });

        deleteButton.setOnClickListener(v -> {
         //   document.delete();
            dataBase.deleteOrder(dataBase.currentOrderId);
            setContentView(R.layout.new_order);
        });

      //  if (currentOrder == null) {
      //      return;
      //  }
       // System.out.println(currentOrder.toString());
      //  numberOfPicklesText.setText(currentOrder.getPickles()); // show number of pickles
     //   commentText.setText(currentOrder.getComment()); // show comment
      //  if (currentOrder.hummus){
      //      hummusCB.setChecked(true);
     //   }
     //   if (currentOrder.tahini){
      //      tahiniCB.setChecked(true);
      //  }
      //  costumerName.setText(currentOrder.getCostumerName());




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Sandwich currentOrder = dataBase.getSandwich();

                String pickles = numberOfPicklesText.getText().toString();
                boolean hummus=false;
                boolean tahini=false;
                String comment="";

                // if hummus is checked
                if (hummusCB.isChecked()){
                    hummus = true;
                }
                if (tahiniCB.isChecked()){
                    tahini = true;
                }
                comment = commentText.getText().toString();
                String costumer_name = costumerName.getText().toString(); // todo fix only name

              //  String orderId = currentOrder.getId();
                String orderId = dataBase.currentOrderId;
                Sandwich newSandwich = new Sandwich(orderId, costumer_name, "waiting", pickles, hummus, tahini, comment);
                dataBase.updateOrder(newSandwich);

                // todo show message that changes are saved
                Toast.makeText(context, "Order was successfully saved", Toast.LENGTH_SHORT).show();
                //setContentView(R.layout.activity_main);
            }
        });



        MinusBtn.setOnClickListener(v -> {
            int parsed = Integer.parseInt(numberOfPicklesText.getText().toString());
            //  if (parsed > 10){
            //     return;
            //  }

            if (parsed - 1 >= 0) {
                String s = Integer.toString(parsed - 1);
                numberOfPicklesText.setText(s);
            }
        });

        PlusBtn.setOnClickListener(v -> {
            int parsed = Integer.parseInt(numberOfPicklesText.getText().toString());
            // if (parsed > 10){
            //    return;
            // }
            if (parsed + 1 <= 10) {
                String s = Integer.toString(parsed + 1);
                numberOfPicklesText.setText(s);
            }
        });

        numberOfPicklesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = numberOfPicklesText.getText().toString();
                if (text.equals("")){
                    return;
                }
                int text_num = 0;
                try {
                    text_num = Integer.parseInt(text);
                }
                catch(Exception e){
                    System.out.println("wrong text");
                }
                if (text_num > 10){
                    x_image_view.setVisibility(View.VISIBLE);
                }
                else{
                    x_image_view.setVisibility(View.GONE);
                }
            }
        });


    }
}
