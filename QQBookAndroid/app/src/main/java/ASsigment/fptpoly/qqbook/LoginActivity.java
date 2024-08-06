package ASsigment.fptpoly.qqbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ASsigment.fptpoly.qqbook.Model.Favorite;
import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView tv_Register;
    EditText edUsername, edPassword;
    Button btnLogin;

    String idUser, coin, fullName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btn_login);

        tv_Register = findViewById(R.id.tv_Register);

        tv_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edUsername.getText().toString().trim();
                String password = edPassword.getText().toString().trim();

                User user = new User(name, password);

                Log.d("TAG", "User: " + user.getUsername() + " - " + user.getPassword());

                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không để trống tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    InterfaceAPI.apiservice.getUserWithName(edUsername.getText().toString().trim()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                if (user != null) {
                                    idUser = user.get_id();
//                                    money = user.getMoney();
                                    Log.d("1.5", "ID: " + idUser);
                                    rememberUser(edUsername.getText().toString(), user.get_id(), user.getEmail());
                                    checkSignIn();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            // Lỗi kết nối
                            Log.e("LoginActivity", "Error: " + t.getMessage());
                            Toast.makeText(LoginActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }
    private void checkSignIn() {
        List<Favorite> emptyList = new ArrayList<>();


        User user = new User("",edUsername.getText().toString(), edPassword.getText().toString(),"", "","","",emptyList);

        InterfaceAPI.apiservice.Login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    if (loggedInUser != null) {

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.putExtra("idUser",id_user );
//                        intent.putExtra("money",money );
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void rememberUser(String username, String userID, String email) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("USERID", userID);
        edit.putString("USERNAME", username);
        edit.putString("USEREMAIL", email);
        edit.commit();
    }
}
