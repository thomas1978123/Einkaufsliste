package de.thomas1378.scannersqlite;

import static android.graphics.Color.GREEN;
import static androidx.appcompat.app.AlertDialog.Builder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    DatabaseHelper myDb;
    DatabaseHelperList myDb2;

    EditText etName;
    Button btnAdd;
    ListView lvList;
    ImageView ivBack, ivRefresh, ivRemoveDb;
    Spinner spDropdown;

    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;

    String goodsList, itemList, eintrag;

    FrameLayout myLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        etName = (EditText) findViewById(R.id.etName);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        lvList = (ListView) findViewById(R.id.lvList2);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        ivRemoveDb = (ImageView) findViewById(R.id.ivRemoveDb);
        spDropdown = (Spinner) findViewById(R.id.spDropdown);

        String[] items = new String[]{"Stück", "Gramm", "Kilogramm", "mLiter", "Liter"};

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadActivity();
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                getGoods();
            }
        });

        ivRemoveDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadActivity2();
            }
        });

        list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, list);

        //Spinner dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spDropdown.setAdapter(adapter);

        //simple_list_item_1
        //lvList.setAdapter(arrayAdapter);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String goods = etName.getText().toString().trim();
                //tvTest.setText(a);

                //Cursor res = myDb.getId(goods);
                Cursor res = myDb.getId(goods);

                if (res.getCount() == 0) {
                    //Toast.makeText(ThirdActivity.this, "Nichts!" + goods+ "test" , Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                //buffer.setLength(0);
                while (res.moveToNext()) {
                    buffer.append(res.getString(0));

                    Cursor res1 = myDb.getAllD(buffer.toString());
                    if (res1.getCount() == 0) {
                        //show message
                        //showMessage("Error", "Keine Daten gefunden!");
                    }
                    }
                String n = buffer.toString();

                list.clear();
                lvList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

                //Suche mit einem Buchstaben der Hauptdatenbank (DatabaseHelper)
                for(int i = 0; i < buffer.length(); i++){

                    String a = String.valueOf(buffer.charAt(i));

                    Cursor res1 = myDb.getAllD(a);
                    if (res1.getCount() == 0) {
                    }
                    //list.clear();
                    while (res1.moveToNext()) {
                        //Columnindex Spalten
                        StringBuffer buffer1 = new StringBuffer();
                        buffer1.append(res1.getString(2));
                        String name = buffer1.toString();
                        arrayAdapter.add(name);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                }

            @Override
            public void afterTextChanged(Editable s) {
                String test = "";
                if (etName.getText().toString().equals(test)){
                    getGoods();
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Object o = lvList.getItemAtPosition(position);
                itemList = o.toString().trim();
                StringBuffer buffer = new StringBuffer();
                buffer.setLength(0);

                if(checkGoodsListSale()) {

                    //Flag = 1 setzen
                    Cursor res = myDb.getId(itemList);

                    if(res.getCount() == 0){
                        Toast.makeText(ThirdActivity.this, "Nichts!" + itemList+ "test" , Toast.LENGTH_SHORT).show();
                    }

                    //buffer.setLength(0);
                    while(res.moveToNext()) {
                        buffer.append(res.getString(0).trim());
                    }
                    String idFlag =buffer.toString().trim();
                    String flag ="1";

                    boolean isUpdate = myDb.updateFlag1(idFlag, flag);

                    if (isUpdate == true){
                        //Toast.makeText(ThirdActivity.this, "Flag1 wurde aktualisiert!", Toast.LENGTH_LONG).show();
                        parent.getChildAt(position).setBackgroundColor(GREEN);
                        AddDataList();

                    }else{
                        //Toast.makeText(ThirdActivity.this, "Flag1 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                    }



                }


                return true;
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsList = etName.getText().toString().trim();
                String vergleich = "";
                if (!goodsList.equals(vergleich) && checkGoods()){
                    AddData();
                    //AddDataList1();
                    //Flag =1
                    Cursor res = myDb.getId(goodsList);

                    if(res.getCount() == 0){
                        Toast.makeText(ThirdActivity.this, "Nichts!" + itemList+ "test" , Toast.LENGTH_SHORT).show();
                    }
                    StringBuffer buffer = new StringBuffer();
                    //buffer.setLength(0);
                    while(res.moveToNext()) {
                        buffer.append(res.getString(0));
                    }
                    String idFlag =buffer.toString();
                    String flag ="1";

                    boolean isUpdate = myDb.updateFlag1(idFlag, flag);

                    if (isUpdate == true){
                        //Toast.makeText(ThirdActivity.this, "Flag1 wurden aktualisiert!", Toast.LENGTH_LONG).show();
                        Integer a = Integer.parseInt(idFlag);
                        lvList.setItemChecked(a, true);
                    }else{
                        //Toast.makeText(ThirdActivity.this, "Flag1 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                    }

                    if(!goodsList.equals(vergleich) && checkGoodsList() && !checkGoods()){


                        AddDataList1();

                        //Flag =1
                        Cursor res2 = myDb.getId(goodsList);

                        if(res2.getCount() == 0){
                            Toast.makeText(ThirdActivity.this, "Nichts!" + itemList+ "test" , Toast.LENGTH_SHORT).show();
                        }
                        StringBuffer buffer2 = new StringBuffer();
                        //buffer.setLength(0);
                        while(res2.moveToNext()) {
                            buffer2.append(res2.getString(0));
                        }
                        String idFlag2 =buffer2.toString();
                        flag ="1";

                        boolean isUpdate2 = myDb.updateFlag1(idFlag2, flag);

                        if (isUpdate2 == true){
                            //Toast.makeText(ThirdActivity.this, "Flag1 wurden aktualisiert!", Toast.LENGTH_LONG).show();
                            Integer a = Integer.parseInt(idFlag2);
                            lvList.setItemChecked(a, true);
                        }else{
                            //Toast.makeText(ThirdActivity.this, "Flag1 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                        }

                    }

                    //getGoods();
                    //etName.setText("");
                }



                SparseBooleanArray checkedItems = lvList.getCheckedItemPositions();
                if (checkedItems != null){
                    //lvList.isSelected()
                    //AddDataList1();
                    SparseBooleanArray checkedItemPositions = lvList.getCheckedItemPositions();
                    int itemCount = lvList.getCount();
                    for(int i=itemCount-1; i >= 0; i--) {
                        Object o = lvList.getItemAtPosition(i);
                        eintrag = o.toString();
                        if (checkedItemPositions.get(i) && checkGoodsList2()) {



                            //Flag = 1 setzen
                            Cursor res = myDb.getId(eintrag);

                            if (res.getCount() == 0) {
                                //Toast.makeText(ThirdActivity.this, "Nichts!" + itemList + "test", Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(ThirdActivity.this, "Flag1 wurden aktualisiert!", Toast.LENGTH_LONG).show();
                                lvList.setItemChecked(i, true);
                            } else {
                                //Toast.makeText(ThirdActivity.this, "Flag1 wurde nicht aktualisiert!", Toast.LENGTH_LONG).show();
                            }


                                //Daten in DatabaseHelperList schreiben
                                boolean isInserted1 = myDb2.insertName(eintrag);
                                if (isInserted1 == true) {
                                    //Toast.makeText(ThirdActivity.this, eintrag+"Die Ware wurde zur Einkaufsliste hinzugefügt!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(ThirdActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
                                }

                                //lvList.setItemChecked(i,true);
                            //lvList.setSelection(i);
                            //lvList.setItemChecked(i, true);

                        }
                    }

                }
                //setChecked();
                getGoods();
                etName.setText("");
                //setChecked();
            }

        });

        //Constructur der DatabaseHelper.java-Class --> Create Datbase und Table
        myDb = new DatabaseHelper(this);
        myDb2 = new DatabaseHelperList(this);

        getGoods();
        //setChecked();
    }

    public void clearList(){
        //list.clear();
        //arrayAdapter.notifyDataSetChanged();

        list.clear();
        lvList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }


    public void checkData(){

    }

    public boolean checkGoods() {
        boolean goodsIsOk = myDb.checkGoodsIsOkay(goodsList);
        if (!goodsIsOk) {
            //Toast.makeText(ThirdActivity.this, goodsList+"Die Ware ist bereits in der Datenbank hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk;
    }

    public boolean checkGoodsList() {
        boolean goodsIsOk2 = myDb2.checkGoodsIsOkay(goodsList);
        if (!goodsIsOk2) {
            //Toast.makeText(ThirdActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk2;
    }

    public boolean checkGoodsList2() {
        boolean goodsIsOk3 = myDb2.checkGoodsIsOkay(eintrag);
        if (!goodsIsOk3) {
            //Toast.makeText(ThirdActivity.this, eintrag+"Die Ware ist bereits in der Einkaufsliste hinterlegt!" , Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk3;
    }

    public boolean checkGoodsListSale() {
        boolean goodsIsOk2 = myDb2.checkGoodsIsOkay(itemList);
        if (!goodsIsOk2) {
            //Toast.makeText(ThirdActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk2;
    }


    public void AddData(){
        boolean isInserted = myDb.insertName(goodsList);
        if (isInserted == true){
            //Toast.makeText(ThirdActivity.this, goodsList+"Die Ware wurde zur Datenbank hinzugefügt!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(ThirdActivity.this, "Die Ware wurde nicht zur Datenbank hinzugefügt!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddDataList(){
        boolean isInserted1 = myDb2.insertName(itemList);
        if (isInserted1 == true){
            //Toast.makeText(ThirdActivity.this, "Die Ware wurde zur Einkaufsliste hinzugefügt!", Toast.LENGTH_SHORT).show();

        }else{
            //Toast.makeText(ThirdActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddDataList1(){
        boolean isInserted1 = myDb2.insertName(goodsList);
        if (isInserted1 == true){
            //Toast.makeText(ThirdActivity.this, goodsList+"Die Ware wurde zur Einkaufsliste hinzugefügt!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(ThirdActivity.this, "Die Ware ist bereits in der Einkaufsliste hinterlegt!", Toast.LENGTH_SHORT).show();
        }
    }

    //lvList erstellen
    public void getGoods(){

        list.clear();
        lvList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        Cursor res = myDb.getAllData();
        if (res.getCount() == 0){
            //show message
            //showMessage("Error", "Keine Daten gefunden!");
        }
        list.clear();
        while(res.moveToNext()) {
            //Columnindex Spalten
            StringBuffer buffer = new StringBuffer();
            //StringBuffer buffer2 = new StringBuffer();
            //StringBuffer buffer3 = new StringBuffer();
            //buffer3.append(res.getString(0));
            buffer.append(res.getString(2));
            //buffer2.append(res.getString(3));
            String name = buffer.toString();
            //String flagSet = buffer2.toString();

            arrayAdapter.add(name);
            arrayAdapter.notifyDataSetChanged();

            //int test = res.getInt(0);
            //for (int i = buffer2.length() - 1; i >= 0; i--) {
            //if(flagSet.equals("1")){
                //lvList.setItemChecked(test, true);
            //}
            setChecked();

        }

        }

    public void setChecked(){
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0){
            //show message
            //showMessage("Error", "Keine Daten2 gefunden!");
        }


        while(res.moveToNext()) {
            StringBuffer buffer2 = new StringBuffer();
            buffer2.append(res.getString(3));
            //buffer2.append(res2.getString(3));
            //for (int i = buffer2.length()-1 ; i >= 0; i--) {

                String vergleichFlag1 = "1";
                String vergleichFlag0 = "0";

                String idFlag =buffer2.toString();

            int test = res.getInt(0) -1;
                //lvList.setItemChecked();
                if (res.getString(3).equals(vergleichFlag1)) {
                    Integer a = Integer.parseInt(idFlag);
                    //lvList.setItemChecked(i,true);
                    lvList.setItemChecked(test,true);
                    //Toast.makeText(ThirdActivity.this, test+"Checked!" + res.getString(3) , Toast.LENGTH_SHORT).show();
                }else{
                    //lvList.setItemChecked(test, false);

                }
            //}
        }

    }

    public void deleteList(){
        Integer deletedRows = myDb2.delete(goodsList);
        if (deletedRows > 0){
            //Toast.makeText(ThirdActivity.this, "Daten wurden aus der Einkausfsliste gelöscht!", Toast.LENGTH_LONG).show();
              }else{
                //Toast.makeText(ThirdActivity.this, "Daten wurden nicht aus der Einkausfsliste gelöscht!", Toast.LENGTH_LONG).show();
        }
    }

    public void showMessage(String title, String Message){
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show() ;
    }

    public String loadActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("ScanId",scanId);
        //Refresh MainActivity
        startActivity(intent);
        this.finish();
        return(null);
    }

    public String loadActivity2(){
        Intent intent = new Intent(this,MainActivity2.class);
        //intent.putExtra("ScanId",scanId);
        //Refresh MainActivity
        startActivity(intent);
        this.finish();
        return(null);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}