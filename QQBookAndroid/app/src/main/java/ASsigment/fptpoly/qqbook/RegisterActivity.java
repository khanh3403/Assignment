package ASsigment.fptpoly.qqbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    EditText edUsername,edUserpass, edEmail, edFullname;
    Button btn_register, btn_register_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edUsername=findViewById(R.id.edUsername_regis);
        edUserpass=findViewById(R.id.edUserpass_regis);
        edEmail=findViewById(R.id.edEmail_regis);
        edFullname=findViewById(R.id.edFullname_regis);

        btn_register=findViewById(R.id.btn_Register);
        btn_register_back = findViewById(R.id.btn_Register_back);
        btn_register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edUsername.getText().toString();
                String password = edUserpass.getText().toString();
                String email = edEmail.getText().toString();
                String fullname = edFullname.getText().toString();
                if (name.isEmpty() || password.isEmpty() || email.isEmpty() || fullname.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Không được để trông", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    PostSignUp();
                }
            }
        });
    }
    private void PostSignUp() {
        User user = new User(edUsername.getText().toString(), edUserpass.getText().toString()
                , edEmail.getText().toString(), edFullname.getText().toString());
        InterfaceAPI.apiservice.Register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    if (loggedInUser != null) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Đăng ký deo dc", Toast.LENGTH_SHORT).show();
            }
        });
    }

}