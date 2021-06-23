package test.huji.postpc.y2021.liorait.post_pc_ex7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import java.io.Serializable;

import huji.postpc.y2021.liorait.post_pc_ex7.LocalDataBase;
import huji.postpc.y2021.liorait.post_pc_ex7.MainActivity;
import huji.postpc.y2021.liorait.post_pc_ex7.R;
import huji.postpc.y2021.liorait.post_pc_ex7.SandwichOrderApplication;
import huji.postpc.y2021.liorait.post_pc_ex7.editActivity;
import huji.postpc.y2021.liorait.post_pc_ex7.newOrderActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})
public class MainActivityTest {

    private ActivityController<MainActivity> activityController;
    private MainActivity activityUnderTest;
    private SandwichOrderApplication mockApplication;
    private LocalDataBase dataBaseTest = SandwichOrderApplication.getInstance().getDataBase();

    /** initialize main activity with a mock calculator */
    @Before
    public void setup(){
        mockApplication = Mockito.mock(SandwichOrderApplication.class);
        assertNull(dataBaseTest.getId()); // when db is created the order id is null

       // Mockito.when(mockCalculator.output()).thenReturn(DEFAULT_CALCULATOR_OUTPUT);

        activityController = Robolectric.buildActivity(MainActivity.class);
        activityUnderTest = activityController.get();
        activityUnderTest.dataBase = dataBaseTest;
    }

    @Test
    public void when_no_order_exists_shows_newOrderActivity_screen() {
        // setup
        mockApplication = Mockito.mock(SandwichOrderApplication.class);
        assertNull(dataBaseTest.getId()); // when db is created the order id is null

        activityController = Robolectric.buildActivity(MainActivity.class);
        activityUnderTest = activityController.get();
        activityUnderTest.dataBase = dataBaseTest;
        activityController.create().visible();

       // EditText costumerName = activityUnderTest.findViewById(R.id.)

        Intent intent = Shadows.shadowOf(activityUnderTest).peekNextStartedActivity();
        // verify that the activity that is shown is new order activity
        Assert.assertEquals("huji.postpc.y2021.liorait.post_pc_ex7.newOrderActivity",  intent.getComponent().getClassName());
    }

    @Test
    public void when_creates_an_order_and_in_waiting_state_shows_editActivity_screen() {
        // setup
        mockApplication = Mockito.mock(SandwichOrderApplication.class);

        ActivityController<newOrderActivity> newOrderActivityController = Robolectric.buildActivity(newOrderActivity.class);
        newOrderActivity activityUnderTest = newOrderActivityController.get();
        activityUnderTest.dataBase = dataBaseTest;
        newOrderActivityController.create().visible();
        Button saveButton = activityUnderTest.findViewById(R.id.saveImageButton);
        EditText costumerName = activityUnderTest.findViewById(R.id.editTextTextPersonName);
        costumerName.setText("test");
        saveButton.performClick();

        Intent intent = Shadows.shadowOf(activityUnderTest).getNextStartedActivity();

        // verify that the first activity is edit order activity
        Assert.assertEquals("huji.postpc.y2021.liorait.post_pc_ex7.editActivity",  intent.getComponent().getClassName());

        assertNotNull(dataBaseTest.getId()); // when db is created the order id is not null
        Assert.assertEquals("waiting", dataBaseTest.getState());
    }

    @Test
    public void when_new_order_activity_launches_fields_have_empty_values() {
        // setup
        mockApplication = Mockito.mock(SandwichOrderApplication.class);

        ActivityController<newOrderActivity> newOrderActivityController = Robolectric.buildActivity(newOrderActivity.class);
        newOrderActivity activityUnderTest = newOrderActivityController.get();
        activityUnderTest.dataBase = dataBaseTest;
        newOrderActivityController.create().visible();

        EditText costumerName = activityUnderTest.findViewById(R.id.editTextTextPersonName);
        EditText numberOfPicklesText = activityUnderTest.findViewById(R.id.editNumberPicklesText);
        EditText commentText = activityUnderTest.findViewById(R.id.editCommentsEditText);

        // Assert fields have empty values
        Assert.assertEquals("", costumerName.getText().toString());
        Assert.assertEquals("", commentText.getText().toString());
        Assert.assertEquals("0", numberOfPicklesText.getText().toString());
    }
}