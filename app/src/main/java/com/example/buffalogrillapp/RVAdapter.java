package com.example.buffalogrillapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.example.buffalogrillapp.MenuDataBase.TABLE_NAME;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ItemViewHolder> {

    private final static String PASSWORD="DEADLINE";
    private final List<Menu> mValues;
    private Context context;

    String pass = "";

    SQLiteDatabase menu_db;
    DBhelper dbHelper;


    public RVAdapter(List<Menu> items,Context context) {
        mValues = items;
        this.context=context;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName,itemMode,itemDuré,itemTmp;
        public Menu mItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemMode = (TextView) itemView.findViewById(R.id.item_mode);
            itemDuré = (TextView) itemView.findViewById(R.id.item_duré);
            itemTmp = (TextView) itemView.findViewById(R.id.item_temp);
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {


        holder.mItem = mValues.get(position);

        holder.itemName.setText(mValues.get(position).getName()+"");
        holder.itemDuré.setText(mValues.get(position).getDuré_d_vie()+"");
        holder.itemMode.setText(mValues.get(position).getMode_d_emploie()+"");
        holder.itemTmp.setText(mValues.get(position).getTemperature()+"C°");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               infoDialog(mValues,position);
            }
        });

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        this.context=viewGroup.getContext();
        dbHelper=new DBhelper(this.context);
        menu_db=dbHelper.getReadableDatabase();

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void passwordDialog(List<Menu> menu, int position){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.password_dialog_box, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());

        builder.setView(dialogView);

        //builder.setTitle("Information Produit");
        final EditText edit_pass=(EditText) dialogView.findViewById(R.id.edit_pass);
        final Button button_pass=(Button) dialogView.findViewById(R.id.button_pass);

        builder.setCancelable(true);

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

        button_pass.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        button_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass =edit_pass.getText().toString();

                if(pass.equals(PASSWORD)) {
                    menu_db = dbHelper.getWritableDatabase();
                    menu_db.delete(TABLE_NAME, MenuDataBase.NAME + " = ?", new String[]{menu.get(position).getName()});
                    menu_db.close();
                    menu.remove(position);
                    notifyItemRemoved(position);
                }else Toast.makeText(context,"Wrong Password",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

    }


    public static String parseDuré(String duré_d_vie){

        if (duré_d_vie.contains("mois"))return getUseTime(duré_d_vie,1);
        else if(duré_d_vie.contains("J+")) return getUseTime(duré_d_vie,0);
        else if(duré_d_vie.equals("J")) return "Jour méme";
        else if(duré_d_vie.contains("minute")) return "A la minute";

        else return duré_d_vie;

    }
    public static String getUseTime(String input,int code)
    {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date=dtf.format(now);
        // Get an instance of LocalTime
        // from date
        LocalDate currentDate
                = LocalDate.parse(date);

        // Get day from date
        int day = currentDate.getDayOfMonth();
        // Get month from date
        int month = currentDate.getMonth().getValue();
        // Get year from date
        int year = currentDate.getYear();
        //get number of days after J letter
        int n=Character.getNumericValue(input.charAt(2));

        if(code==0) day=day+n+1;
        else month=month+n;

        String output=year+"-"+month+"-"+day;

        DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH);
        DateTimeFormatter dtfOutput = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH);

        return LocalDate.parse(output, dtfInput).format(dtfOutput)+":"+output;
    }

    public void infoDialog(List<Menu> menu, int position){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.info_dialog_box, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());

        builder.setView(dialogView);

        builder.setTitle("Information Produit");
        final TextView product_name=(TextView)dialogView.findViewById(R.id.text_name);
        final TextView product_mode=(TextView)dialogView.findViewById(R.id.text_mode);
        final TextView product_duré=(TextView)dialogView.findViewById(R.id.text_duré);
        final TextView product_tmp=(TextView)dialogView.findViewById(R.id.text_tmp);

        product_name.setText(menu.get(position).getName());
        product_mode.setText(menu.get(position).getMode_d_emploie());
        product_duré.setText(menu.get(position).getDuré_d_vie());
        product_tmp.setText(""+menu.get(position).getTemperature());

        final TextView date=(TextView)dialogView.findViewById(R.id.text_date);
        String dateDbValue=menu.get(position).getDuré_d_vie();
        date.setText(""+parseDuré(dateDbValue));

        builder.setCancelable(true);

        Button delete=(Button)dialogView.findViewById(R.id.suprimer_info);
        Button ok=(Button)dialogView.findViewById(R.id.info_ok);

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordDialog(menu,position);
                alertDialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    alertDialog.dismiss();
            }
        });

    }
}
