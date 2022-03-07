package softtrack.apps.texteditor;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RecentFilesActivity extends AppCompatActivity {

    public TabLayout activityRecentFilesContainerTabs;
    public LinearLayout activityRecentFilesContainerScrollBody;
    public Menu myMenu;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_files);

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.activity_recent_files_menu, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        boolean isMoreMenuItemClearHistoryBtn = itemId == R.id.activity_recent_files_menu_more_menu_clear_history_btn;
        if (isMoreMenuItemClearHistoryBtn) {
            activityRecentFilesContainerScrollBody.removeAllViews();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initialize() {
        findViews();
        addHandlers();
        initializeStartContent();
    }

    public void findViews() {
        activityRecentFilesContainerTabs = findViewById(R.id.activity_recent_files_container_tabs);
        activityRecentFilesContainerScrollBody = findViewById(R.id.activity_recent_files_container_scroll_body);
    }

    public void addHandlers() {
        activityRecentFilesContainerTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTabPosition = activityRecentFilesContainerTabs.getSelectedTabPosition();
                boolean isRecentOpenedFiles = selectedTabPosition == 0;
                if (isRecentOpenedFiles) {
                    initializeStartContent();
                } else {
                    showFiles(activityRecentFilesContainerScrollBody, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showFiles(LinearLayout fileManager, boolean isRecentAdd) {
        fileManager.removeAllViews();
        String currentPath = getApplicationContext().getCacheDir().getPath();
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for(File fileInDir : listOfFiles){

                BasicFileAttributes attrs;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    attrs = Files.readAttributes(fileInDir.toPath(), BasicFileAttributes.class);
                    long currentTimeInMillis = System.currentTimeMillis();
                    boolean isCanAdd = false;
                    if (isRecentAdd) {
                        FileTime time = attrs.creationTime();
                        long timeInMillis = time.toMillis();
                        isCanAdd = timeInMillis > currentTimeInMillis - 1000 * 60 * 60;
                    } else {
                        long timeInMillis = fileInDir.lastModified();
                        isCanAdd = timeInMillis > currentTimeInMillis - 1000 * 60 * 60;
                    }
                    if (isCanAdd) {
                        Log.d("debug", "manager: " + fileInDir.getPath());
                        LinearLayout file = new LinearLayout(RecentFilesActivity.this);
                        file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        ImageView fileImg = new ImageView(RecentFilesActivity.this);
                        fileImg.setImageResource(R.drawable.folder);
                        fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                        fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout fileInfo = new LinearLayout(RecentFilesActivity.this);
                        fileInfo.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                        fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                        fileInfo.setLayoutParams(fileInfoLayoutParams);
                        TextView fileName = new TextView(RecentFilesActivity.this);
                        String rawFileName = fileInDir.getName();
                        fileName.setText(rawFileName);
                        TextView fileMetaData = new TextView(RecentFilesActivity.this);
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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sdf = new SimpleDateFormat("dd MM yyyy г. HH:mm");
                        }
                        String formattedDate = sdf.format(new Date(fileInDir.lastModified() * 1000L));
                        String rawFileMetaData = rawFileSize + " " + formattedDate;
                        fileMetaData.setText(rawFileMetaData);
                        fileInfo.addView(fileName);
                        fileInfo.addView(fileMetaData);
                        file.addView(fileImg);
                        file.addView(fileInfo);
                        fileManager.addView(file);
                        file.setContentDescription(fileInDir.getPath());
                        file.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String filePath = view.getContentDescription().toString();
                                Log.d("debug", "filePath: " + filePath);
                                Intent intent = new Intent(RecentFilesActivity.this, MainActivity.class);
                                intent.putExtra("filePath", filePath);
                                RecentFilesActivity.this.startActivity(intent);
                            }
                        });
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.d("debug", "ошибка в FileTask");
        } catch (ExecutionException e) {
            Log.d("debug", "ошибка в FileTask");
        } catch (IOException e) {
            Log.d("debug", "ошибка в FileTask");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initializeStartContent() {
        initializeActionBar();
        showFiles(activityRecentFilesContainerScrollBody, false);
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Недавние фалы");
    }

}
