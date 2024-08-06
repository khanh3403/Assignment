package ASsigment.fptpoly.qqbook.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import ASsigment.fptpoly.qqbook.LoginActivity;
import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {
    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvName, tvEmail;
        Button btnChangePass, btnLogout;
        tvName = view.findViewById(R.id.tv_setting_name);
        tvEmail = view.findViewById(R.id.tv_setting_email);

        btnChangePass = view.findViewById(R.id.btn_setting_changepass);
        btnLogout = view.findViewById(R.id.btn_setting_logout);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String userid = sharedPreferences.getString("USERID", "");
        String userName = sharedPreferences.getString("USERNAME", "");
        String userEmail = sharedPreferences.getString("USEREMAIL", "");

        tvName.setText(userName);
        tvEmail.setText(userEmail);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_edit_password);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText etEditComment = dialog.findViewById(R.id.et_edit_comment);

                AppCompatButton btnSubmit = dialog.findViewById(R.id.btn_dialog_item_submit);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btn_dialog_item_cancel);
                TextInputEditText etCurrentPassword = dialog.findViewById(R.id.ed_setting_Password);
                TextInputEditText etNewPassword = dialog.findViewById(R.id.ed_setting_newPassword);
                TextInputEditText etConfirmPassword = dialog.findViewById(R.id.ed_setting_newPassword2);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentPassword = etCurrentPassword.getText().toString().trim();
                        String newPassword = etNewPassword.getText().toString().trim();
                        String confirmPassword = etConfirmPassword.getText().toString().trim();

                        // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
                        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu mới có khớp nhau không
                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(getContext(), "Mật khẩu mới không khớp, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Gửi yêu cầu thay đổi mật khẩu đến server
                        InterfaceAPI.apiservice.changePassword(userid, new ChangePasswordRequest(currentPassword, newPassword)).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    // Xử lý thành công
                                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss(); // Đóng dialog
                                } else {
                                    // Xử lý khi mật khẩu cũ không chính xác
                                    Toast.makeText(getContext(), "Mật khẩu cũ không chính xác, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                // Xử lý khi gặp lỗi kết nối
                                Toast.makeText(getContext(), "Lỗi kết nối, vui lòng kiểm tra lại đường truyền mạng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });



                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(i);
                getActivity().finish();
            }
        });


    }
}