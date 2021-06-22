package huji.postpc.y2021.liorait.post_pc_ex7;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

public class newOrderActivity extends Activity {
    public LocalDataBase dataBase = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        setContentView(R.layout.new_order);

        if (dataBase == null) {
            dataBase = SandwichOrderApplication.getInstance().getDataBase();
        }

        // find views
        Button MinusBtn = findViewById(R.id.minus);
        Button PlusBtn = findViewById(R.id.plus);
        EditText numberOfPicklesText = findViewById(R.id.editNumberPicklesText);
        ImageView x_image_view = findViewById(R.id.x_image);
        Button saveButton = findViewById(R.id.saveImageButton);
        CheckBox hummusCB = findViewById(R.id.addHummusCheckBox);
        CheckBox tahiniCB = findViewById(R.id.addTahiniCheckBox);
        EditText commentText = findViewById(R.id.editCommentsEditText);
        EditText costumerName = findViewById(R.id.editTextTextPersonName);

        if (savedInstanceState != null){
            Serializable saved_output = savedInstanceState.getSerializable("saved_state_new_order");
            loadState(saved_output);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String costumer_name = costumerName.getText().toString();

                if (costumer_name.equals("")) {
                    Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show();
                } else {
                    String pickles = numberOfPicklesText.getText().toString();
                    int picklesNum = Integer.parseInt(pickles);
                    if (picklesNum > 10) {
                        Toast.makeText(context, "Wrong amount of pickles", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean hummus = false;
                        boolean tahini = false;
                        String comment = "";

                        // if hummus is checked
                        if (hummusCB.isChecked()) {
                            hummus = true;
                        }
                        if (tahiniCB.isChecked()) {
                            tahini = true;
                        }
                        comment = commentText.getText().toString();
                        costumer_name = costumerName.getText().toString();

                        String orderId = UUID.randomUUID().toString();
                        Sandwich newSandwich = new Sandwich(orderId, costumer_name, "waiting", picklesNum, hummus, tahini, comment);
                        dataBase.addNewOrder(newSandwich);

                        Intent editIntent = new Intent(newOrderActivity.this, editActivity.class);
                        startActivity(editIntent);
                    }
                }
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
                if (text.equals("")) {
                    return;
                }
                int text_num = 0;
                try {
                    text_num = Integer.parseInt(text);
                } catch (Exception e) {
                    System.out.println("wrong text");
                }
                if (text_num > 10) {
                    x_image_view.setVisibility(View.VISIBLE);
                } else {
                    x_image_view.setVisibility(View.GONE);
                }
            }
        });
    }

    // flip screen
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Serializable serializable = saveState();
        outState.putSerializable("saved_state_new_order", serializable);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Serializable saved_output = savedInstanceState.getSerializable("saved_state_new_order");
        loadState(saved_output);
    }

    public Serializable saveState() {
        OrderState order_state = new OrderState();

        EditText numberOfPicklesText = findViewById(R.id.editNumberPicklesText);
        CheckBox hummusCB = findViewById(R.id.addHummusCheckBox);
        CheckBox tahiniCB = findViewById(R.id.addTahiniCheckBox);
        EditText commentText = findViewById(R.id.editCommentsEditText);
        EditText costumerName = findViewById(R.id.editTextTextPersonName);

        order_state.comment = commentText.getText().toString();
        order_state.costumer_name = costumerName.getText().toString();
        if (hummusCB.isChecked()){
            order_state.hummus = true;
        }
        else{
            order_state.hummus = false;
        }
        if (tahiniCB.isChecked()){
            order_state.tahini = true;
        }
        else{
            order_state.tahini = false;
        }
        String pickles_str = numberOfPicklesText.getText().toString();
        order_state.number_of_pickles = Integer.parseInt(pickles_str);

        return order_state;
    }

    public void loadState(Serializable prevState) {
        if (!(prevState instanceof OrderState)) {
            return; // ignore
        }
        OrderState casted = (OrderState) prevState;

        EditText numberOfPicklesText = findViewById(R.id.editNumberPicklesText);
        CheckBox hummusCB = findViewById(R.id.addHummusCheckBox);
        CheckBox tahiniCB = findViewById(R.id.addTahiniCheckBox);
        EditText commentText = findViewById(R.id.editCommentsEditText);
        EditText costumerName = findViewById(R.id.editTextTextPersonName);

        String pickles = Integer.toString(casted.number_of_pickles);
        numberOfPicklesText.setText(pickles);
        commentText.setText(casted.comment);
        costumerName.setText(casted.costumer_name);

        if (casted.hummus){
            hummusCB.setChecked(true);
        }
        if (casted.tahini){
            tahiniCB.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class OrderState implements Serializable {
         String costumer_name;
         int number_of_pickles;
         boolean hummus;
         boolean tahini;
         String comment;
    }
}