package de.thomas1378.scannersqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper myDb;
    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;
    String goodsList = null;

    ImageView ivBack2, ivTrash2;
    ListView lvList2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ivBack2 = findViewById(R.id.ivBack2);
        ivTrash2 = findViewById(R.id.ivTrash2);
        lvList2 = findViewById(R.id.lvList2);

        myDb = new DatabaseHelper(this);

        list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, list);

        lvList2.setAdapter(arrayAdapter);

        ivBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadActivity();
            }
        });

        ivTrash2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                SparseBooleanArray checkedItemPositions = lvList2.getCheckedItemPositions();
                int itemCount = lvList2.getCount();
                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                        //listGood.remove(eintrag);

                        Object o = lvList2.getItemAtPosition(i);
                        String eintrag = o.toString().trim();

                        //listGood.remove(eintrag);

                        Cursor res = myDb.getId(eintrag);

                        if(res.getCount() == 0){
                            //Toast.makeText(MainActivity2.this, "Nichts!" + eintrag+ "test" , Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        //buffer.setLength(0);
                        while(res.moveToNext()) {
                            buffer.append(res.getString(0));
                        }
                        String n =buffer.toString();


                        Integer deletedRows = myDb.delete(n);
                        if (deletedRows > 0){
                            //Toast.makeText(MainActivity2.this, "Daten wurden gelöscht!" , Toast.LENGTH_SHORT).show();

                            list.remove(eintrag);
                            arrayAdapter.notifyDataSetChanged();

                        }else{
                            //Toast.makeText(MainActivity2.this, "Daten wurden nicht gelöscht!", Toast.LENGTH_SHORT).show();
                        }
                        //
                        //arrayAdapter1.remove(listGood.get(i));

                        //arrayAdapter1.remove(lvGoodsList.getItemAtPosition(i).toString());
                    }
                }


                checkedItemPositions.clear();
                list.clear();
                arrayAdapter.notifyDataSetChanged();
                getGoods();
            }
        });

        lvList2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int wich_item = position;
                //final String  raw = Long.toString(id + 1);
                Object o = lvList2.getItemAtPosition(position);
                String eintrag = o.toString().trim();

                new AlertDialog.Builder(MainActivity2.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Löschen")
                        .setMessage("Soll der Artikel gelöscht werden?")
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Löschen
                                Integer a = wich_item + 1;
                                goodsList = String.valueOf(a);


                                //tvUnvisible.getText().toString()
                                Cursor res = myDb.getId(eintrag);

                                if(res.getCount() == 0){
                                    //Toast.makeText(MainActivity2.this, "Nichts!" + eintrag+ "test" , Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                StringBuffer buffer = new StringBuffer();
                                //buffer.setLength(0);
                                while(res.moveToNext()) {
                                    buffer.append(res.getString(0));
                                }
                                String n =buffer.toString();


                                Integer deletedRows = myDb.delete(n);
                                if (deletedRows > 0){
                                    //Toast.makeText(MainActivity2.this, "Daten wurden gelöscht!" , Toast.LENGTH_SHORT).show();

                                    list.remove(wich_item);
                                    arrayAdapter.notifyDataSetChanged();

                                }else{
                                    Toast.makeText(MainActivity2.this,  n, Toast.LENGTH_SHORT).show();
                                }

                            }


                        })
                        .setNegativeButton("Nein", null)
                        .show();

                return false;
            }
        });

        getGoods();
    }

    //lvList erstellen
    public void getGoods(){

        list.clear();
        lvList2.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        Cursor res = myDb.getAllData();
        while(res.moveToNext()){
            //Columnindex Spalten
            StringBuffer buffer = new StringBuffer();
            buffer.append(res.getString(2)+ "\n");
            String name = buffer.toString();
            arrayAdapter.add(name);
            arrayAdapter.notifyDataSetChanged();
        }

    }

    public boolean checkGoodsList() {
        boolean goodsIsOk2 = myDb.checkGoodsIsOkay(goodsList);
        if (!goodsIsOk2) {
            //Toast.makeText(MainActivity2.this, "Der Name ist in der Einkaufsliste bereits hinterlegt!", Toast.LENGTH_SHORT).show();
        }
        return goodsIsOk2;
    }

    public void deleteList(){
        Integer deletedRows = myDb.delete(goodsList);
        arrayAdapter.notifyDataSetChanged();
        if (deletedRows > 0){
            //Toast.makeText(MainActivity2.this, "Die Ware wurden aus der Einkausfsliste gelöscht!", Toast.LENGTH_LONG).show();
        }else{
            //Toast.makeText(MainActivity2.this, "Die Ware wurden nicht aus der Einkausfsliste gelöscht!", Toast.LENGTH_LONG).show();
        }
    }

    public String loadActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("ScanId",scanId);
        //Refresh MainActivity
        startActivity(intent);
        this.finish();
        return(null);
    }

    @Override
    public void onClick(View v) {

    }
}