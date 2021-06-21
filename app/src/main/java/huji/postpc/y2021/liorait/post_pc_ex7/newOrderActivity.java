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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}