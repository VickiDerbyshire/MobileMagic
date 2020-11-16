package com.example.umar1_mdproject_mtg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    CardDB_Helper mySQL_CardDB_Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connecting to the database
        mySQL_CardDB_Helper = new CardDB_Helper(this, null, null, 1);

        //below line to clears the previous rows each time app runs (it just removes every row from the table)
        mySQL_CardDB_Helper.DeleteCardRows();

        //Manually Add Rows to the database
        mySQL_CardDB_Helper.addCard("x11111", "set1", "red", "1", "$10", "img1");
        mySQL_CardDB_Helper.addCard("y22222", "set2", "teal", "2", "$22", "img2");

        //call the function to display to screen
        View_Tables();
    }

    private void View_Tables() {
        setContentView(R.layout.activity_main);

        TextView Table_View = (TextView) findViewById(R.id.lblViewTable);
        mySQL_CardDB_Helper = new CardDB_Helper(this, null, null, 1);

        //Cursor goes through each row of the table and outputs it to the screen
        Cursor c = mySQL_CardDB_Helper.ViewData();

        if (c.getCount() == 0) {
            Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
        } else {
            while (c.moveToNext()) {

                Table_View.append("\n\nUSER CARD ID: " + c.getString(c.getColumnIndex("user_card_ID")) + "   ");
                Table_View.append("CARD ID: " + c.getString(c.getColumnIndex("card_PK")) + "   ");
                Table_View.append("SET: " + c.getString(c.getColumnIndex("card_set")) + "   ");
                Table_View.append("COLOR: " + c.getString(c.getColumnIndex("card_color")) + "   \n");
                Table_View.append("QTY: " + c.getString(c.getColumnIndex("card_qty")) + "   ");
                Table_View.append("$: " + c.getString(c.getColumnIndex("card_price")) + "   ");
                Table_View.append("IMG: " + c.getString(c.getColumnIndex("card_img")) + "\n");
            }
        }
    }

    public void MTG_Search_View(View view) {
        Intent openSearch = new Intent(getApplicationContext(), MTG_Search.class);
        startActivity(openSearch);
    }
}