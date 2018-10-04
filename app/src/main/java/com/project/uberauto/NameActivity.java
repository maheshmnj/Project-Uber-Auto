package com.project.uberauto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NameActivity extends AppCompatActivity {
    private String url= "https://onkarbangale44.000webhostapp.com/Reg.php";
    private Button next;
    EditText first,last;  // first and last name
    private String phone; //phone number
    private RadioGroup rgroup;
    String currentUser;  // user / driver
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name);
        next = findViewById(R.id.next);
        first = findViewById(R.id.firstname);
        last = findViewById(R.id.lastname);
        Bundle bundle =getIntent().getExtras();
        phone = bundle.getString("number");
        rgroup = findViewById(R.id.rgroup);
        currentUser = "user";
        StringRequest request;
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked = findViewById(checkedId);
                Toast.makeText(NameActivity.this, checked.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!first.getText().toString().isEmpty()&&!last.getText().toString().isEmpty()) {
                    Toast.makeText(NameActivity.this, "lOGIN Success for "+phone, Toast.LENGTH_SHORT).show();
                    Intent start = new Intent(NameActivity.this, PostLogin.class);
                    startActivity(start);
                }
                else{
                    Toast.makeText(NameActivity.this, "first or last name left empty!", Toast.LENGTH_SHORT).show();
            }
        }
    });

         request =new StringRequest(Request.Method.GET, url+"?first="+first+"&last="+ last+"&phone="+phone,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject=new JSONObject(response);
                        if(!jsonObject.getBoolean("error"))
                        {
                            String obj=jsonObject.getString("error_msg");
                            Toast.makeText(getApplicationContext(),obj,Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
                        }

                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
        }
    });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
}

}
