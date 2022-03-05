package softtrack.apps.texteditor;

import android.content.ClipData;
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
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MemoryManagerActivity extends AppCompatActivity {

    public LinearLayout activityMemoryManagerContainerBody;
    public Menu myMenu;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_manager);

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.activity_memory_manager_menu, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initialize() {
        activityMemoryManagerContainerBody = findViewById(R.id.activity_memory_manager_container_body);
        initializeActionBar();
        showFiles();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showFiles() {
        String currentPath = getApplicationContext().getCacheDir().getPath();
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for(File fileInDir : listOfFiles){
                Log.d("debug", "manager: " + fileInDir.getPath());
                LinearLayout file = new LinearLayout(MemoryManagerActivity.this);
                file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                ImageView fileImg = new ImageView(MemoryManagerActivity.this);
                fileImg.setImageResource(R.drawable.folder);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(MemoryManagerActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                TextView fileName = new TextView(MemoryManagerActivity.this);
                String rawFileName = fileInDir.getName();
                fileName.setText(rawFileName);
                TextView fileMetaData = new TextView(MemoryManagerActivity.this);
                long countOfMeasure = 0l;
                String measure = "MB";
                if (fileInDir.length() >= 8796093022208l) {
                    countOfMeasure = fileInDir.length() / 8796093022208l;
                    measure = "GB";
                } else if (fileInDir.length() >= 8589934592l) {
                    countOfMeasure = fileInDir.length() / 8589934592l;
                    measure = "MB";
                } else if (fileInDir.length() >= 8388608l) {
                    countOfMeasure = fileInDir.length() / 8388608l;
                    measure = "KB";
                } else if (fileInDir.getTotalSpace() >= 8192l) {
                    countOfMeasure = fileInDir.length() / 8192l;
                    measure = "B";
                }
                String rawFileSize = countOfMeasure + " " + measure;
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    sdf = new SimpleDateFormat("dd MM YYYY г. HH:mm z");
                    sdf = new SimpleDateFormat("dd MM yyyy г. HH:mm");
                }
//                sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                String formattedDate = sdf.format(new Date(fileInDir.lastModified() * 1000L));
                String rawFileMetaData = rawFileSize + " " + formattedDate;
                fileMetaData.setText(rawFileMetaData);
                fileInfo.addView(fileName);
                fileInfo.addView(fileMetaData);
                file.addView(fileImg);
                file.addView(fileInfo);
                activityMemoryManagerContainerBody.addView(file);
            }
        } catch (InterruptedException e) {
            Log.d("debug", "ошибка в FileTask");
        } catch (ExecutionException e) {
            Log.d("debug", "ошибка в FileTask");
        }
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LinearLayout customView = new LinearLayout(MemoryManagerActivity.this);
        customView.setOrientation(LinearLayout.VERTICAL);
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        TextView customViewLabel = new TextView(MemoryManagerActivity.this);
        customViewLabel.setText("Открыть файл");
        TextView customViewPath = new TextView(MemoryManagerActivity.this);
        customViewPath.setText("/storage");
        LinearLayout.LayoutParams customViewPathLayoutParams = new LinearLayout.LayoutParams(50, 50);
        customViewPathLayoutParams.setMargins(25, 0, 0 , 0);
        customViewPath.setLayoutParams(customViewPathLayoutParams);
        customView.addView(customViewLabel);
        customView.addView(customViewPath);
        actionBar.setCustomView(customView);
    }

}
