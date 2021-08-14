package de.thomas1378.scannersqlite;

import static de.thomas1378.scannersqlite.R.layout.activity_second;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper myDb;
    DatabaseHelperList myDb2;

    EditText etScanId, etGoods;
    ImageView ivBackList, ivOk;
    TextView tvTip;

    String n, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_second);

        etScanId = findViewById(R.id.etScanID);
        etGoods = findViewById(R.id.etGoods);
        ivBackList = findViewById(R.id.ivBackList);
        ivOk = findViewById(R.id.ivOk);
        tvTip = findViewById(R.id.tvTip);

        ivBackList.setOnClickListener(this);

        Intent intent = getIntent();
        String scanId = intent.getStringExtra("ScanId");
        etScanId.setText(scanId);

        //Constructur der DatabaseHelper.java-Class --> Create Datbase und Table
        myDb = new DatabaseHelper(this);
        myDb2 = new DatabaseHelperList(this);

        checkData();
        scanIdVorhanden();
        //AddData();

    }

    public void checkData(){
        ivOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodsList = etGoods.getText().toString().trim();
                String vergleich = "";
                if (!goodsList.equals(vergleich) && checkScanId() && checkGoods()){
                    AddData();
                    if(checkGoodsList()){
                        AddDataList1();

                        //Flag = 1 setzen
                        Cursor res = myDb.getId(goodsList);

                        if (res.getCount() == 0) {
                            Toast.makeText(SecondActivity.this, "Nichts!" + goodsList + "test", Toast.LENGTH_SHORT).show();
                        }
                        StringBuffer buffer3 = new StringBuffer();
                        //buffer.setLength(0);
                        while (res.moveToNext()) {
                            buffer3.append(res.getString(0).trim());
                        }
                        String idFlag3 = buffer3.toString().trim();
                        String flag = "1";

                        //Flag=1 in DatabaseHelper schreiben
                        boolean isUpdate2 = myDb.updateFlag1(idFlag3, flag);

                        if (isUpdate2 == true) {
                            //Toast.makeText(SecondActivity.this, "Flag1 wurden aktualisiert!", Toast.LENGTH_LONG).show();
                            //lvList.setItemChecked(i, true);
                        } else {
                            //Toast.makeText(SecondActivity.this, "Flag1 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                        }

                    }
                }


            }
        });
    }

    public void scanIdVorhanden(){
        if(!checkScanId()){

            Cursor res = myDb.getGoodsScan(etScanId.getText().toString());

            if(res.getCount() == 0){
                Toast.makeText(SecondActivity.this, "Nichts!" + etScanId.getText().toString()+ "test" , Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer buffer = new StringBuffer();
            //buffer.setLength(0);
            while(res.moveToNext()) {
                buffer.append(res.getString(2));
            }

            b =buffer.toString().trim();

            etGoods.setText(b);
            etGoods.setEnabled(false);
            tvTip.setText("Bereits in der Datenbank hinterlegt!");

            if(checkGoodsList2()){
                AddDataList1();

                //Flag = 1 setzen
                Cursor res4 = myDb.getId(b);

                if (res4.getCount() == 0) {
                    Toast.makeText(SecondActivity.this, "Nichts!" + b + "test", Toast.LENGTH_SHORT).show();
                }
                StringBuffer buffer4 = new StringBuffer();
                //buffer.setLength(0);
                while (res.moveToNext()) {
                    buffer4.append(res.getString(0).trim());
                }
                String idFlag3 = buffer4.toString().trim();
                String flag = "1";

                //Flag=1 in DatabaseHelper schreiben
                boolean isUpdate4 = myDb.updateFlag1(idFlag3, flag);

                if (isUpdate4 == true) {
                    //Toast.makeText(SecondActivity.this, "Flag1 wurden aktualisiert!", Toast.LENGTH_LONG).show();
                    //lvList.setItemChecked(i, true);
                } else {
                    //Toast.makeText(SecondActivity.this, "Flag1 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                }

            }

        }
    }

    public boolean checkGoods() {
        boolean goodsIsOk = myDb.checkGoodsIsOkay(etGoods.getText().toString());
        if (!goodsIsOk) {
            //Toast.makeText(SecondActivity.this, "Bereits als " +etGoods.getText().toString() + " hinterlegt!", Toast.LENGTH_LONG).show();
        }
        return goodsIsOk;
    }

    public boolean checkScanId() {
        boolean scanIsOk = myDb.checkScanIdIsOkay(etScanId.getText().toString());
        if (!scanIsOk) {
            //Toast.makeText(SecondActivity.this, "Die ScanId ist bereits hinterlegt!", Toast.LENGTH_LONG).show();
        }
        return scanIsOk;
    }

    public void AddData(){
        boolean isInserted = myDb.insertData(etScanId.getText().toString(),
                             etGoods.getText().toString());
            if (isInserted == true){
                //  Toast.makeText(SecondActivity.this, "Daten eingefügt!", Toast.LENGTH_LONG).show();
            }else{
                //Toast.makeText(SecondActivity.this, "Daten wurden nicht eingefügt!", Toast.LENGTH_LONG).show();
            }
    }

    public boolean checkGoodsList() {
        boolean goodsIsOk2 = myDb2.checkGoodsIsOkay(etGoods.getText().toString());
        if (!goodsIsOk2) {
            //Toast.makeText(SecondActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk2;
    }

    public boolean checkGoodsList2() {
        boolean goodsIsOk2 = myDb2.checkGoodsIsOkay(b);
        if (!goodsIsOk2) {
            //Toast.makeText(SecondActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk2;
    }

    public boolean checkGoodsListDb() {
        boolean goodsIsOk2 = myDb2.checkGoodsIsOkay(n);
        if (!goodsIsOk2) {
            //Toast.makeText(SecondActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk2;
    }

    public void AddDataList1(){
        boolean isInserted1 = myDb2.insertName(etGoods.getText().toString());
        if (isInserted1 == true){
            //Toast.makeText(SecondActivity.this, "Die Ware wurde zur Einkaufsliste hinzugefügt!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(SecondActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddDataList2(){
        boolean isInserted1 = myDb2.insertName(n);
        if (isInserted1 == true){
            //Toast.makeText(SecondActivity.this, "Die Ware wurde zur Einkaufsliste hinzugefügt!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(SecondActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
    }

    //Zurück
    public String loadActivity(String scanId){
        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("ScanId",scanId);
        startActivity(intent);
        this.finish();
        this.setVisible(false);
        return(null
        );
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBackList:
                loadActivity(null);
                break;
        }
    }
}