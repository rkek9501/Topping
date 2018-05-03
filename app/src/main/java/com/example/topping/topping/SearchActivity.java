package com.example.topping.topping;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SearchActivity extends AbstractActivity {

    EditText editText;
    private Button findBtn;
    private ListView listView;
//안녕
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = (EditText)findViewById(R.id.main_editText);

        Intent intent = getIntent();
        String id = intent.getStringExtra("value");
        if(id.equals(""))
            Toast.makeText(this, "입력한 정보가 없습니다!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"입력한 값은"+id+"입니다.",Toast.LENGTH_SHORT).show();

        editText.setText(id);



        //Intent intent = getIntent();
        String data = intent.getStringExtra("value");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

        findBtn = (Button)findViewById(R.id.search_find_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContentActivity.class));
            }
        });

        LinearLayout container = (LinearLayout)findViewById(R.id.searchParent);
        View listitem = View.inflate(getApplicationContext(), R.layout.listview_item,container);
        listView = (ListView)findViewById(R.id.searchListview);

        SearchListViewAdapter adapter = new SearchListViewAdapter();
        for(int i=0; i<10; i++){
            adapter.addItem(null, "userName "+i, "hobby "+i,"date"+i);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"User ", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapter);
    }
}
