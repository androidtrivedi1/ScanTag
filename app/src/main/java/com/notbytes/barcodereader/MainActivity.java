package com.notbytes.barcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;
    private TextView mTvResult, tvgram;
    private TextView mTvResultHeader;
    Button btnadd, btnhistory,btnclearall;
    DataHelper dbHelper;
    String item_no = "";
    String gram = "";
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_activity).setOnClickListener(this);
        findViewById(R.id.btn_fragment).setOnClickListener(this);
        mTvResult = findViewById(R.id.tv_result);
        mTvResultHeader = findViewById(R.id.tv_result_head);
        tvgram = findViewById(R.id.tvgram);
        btnadd = findViewById(R.id.btnadd);
        btnhistory = findViewById(R.id.btnhistory);
        btnclearall = findViewById(R.id.btnclearall);

        dbHelper = new DataHelper(MainActivity.this);

        btnhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.openToRead();
                if (!dbHelper.retrivedata().isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                }
                dbHelper.close();
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!item_no.equals("") && !gram.equals("")) {

                    if (gram.matches("\\d+(\\.\\d*)?|\\.\\d+")) {

                        dbHelper.openToWrite();
                        //  dbHelper.insert(item_no, gram);
                        if (dbHelper.retrivedata().size() > 0) {
                            for (int i = 0; i < dbHelper.retrivedata().size(); i++) {
                                if (item_no.equals(dbHelper.retrivedata().get(i).getItem_no())) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            }
                            if (flag == true) {
                                flag = false;
                                Toast.makeText(MainActivity.this, "Record already exists...!", Toast.LENGTH_SHORT).show();
                            } else {
                                dbHelper.insert(item_no, gram);
                                flag = false;
                                Toast.makeText(MainActivity.this, "Record added...!", Toast.LENGTH_SHORT).show();
                                mTvResultHeader.setText("");
                                mTvResult.setText("");
                                tvgram.setText("");
                                item_no = "";
                                gram = "";
                            }
                        } else {
                            dbHelper.insert(item_no, gram);
                            flag = false;
                            Toast.makeText(MainActivity.this, "Record added...!", Toast.LENGTH_SHORT).show();
                            mTvResultHeader.setText("");
                            mTvResult.setText("");
                            tvgram.setText("");
                            item_no = "";
                            gram = "";
                        }
                        dbHelper.close();
                    } else {
                        Toast.makeText(MainActivity.this,"Please scan gram properly",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please scan both values",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnclearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvResultHeader.setText("");
                mTvResult.setText("");
                tvgram.setText("");
                item_no = "";
                gram = "";
            }
        });

        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment:
                addBarcodeReaderFragment();
                mTvResultHeader.setText("");
                mTvResult.setText("");
                tvgram.setText("");
                item_no = "";
                gram = "";
                break;
            case R.id.btn_activity:
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fm_container);
                if (fragmentById != null) {
                    fragmentTransaction.remove(fragmentById);
                }
                fragmentTransaction.commitAllowingStateLoss();
                launchBarCodeActivity();
                break;
        }
    }

    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
       //     Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
            mTvResultHeader.setText("On Activity Result");
        //    mTvResult.setText(barcode.rawValue);
        }

    }

    @Override
    public void onScanned(Barcode barcode) {
   //     Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
        mTvResultHeader.setText("Barcode value from tags");
    //    mTvResult.setText(barcode.rawValue);
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        String str = "";
        for (int i = 0; i < barcodes.size(); i++) {
            // mTvResult.setText(barcodes.get(i).displayValue + "\n");

            item_no = barcodes.get(1).displayValue;
            gram = barcodes.get(0).displayValue;
            mTvResult.setText("Item No. - " + barcodes.get(1).displayValue + "");
            tvgram.setText("Gram -  " + barcodes.get(0).displayValue + "");
           /* if (i == 0) {
                str = "Item No. - " + barcodes.get(i).displayValue;
               item_no = barcodes.get(i).displayValue;
                mTvResult.setText("Item No. - " + barcodes.get(i).displayValue + "");
            }
            if (i == 1) {
                str = str + "\n" + "Gram -  " + barcodes.get(i).displayValue;
                gram = barcodes.get(i).displayValue;
                tvgram.setText("Gram -  " + barcodes.get(i).displayValue + "");
            }*/

            //    str = "Item No. - "+str+barcodes.get(i).displayValue+"\n"+ "Gram - ";
        }
        //   mTvResult.setText(str+"");
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}
