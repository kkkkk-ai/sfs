package com.example.app;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<CostBean> mCostBeenList;
    private DatabaseHelper mDatabaseHelper;
    private CostListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHelper=new DatabaseHelper(this);

        mCostBeenList=new ArrayList<>();
        initCostData();
        ListView costList= (ListView) findViewById(R.id.lv_main);

        mAdapter=new CostListAdapter(this,mCostBeenList);
        costList.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
                View viewDialog=inflater.inflate(R.layout.new_cost_data,null);
                final EditText title= (EditText) viewDialog.findViewById(R.id.et_cost_title);
                final EditText money= (EditText) viewDialog.findViewById(R.id.et_cost_money);
                final DatePicker date= (DatePicker) viewDialog.findViewById(R.id.dp_cost_date);

                builder.setView(viewDialog);
                builder.setTitle("新的花费");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CostBean coseBean=new CostBean();
                        coseBean.costTitle=title.getText().toString();
                        coseBean.costMoney=money.getText().toString();
                        coseBean.costDate=date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth();
                        mDatabaseHelper.insertCost(coseBean);
                        mCostBeenList.add(coseBean);

                        mAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel",null);

                //勿忘！！！
                builder.create().show();
            }
        });

        //打开SQLiteStudio
        //SQLiteStudioService.instance().start(this);

    }

    private void initCostData() {
        Cursor cursor= mDatabaseHelper.getAllCostData();
        if (cursor!=null){
            while (cursor.moveToNext()){
                CostBean costBean=new CostBean();
                costBean.costTitle=cursor.getString(cursor.getColumnIndex("cost_title"));
                costBean.costDate=cursor.getString(cursor.getColumnIndex("cost_date"));
                costBean.costMoney=cursor.getString(cursor.getColumnIndex("cost_money"));
                mCostBeenList.add(costBean);
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_chart) {
//            Intent intent=new Intent(MainActivity.this,ChartActivity.class);
//            intent.putExtra("cost_list", (Serializable) mCostBeenList);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //SQLiteStudioService.instance().stop();
        super.onDestroy();
    }
}

