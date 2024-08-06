package ASsigment.fptpoly.qqbook.api;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class NotifyConfig extends Application {
    public static final String CHANNEL_ID = "KKK";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Tên kênh
            CharSequence name = "Tên kênh";
            // Mô tả kênh
            String description = "Mô tả";
            // Độ ưu tiên của kênh
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // Âm thanh mặc định của thông báo
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // Cấu hình thuộc tính âm thanh của kênh thông báo
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            // Tạo kênh thông báo
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(uri, audioAttributes);

            // Thêm kênh vào NotificationManager
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
