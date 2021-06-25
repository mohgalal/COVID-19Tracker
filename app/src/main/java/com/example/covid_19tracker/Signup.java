package com.example.covid_19tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.covid_19tracker.Constant.SIGN_IN_URL;
import static com.example.covid_19tracker.Constant.SSN_FILE_NAME;
import static com.example.covid_19tracker.Constant.SSN_SP_KEY;

public class Signup extends AppCompatActivity {

    MySqliteDatabaseSSN db;
    TextInputEditText et_name,et_ssn,et_phone,et_pass;
    Button btn_register;
    TextInputLayout textInputLayout1,textInputLayout2,textInputLayout3,textInputLayout4;
    Animation anim1,anim2,anim3,anim4,anim5;
//    ProgressDialog progressDialog = new ProgressDialog(Signup.this);
    private static final String TAG= "Signup";
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar ;
    String SSN1,name,phone1;
    boolean insertResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_name=findViewById(R.id.signup_text_input_et_full_name);
        et_ssn=findViewById(R.id.signup_text_input_et_ssn);
        et_phone=findViewById(R.id.signup_text_input_et_phone);
//        et_pass=findViewById(R.id.signup_text_input_et_password);
        btn_register=findViewById(R.id.signup_btn_register);

//        progressBar=findViewById(R.id.progress_login);
//        Sprite doubleBounce = new FoldingCube();
//        progressBar.setIndeterminateDrawable(doubleBounce);
//        progressBar.setVisibility(View.VISIBLE);


        textInputLayout1=findViewById(R.id.signup_text_input_layout_full_name);
        textInputLayout2=findViewById(R.id.signup_text_input_layout_phone);
        textInputLayout3=findViewById(R.id.signup_text_input_layout_ssn);
//        textInputLayout4  = findViewById(R.id.signup_text_input_layout_password);

        anim1= AnimationUtils.loadAnimation(Signup.this, R.anim.anim1);
        anim2= AnimationUtils.loadAnimation(Signup.this, R.anim.anim2);
        anim3= AnimationUtils.loadAnimation(Signup.this, R.anim.anim3);
        anim4= AnimationUtils.loadAnimation(Signup.this, R.anim.anim4);
        anim5= AnimationUtils.loadAnimation(Signup.this, R.anim.anim5);

        textInputLayout1.setAnimation(anim1);
        textInputLayout2.setAnimation(anim2);
        textInputLayout3.setAnimation(anim3);
//        textInputLayout4.setAnimation(anim4);
        btn_register.setAnimation(anim5);

        db = new MySqliteDatabaseSSN(Signup.this);

//        textInputLayout4.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences(SSN_FILE_NAME, Context.MODE_PRIVATE);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(Signup.this, "moooo", Toast.LENGTH_LONG).show();
                signUp(v);
//                name = et_name.getText().toString();
//                SSN1 = et_ssn.getText().toString().trim();
//                phone1 = et_phone.getText().toString().trim();
//                editor = sharedPreferences.edit();
//                editor.putString(SSN_SP_KEY, SSN1);
//                editor.apply();
//                if (SSN1.equals("")) {
//                    et_ssn.setError("Please Enter Your SSN");
//                }else {
//                Intent intent = new Intent(Signup.this, Question.class);
//                startActivity(intent);}
//                SSNModel model = new SSNModel(Long.parseLong(SSN1));
//                insertResult = db.insertSSN(model);
//                if(insertResult){
//                    Toast.makeText(Signup.this,"Success Insertion"+" ,number of rows inserted "+db.getSSNCount(),Toast.LENGTH_LONG).show();
//                }
//                else {
//                    Toast.makeText(Signup.this,"Error Occurred",Toast.LENGTH_LONG).show();
//
//                }
            }
        });

    }

    private void signUp(View view) {
        name = et_name.getText().toString();
        SSN1 = et_ssn.getText().toString().trim();
        phone1 = et_phone.getText().toString().trim();


        // SSN=Integer.parseInt(SSN1);
//        String[] words = name.split(" ");
//        for (int i = 0; i < words.length; i++) {
//            if (!(words[i].length() <= 20)) {
//                et_name.setError("The name is too long");
//                break;
//            }
            if (name.equals("") && SSN1.equals("") && phone1.equals("")) {

                et_name.setError("Please Enter Your Name");
                et_ssn.setError("Please Enter Your SSN");
                et_phone.setError("Please Enter Your Phone");

            } else if (name.equals("")) {

                et_name.setError("Please Enter Your Name");

            }else if (name.length()>=23) {

                et_name.setError("The name is too long");

            }else if (phone1.length() != 11) {

                et_phone.setError("The phone should be 11 number");

            } else if (SSN1.length() != 14) {

                et_ssn.setError("The SSN should be 14 number");
            } else {

                RequestParams params = new RequestParams();

                params.put("full_name", name);
                params.put("SSN", SSN1);
                params.put("phone_number", phone1);
                params.put("infected", "0");
                params.put("longitude", "8.9");
                params.put("latitude", "9.0");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();


                AsyncHttpClient async = new AsyncHttpClient();
                async.setTimeout(6000000);
                async.post((SIGN_IN_URL), params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(String response) {
                        super.onSuccess(response);
                        Log.d(TAG, "onSuccess: ");
                        //Toast.makeText(Signup.this, "Success", Toast.LENGTH_SHORT).show();
                        try {
//                   progressDialog.dismiss();
                            Log.d(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                            JSONObject JO = jsonArray.getJSONObject(0);
                            String code = JO.getString("code");

                            if (code.equals("sign_up_true")) {
                                // Toast.makeText(Signup.this, "Success", Toast.LENGTH_SHORT).show();
                                editor = sharedPreferences.edit();
                                editor.putString(SSN_SP_KEY, SSN1);
                                editor.apply();
                                Toast.makeText(Signup.this, SSN1 + "", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Signup.this, Question.class);
                                // intent.putExtra(EXTRA_SSN, SSN1);
                                startActivity(intent);
                                finish();

//                       Bundle bundle =new Bundle();
//                       bundle.putString("fragment",SSN1);
//                       ProfileFragment fragment = new ProfileFragment();
//                       fragment.setArguments(bundle);

//                       FragmentManager fm = getSupportFragmentManager();
//                       FragmentTransaction ft = fm.beginTransaction();
//                       ft.replace(R.id.profileFragment,fragment);
//                       ft.commit();

                            } else
                                Toast.makeText(Signup.this, "sorry, try again", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        super.onFailure(statusCode, error, content);
                        Toast.makeText(Signup.this, error.getMessage() + content, Toast.LENGTH_SHORT).show();
                        if (statusCode == 404) {
                            Toast.makeText(Signup.this, "not found", Toast.LENGTH_SHORT).show();
                        } else if (statusCode >= 500 && statusCode <= 600) {
                            Toast.makeText(Signup.this, "error from animals", Toast.LENGTH_SHORT).show();
                        } else if (statusCode == 403) {
                            Toast.makeText(Signup.this, "forbidden error", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(Signup.this, "Unexpected error ", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    }
