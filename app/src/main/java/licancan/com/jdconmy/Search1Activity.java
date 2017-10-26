package licancan.com.jdconmy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import licancan.com.jdconmy.R;

public class Search1Activity extends AppCompatActivity implements View.OnClickListener {

    private ImageView search_back1;
    private EditText search_name1;
    private Button but_search1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);
        initView();
    }


    private void initView() {
        search_back1 = (ImageView) findViewById(R.id.search_back1);
        search_name1 = (EditText) findViewById(R.id.search_name1);
        but_search1 = (Button) findViewById(R.id.but_search1);

        search_back1.setOnClickListener(this);
        but_search1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.search_back1:
                finish();
                break;

            case R.id.but_search1:
                String name=search_name1.getText().toString();
                Intent intent=new Intent(this,Search2Activity.class);
                intent.putExtra("name",name);
                startActivity(intent);
                break;
        }
    }
}
