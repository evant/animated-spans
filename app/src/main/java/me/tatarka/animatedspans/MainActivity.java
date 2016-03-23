package me.tatarka.animatedspans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText name = (EditText) findViewById(R.id.name);
        final EditText password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(SpannablePasswordTransformationMethod.getInstance());

        EditTextAnimations.dropInOnInsert(name);
        EditTextAnimations.dropInOnInsert(password);

        Button accept = (Button) findViewById(R.id.accept_password);
        Button reject = (Button) findViewById(R.id.reject_password);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextAnimations.acceptAndClear(password);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextAnimations.rejectAndClear(password);
            }
        });
    }
}
