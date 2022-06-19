package com.example.buffalogrillapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TabOneFragment extends Fragment {

    private RecyclerView recyclerview;
    SQLiteDatabase menu_db;
    DBhelper dbHelper;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.tab_one_fragment, container, false);

        recyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbHelper=new DBhelper(this.getContext());
        menu_db=dbHelper.getReadableDatabase();

        try {
            RVAdapter adapter = new RVAdapter(getListMenu(0),this.getActivity());
            recyclerview.setAdapter(adapter);
        }catch (NullPointerException e){
            Log.d("Nazim","null pointer");
        }
    }

    public List<Menu> getListMenu(int tab){

        List<Menu> menus = new ArrayList<>();
        int _tmp,_id;
        String _mode,_name,_duré,_cat;

        cursor = menu_db.rawQuery("SELECT *FROM " + MenuDataBase.TABLE_NAME + "" +
        " WHERE " + MenuDataBase.CATEGORY + "=?", new String[]{Menu.MENU_TABs[tab]});

        int count = cursor.getCount();
        Log.d("Nazim","cursor count:"+count);

            Log.d("Nazim","cursor is not empty bitch");
            if (count>0)
            {
                cursor.moveToFirst();
                for(int i = 0; i < count; i++) {

                    _id=cursor.getInt(cursor.getColumnIndex("id"));
                    _name = cursor.getString(cursor.getColumnIndex(MenuDataBase.NAME));
                    _duré = cursor.getString(cursor.getColumnIndex(MenuDataBase.DURE_DE_VIE));
                    _cat = cursor.getString(cursor.getColumnIndex(MenuDataBase.CATEGORY));
                    _mode = cursor.getString(cursor.getColumnIndex(MenuDataBase.MODE));
                    _tmp = cursor.getInt(cursor.getColumnIndex(MenuDataBase.TEMPERATURE));
                    Log.d("Nazim", "product name :" + _name);
                    menus.add(new Menu(_id,_name, _cat, _duré, _mode, _tmp));
                    cursor.moveToNext();
                }
            }

        return menus;
    }


}
