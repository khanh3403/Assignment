package ASsigment.fptpoly.qqbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.net.URISyntaxException;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ASsigment.fptpoly.qqbook.Fragment.FavoFragment;
import ASsigment.fptpoly.qqbook.Fragment.HomeFragment;
import ASsigment.fptpoly.qqbook.Fragment.SettingFragment;
import ASsigment.fptpoly.qqbook.api.NotifyConfig;


public class MainActivity extends AppCompatActivity {
    private Socket mSocket;


    {
        try {
            mSocket = IO.socket(InforActivity.Constants.BASE_URL);

        } catch (URISyntaxException e) {
        }
    }
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mở kết nối
        mSocket.connect();
        // lắng nghe sự kiện
        mSocket.on("add comment", newComment);
        mSocket.on("new_comic_added", newComment);

        NavigationBarView view = findViewById(R.id.bottom_navi);
        sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String role = sharedPreferences.getString("ROLE", "");

        view.getMenu().clear();
        view.inflateMenu(R.menu.bottom_navigation_menu_user);
        replaceFragment(new HomeFragment());

        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_Home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.action_Favorite) {
                    replaceFragment(new FavoFragment());
                    return true;
                } else if (item.getItemId() == R.id.action_Setting) {
                    replaceFragment(new SettingFragment());
                    return true;
                } else {
                    return false;
                }
            }
        });


    }
    private Emitter.Listener newComment = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data = (String) args[0];
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Hiển thị thông báo khi có một bình luận mới được thêm vào
                    postNotify("Thông báo", data);
                }
            });
        }
    };


    private void postNotify(String title, String content) {
        Notification customNotification = new NotificationCompat.Builder(MainActivity.this, NotifyConfig.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_delete)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .build();
        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(MainActivity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy,customNotification );
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_content, fragment);
        transaction.commit();
    }
}