package example.TempleApp.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import example.TempleApp.JSON_API.Controller;
import example.TempleApp.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Chromicle.
 */
public class InsertData extends AppCompatActivity {

    String id,name,poojaTyp,overall,money,paidCheck = "NOT PAID";
    Spinner poojaType;
    RadioGroup radioGroup;
    LinearLayout totalLayout;
    EditText moneyDonated;
    int flag;
    private Button insert;
    private EditText uid1ET, nameET;
    private CheckBox paid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_data);
        insert = (Button) findViewById(R.id.insert_btn);
        uid1ET = (EditText) findViewById(R.id.uid);
        nameET = (EditText) findViewById(R.id.name);
        paid = (CheckBox) findViewById(R.id.paid_check);
        poojaType = (Spinner) findViewById(R.id.spinner1);
        totalLayout = (LinearLayout) findViewById(R.id.total_View);
        moneyDonated = (EditText) findViewById(R.id.money_donated);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_donate:
                        totalLayout.setVisibility(View.VISIBLE);
                        poojaType.setVisibility(View.GONE);
                        moneyDonated.setVisibility(View.VISIBLE);
                        flag = 0;
                        Toast.makeText(getBaseContext(), "Selected To Donate Money", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radio_pooja:
                        totalLayout.setVisibility(View.VISIBLE);
                        moneyDonated.setVisibility(View.GONE);
                        poojaType.setVisibility(View.VISIBLE);
                        flag = 1;
                        Toast.makeText(getBaseContext(), "Selected To Register New Pooja", Toast.LENGTH_LONG).show();
                        break;

                }
            }
        });


        paid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                id = uid1ET.getText().toString();
                if (((CheckBox) v).isChecked()) {
                    paidCheck = "PAID";

                } else {
                    paidCheck = "NOT PAID";
                }

            }
        });

            insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id = uid1ET.getText().toString();
                    name = nameET.getText().toString();
                    if (id.length() == 0) {
                        Toast.makeText(getBaseContext(), "Please enter a valid user ID", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (flag == 1) {
                            poojaTyp = String.valueOf(poojaType.getSelectedItem());

                            overall = poojaTyp + getResources().getString(R.string.empty) + name + getResources().getString(R.string.empty) + paidCheck;
                            new InsertDataActivity().execute();
                        } else {
                            money = moneyDonated.getText().toString();
                            overall = money + getResources().getString(R.string.empty) + name + getResources().getString(R.string.empty) + paidCheck;
                            new InsertDataActivity().execute();
                        }
                    }
                }
        });
    }


    class InsertDataActivity extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;
        int x;


        String result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(InsertData.this);
            dialog.setTitle("Hey Wait Please...");
            dialog.setMessage("Inserting your values..");
            dialog.show();

        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject jsonObject = Controller.insertData(id, overall);
            Log.i(Controller.TAG, "Json obj ");

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {

                    result = jsonObject.getString("result");

                }
            } catch (JSONException je) {
                Log.i(Controller.TAG, "exception" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
