package com.notbytes.barcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    DataHelper dbHelper;
    ListView listview;
    public static TextView tvtotal;
    ImageView imgback;
    float total = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listview = (ListView) findViewById(R.id.listview);
        tvtotal = (TextView) findViewById(R.id.tvtotal);
        imgback = (ImageView) findViewById(R.id.imgback);

        dbHelper = new DataHelper(HistoryActivity.this);

        dbHelper.openToRead();
        tvtotal.setText("Total Weight = " + dbHelper.retriveTotal());

       /* ArrayList<DataClass> values = dbHelper.retrivedata();
        HashSet<DataClass> hashSet = new HashSet<DataClass>();
        hashSet.addAll(values);
        values.clear();
        values.addAll(hashSet);*/

        CustomAdapterDetails adapterDetails = new CustomAdapterDetails(HistoryActivity.this, dbHelper.retrivedata());

        listview.setAdapter(adapterDetails);

       /* for (int i = 0; i < values.size(); i++) {
            total = total + values.get(i).getGram();
        }

        tvtotal.setText("Total Weight = " + total);
*/
        dbHelper.close();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
