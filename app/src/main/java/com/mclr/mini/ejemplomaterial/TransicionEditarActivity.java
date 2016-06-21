package com.mclr.mini.ejemplomaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class TransicionEditarActivity extends AppCompatActivity {
    private EditText nameEditText;
    private Switch switchImportant;
    private TextView initialTextView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transicion_editar);

        nameEditText = (EditText) findViewById(R.id.name);
        switchImportant = (Switch) findViewById(R.id.importante);
        initialTextView = (TextView) findViewById(R.id.initial);
        Button update_button = (Button) findViewById(R.id.update_button);
        Button delete_button = (Button) findViewById(R.id.delete_button);

        intent = getIntent();
        String nameExtra = intent.getStringExtra(EjemploMaterialActivity.EXTRA_NOMBRE);
        int importantExtra = intent.getIntExtra(EjemploMaterialActivity.EXTRA_IMPORTANTE, 0);
        String initialExtra = intent.getStringExtra(EjemploMaterialActivity.EXTRA_INICIAL);
        int colorExtra = intent.getIntExtra(EjemploMaterialActivity.EXTRA_COLOR, 0);

        nameEditText.setText(nameExtra);
        switchImportant.setChecked(importantExtra > 0);
        nameEditText.setSelection(nameEditText.getText().length());
        initialTextView.setText(initialExtra);
        initialTextView.setBackgroundColor(colorExtra);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // update initialTextView
                    initialTextView.setText("");
                } else if (s.length() >= 1) {
                    // initialTextView set to first letter of nameEditText and update name stringExtra
                    initialTextView.setText(String.valueOf(s.charAt(0)));
                    intent.putExtra(EjemploMaterialActivity.EXTRA_ACTUALIZAR, true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // must not be zero otherwise do not finish activity and report Toast message
                String text = initialTextView.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getApplicationContext(), "Enter a valid name", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra(EjemploMaterialActivity.EXTRA_ACTUALIZAR, true);
                    intent.putExtra(EjemploMaterialActivity.EXTRA_NOMBRE, String.valueOf(nameEditText.getText()));
                    intent.putExtra(EjemploMaterialActivity.EXTRA_IMPORTANTE, switchImportant.isChecked()? 1: 0);
                    intent.putExtra(EjemploMaterialActivity.EXTRA_INICIAL, String.valueOf(nameEditText.getText().charAt(0)));
                    setResult(RESULT_OK, intent);
                    supportFinishAfterTransition();
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EjemploMaterialActivity.EXTRA_BORRAR, true);
                setResult(RESULT_OK, intent);
                supportFinishAfterTransition();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
