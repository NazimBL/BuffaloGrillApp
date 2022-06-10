package com.example.buffalogrillapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buffalogrillapp.ui.main.SectionsPagerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase menu_db;
    DBhelper dbHelper;
    Cursor cursor;

    String _prod_name,_category,_mode,_duré;
    int _id,_tmp;
    TextView printText;
    EditText searchItem;

    TabLayout tabLayout;
    ViewPager viewPager;

    private List<Menu> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);


        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                //switch (position)
                viewPager.getAdapter().notifyDataSetChanged();
                menuList=getListMenu(position);
                ItemFragment itemFragment=new ItemFragment(menuList);

                return itemFragment;
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab().setText(Menu.ENTRER));
        tabLayout.addTab(tabLayout.newTab().setText(Menu.PLAT));
        tabLayout.addTab(tabLayout.newTab().setText(Menu.DESSERT));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        FloatingActionButton add_fab = findViewById(R.id.fab);
        FloatingActionButton delete_fab = findViewById(R.id.fab2);

        UI_Init();
        //writeDummyDatabase();

        delete_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = menu_db.rawQuery("SELECT *FROM " + MenuDataBase.TABLE_NAME + "" +
                        " WHERE " + MenuDataBase.NAME + "=? AND " + MenuDataBase.CATEGORY + "=?", new String[]{"dela3", "dessert"});

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    //Retrieving User FullName and Email after successfull login and passing to LoginSucessActivity
//                  String _fname = cursor.getString(cursor.getColumnIndex(UserDatabase.));
//                 String _email = cursor.getString(cursor.getColumnIndex(UserDatabase.));
                    Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(MainActivity.this, "Querry Null", Toast.LENGTH_SHORT).show();

                readUserDb();
            }
        });

        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AddItemDialog();
            }
        });


    }

    public List<Menu> getListMenu(int tab){
        List<Menu> menus = null;

            cursor = menu_db.rawQuery("SELECT *FROM " + MenuDataBase.TABLE_NAME + "" +
                    " WHERE " + MenuDataBase.CATEGORY + "=?", new String[]{Menu.MENU_TABs[tab]});

        int count = cursor.getColumnCount();
        if (cursor!=null )
        {

            if  (cursor.moveToFirst())
            {
                do
                {
                    for (int i =0 ; i< count; i++)
                    {
                        String _name = cursor.getString(cursor.getColumnIndex(MenuDataBase.NAME));
                        String _duré = cursor.getString(cursor.getColumnIndex(MenuDataBase.DURE_DE_VIE));
                        String _mode = cursor.getString(cursor.getColumnIndex(MenuDataBase.MODE));
                        int _tmp = cursor.getInt(cursor.getColumnIndex(MenuDataBase.TEMPERATURE));
                        //String data = cursor.getString(i);
                        Menu menu=new Menu(_name,_duré,_mode,_tmp);
                        menus.add(menu);
                    }
                }
                while (cursor.moveToNext());
            }
        }

        return menus;
    }

    public void AddItemDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_box, null);
        builder.setView(dialogView);

        builder.setTitle("Ajouter Nouveau Produit");
        final EditText product_name=(EditText)dialogView.findViewById(R.id.edit_name);
        final EditText product_mode=(EditText)dialogView.findViewById(R.id.edit_mode);
        final EditText product_duré=(EditText)dialogView.findViewById(R.id.edit_duré);
        final EditText product_tmp=(EditText)dialogView.findViewById(R.id.edit_tmp);

        builder.setCancelable(true);

        Button cancel=(Button)dialogView.findViewById(R.id.cancel);
        Button validate=(Button)dialogView.findViewById(R.id.validate);

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                        ContentValues values=new ContentValues();
                        //if tab==1 , category = entrer etc..
                        //values.put(MenuDataBase.NAME,product_name.getText().toString());
                        values.put(MenuDataBase.NAME,product_name.getText().toString());
                        values.put(MenuDataBase.CATEGORY, Menu.ENTRER);

                        values.put(MenuDataBase.MODE,product_mode.getText().toString());
                        //does the date get updated
                        values.put(MenuDataBase.DURE_DE_VIE,product_duré.getText().toString());
                        values.put(MenuDataBase.TEMPERATURE,Integer.parseInt(product_tmp.getText().toString()));

                        menu_db.insert(MenuDataBase.TABLE_NAME,null,values);

                    alertDialog.dismiss();

                }catch (Exception e){

                    Toast.makeText(MainActivity.this,"Wrong input format",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void infoDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.info_dialog_box, null);
        builder.setView(dialogView);

        builder.setTitle("Information Produit");
        final TextView product_name=(TextView)dialogView.findViewById(R.id.text_name);
        final TextView product_mode=(TextView)dialogView.findViewById(R.id.text_mode);
        final TextView product_duré=(TextView)dialogView.findViewById(R.id.text_duré);
        final TextView product_tmp=(TextView)dialogView.findViewById(R.id.text_tmp);

        final TextView date=(TextView)dialogView.findViewById(R.id.text_date);

        builder.setCancelable(true);

        Button delete=(Button)dialogView.findViewById(R.id.suprimer_info);
        Button ok=(Button)dialogView.findViewById(R.id.info_ok);
        Button edit=(Button)dialogView.findViewById(R.id.edit_info);

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    ContentValues values=new ContentValues();
                    //if tab==1 , category = entrer etc..
                    //values.put(MenuDataBase.NAME,product_name.getText().toString());
                    values.put(MenuDataBase.NAME,product_name.getText().toString());
                    values.put(MenuDataBase.CATEGORY, Menu.ENTRER);

                    values.put(MenuDataBase.MODE,product_mode.getText().toString());
                    //does the date get updated
                    values.put(MenuDataBase.DURE_DE_VIE,product_duré.getText().toString());
                    values.put(MenuDataBase.TEMPERATURE,Integer.parseInt(product_tmp.getText().toString()));

                    menu_db.insert(MenuDataBase.TABLE_NAME,null,values);

                    alertDialog.dismiss();

                }catch (Exception e){

                    Toast.makeText(MainActivity.this,"Wrong input format",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
        private void UI_Init(){

        dbHelper=new DBhelper(this);
        menu_db=dbHelper.getReadableDatabase();

        printText=(TextView)findViewById(R.id.print_textview);
        searchItem=(EditText)findViewById(R.id.search_prod);

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog();
            }
        });

    }


    void readUserDb(){

        cursor = menu_db.query(MenuDataBase.TABLE_NAME,MenuDataBase.menuColumns,null,null,null,null,null,null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                _id = cursor.getInt(cursor.getColumnIndex("id"));
                _prod_name = cursor.getString(cursor.getColumnIndex(MenuDataBase.NAME));
                _tmp = cursor.getInt(cursor.getColumnIndex(MenuDataBase.TEMPERATURE));
                _mode = cursor.getString(cursor.getColumnIndex(MenuDataBase.MODE));
                _category = cursor.getString(cursor.getColumnIndex(MenuDataBase.CATEGORY));
                _duré = cursor.getString(cursor.getColumnIndex(MenuDataBase.DURE_DE_VIE));


            }
            printText.setText("id : "+_id+"\nname : "+_prod_name+"\nduré d vie : "
                    +_duré+"\nmode : "+_mode
                    +"\ntemperature : "+_tmp+"\n category : "+_category);
        }
    }

    void writeDummyDatabase(){
        ContentValues values=new ContentValues();
        values.put(MenuDataBase.NAME,"dela3");
        values.put(MenuDataBase.CATEGORY,"dessert");
        values.put(MenuDataBase.DURE_DE_VIE,"J+5");
        values.put(MenuDataBase.TEMPERATURE,-20);

        menu_db.insert(MenuDataBase.TABLE_NAME,null,values);
    }

}