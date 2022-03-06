package softtrack.apps.texteditor;

import android.content.ClipData;
import android.content.Intent;
import android.icu.number.Scale;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MemoryManagerActivity extends AppCompatActivity {

    public LinearLayout activityMemoryManagerContainerBody;
    public Menu myMenu;
    public LinearLayout activityMemoryManagerContainerBodyInternalStorage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_manager);

        initialize();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initialize() {
        findViews();
        initializeActionBar();
        addHandlers();
        showDevices();
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        TextView customView = new TextView(MemoryManagerActivity.this);
        customView.setText("Диспетчер памяти");
        actionBar.setCustomView(customView);
    }

    public static HashSet<String> getExternalMounts() {
        final HashSet<String> out = new HashSet<String>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }
        return out;
    }

    public void showDevices() {
        HashSet<String> devices = getExternalMounts();
        for (String device: devices) {
            TextView mountedDevice = new TextView(MemoryManagerActivity.this);
            mountedDevice.setText("Подключенное устройство");
            activityMemoryManagerContainerBody.addView(mountedDevice);
        }
    }

    public void addHandlers() {
        activityMemoryManagerContainerBodyInternalStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoryManagerActivity.this, OpenFileActivity.class);
                MemoryManagerActivity.this.startActivity(intent);
            }
        });
    }

    public void findViews() {
        activityMemoryManagerContainerBody = findViewById(R.id.activity_memory_manager_container_body);
        activityMemoryManagerContainerBodyInternalStorage = findViewById(R.id.activity_memory_manager_container_body_internal_storage_aside);
    }

}
