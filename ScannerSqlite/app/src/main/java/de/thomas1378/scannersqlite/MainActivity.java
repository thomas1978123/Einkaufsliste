package de.thomas1378.scannersqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper myDb;
    DatabaseHelperList myDb1;

    ImageView ivWrite, ivScan, ivTrash;
    ListView lvGoodsList;

    ArrayList<String> listGood;
    ArrayAdapter<String> arrayAdapter1;

    String goodsList, c;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivScan = findViewById(R.id.ivScan);
        ivTrash = findViewById(R.id.ivTrash);
        ivWrite = findViewById(R.id.ivWrite);
        lvGoodsList = findViewById(R.id.lvLGoodsList);

        ivScan.setOnClickListener(this);

        myDb = new DatabaseHelper(this);
        myDb1 = new DatabaseHelperList(this);

        listGood = new ArrayList<String>();
        arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),
              android.R.layout.simple_list_item_multiple_choice, listGood);

        lvGoodsList.setAdapter(arrayAdapter1);




        ivWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadActivity2(null);
            }
        });

        ivTrash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                SparseBooleanArray checkedItemPositions = lvGoodsList.getCheckedItemPositions();
                int itemCount = lvGoodsList.getCount();
                for(int i=itemCount-1; i >= 0; i--){
                  if(checkedItemPositions.get(i)){
                      //listGood.remove(eintrag);

                      Object o = lvGoodsList.getItemAtPosition(i);
                      String eintrag = o.toString();

                      //Flag = 0 setzen
                      Cursor res2 = myDb.getId(eintrag);

                      if(res2.getCount() == 0){
                          //Toast.makeText(MainActivity.this, "Nichts!" + eintrag+ "test" , Toast.LENGTH_SHORT).show();
                      }
                      StringBuffer buffer2 = new StringBuffer();

                      while(res2.moveToNext()) {
                          buffer2.append(res2.getString(0));
                      }
                      String idFlag =buffer2.toString();
                      String flag ="0";

                      boolean isUpdate = myDb.updateFlag0(idFlag, flag);

                      if (isUpdate == true){
                          //Toast.makeText(MainActivity.this, "Flag wurde auf 0 gesetzt!", Toast.LENGTH_LONG).show();
                      }else{
                          //Toast.makeText(MainActivity.this, "Flag0 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                      }

                      //Aus DatabaseHelperList löschen
                      Cursor res = myDb1.getId(eintrag);

                      if(res.getCount() == 0){
                          //Toast.makeText(MainActivity.this, "Nichts!" + eintrag+ "test" , Toast.LENGTH_SHORT).show();
                          return;
                      }
                      StringBuffer buffer = new StringBuffer();
                      //buffer.setLength(0);
                      while(res.moveToNext()) {
                          buffer.append(res.getString(0));
                      }
                      String n =buffer.toString();

                      //Aus Einkaufsliste löschen
                      Integer deletedRows = myDb1.delete(n);
                      if (deletedRows > 0){

                          //Aus DatabaseHelperList löschen
                          listGood.remove(eintrag);
                          arrayAdapter1.notifyDataSetChanged();

                      }else{
                          //Toast.makeText(MainActivity.this, n+"Daten wurden nicht gelöscht!", Toast.LENGTH_SHORT).show();
                      }
                      //
                    //arrayAdapter1.remove(listGood.get(i));

                      //arrayAdapter1.remove(lvGoodsList.getItemAtPosition(i).toString());
                }
                }


                checkedItemPositions.clear();
                listGood.clear();
                arrayAdapter1.notifyDataSetChanged();
                getGoodsL();
                    }
                });

        listGood.clear();
        lvGoodsList.setAdapter(arrayAdapter1);
        arrayAdapter1.notifyDataSetChanged();
        getGoodsL();
    }

    @Override
    public void onClick(View v) {

        scanCode();

    }

    private void scanCode(){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Code scannen");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result !=null){
            if (result.getContents() !=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());

                String scanId = String.valueOf(result.getContents());
                //checkId(id);
                //check(id);
                loadActivity(scanId);

                builder.setTitle("Scanning result");
                builder.setPositiveButton("Scan again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Beenden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            }



        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //lvList erstellen
    public void getGoodsL(){

        listGood.clear();
        lvGoodsList.setAdapter(arrayAdapter1);
        arrayAdapter1.notifyDataSetChanged();

        try (Cursor res = myDb1.getAllData()) {
            if (res.getCount() == 0) {
                //show message
                //showMessage("Error", "Keine Daten gefunden!");
            }

            listGood.clear();
            while (res.moveToNext()) {
                //Columnindex Spalten
                StringBuffer buffer = new StringBuffer();
                //buffer.append(res.getString(0)+ "\n");
                buffer.append(res.getString(1));
                String name = buffer.toString();

                arrayAdapter1.add(name);
                arrayAdapter1.notifyDataSetChanged();
            }
        }

    }

    //2. activiy öffnen
    public String loadActivity(String scanId){
        Intent intent = new Intent(this,SecondActivity.class);
        intent.putExtra("ScanId",scanId);
        startActivity(intent);
        this.finish();
        this.setVisible(false);
        return(scanId);
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show() ;
    }

    public String loadActivity2(String scanId){
        Intent intent = new Intent(this,ThirdActivity.class);
        //intent.putExtra("ScanId",scanId);
        startActivity(intent);
        //this.finish();
        //this.setVisible(false);
        return(null);
    }

    public Intent refresh(){
        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("ScanId",scanId);
        startActivity(intent);
        //this.finish();
        //this.setVisible(false);
        return(intent);
    }

}