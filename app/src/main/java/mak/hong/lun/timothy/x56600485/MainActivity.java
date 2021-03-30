package mak.hong.lun.timothy.x56600485;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Spinner spinner;
    Button button;
    EditText cAns;
    EditText fAns;
    EditText kAns;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle(getResources().getString(R.string.title));

        cAns = (EditText) findViewById(R.id.main_c_ans);
        fAns = (EditText) findViewById(R.id.main_f_ans);
        kAns = (EditText) findViewById(R.id.main_k_ans);

        input = (EditText) findViewById(R.id.main_edittext);
        spinner = (Spinner) findViewById(R.id.main_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.choices));
        spinner.setAdapter(adapter);
        loadInput();
        button = (Button) findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_input), Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        HashMap<Integer, Double> results = calculateTemperature(Double.parseDouble(input.getText().toString()), spinner.getSelectedItemPosition());
                        DecimalFormat df = new DecimalFormat("0.00");
                        cAns.setText(df.format(results.get(0)));
                        fAns.setText(df.format(results.get(1)));
                        kAns.setText(df.format(results.get(2)));
                        saveInput(input.getText().toString(), spinner.getSelectedItemPosition());
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, R.string.bad_input, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.option_menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.about_msg)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
                return true;
            case R.id.option_menu_exit:
                this.finish();
                System.exit(0);
        }
        return false;
    }

    public void saveInput(String input, int choice) {
        SharedPreferences pref = getSharedPreferences("Temp Convert", MODE_PRIVATE);
        pref.edit().putString("temp", input).apply();
        pref.edit().putInt("choice", choice).apply();
    }

    public void loadInput() {
        SharedPreferences pref = getSharedPreferences("Temp Convert", MODE_PRIVATE);
        if (pref.contains("temp") && pref.contains("choice")) {
            input.setText(pref.getString("temp", ""));
            spinner.setSelection(pref.getInt("choice", 0));
        }
    }

    private HashMap<Integer, Double> calculateTemperature(double temp, int choice) {
        HashMap<Integer, Double> results = new HashMap<>();
        double c;
        switch (choice) {
            case 0: //C
                results.put(0, temp);
                results.put(1, getF(temp));
                results.put(2, getK(temp));
                break;
            case 1: //F
                c = getC(temp);
                results.put(0, c);
                results.put(1, temp);
                results.put(2, getK(c));
                break;
            case 2: //K
                c = temp - 273.15d;
                results.put(0, c);
                results.put(1, getF(c));
                results.put(2, temp);
                break;
        }

        return results;
    }

    private double getC(double f) {
        return (f - 32d)*(5d/9d);
    }

    private double getF(double c) {
        return (9d/5d)*c + 32d;
    }

    private double getK(double c) {
        return c + 273.15d;
    }

}