package softtrack.apps.texteditor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class OpenFileActivity extends AppCompatActivity {

    public Menu myMenu;
    public LinearLayout activityOpenFileContainerScrollBody;
    @SuppressLint("WrongConstant") public SQLiteDatabase db;
    public String currentPath = "";
    public ArrayList<HashMap<String, Object>> bookmarks;
    public boolean isDetectedBookmark = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file);

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.activity_open_file_menu, menu);
        myMenu = menu;
        initDB();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        boolean isBookmarkBtn = itemId == R.id.activity_open_file_menu_star_btn;
        if (isBookmarkBtn) {
            if (!isDetectedBookmark) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpenFileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_add_bookmark_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                EditText activityAddBookmarkDialogContainerNameField = dialogView.findViewById(R.id.activity_add_bookmark_dialog_container_name_field);
                TextView activityAddBookmarkDialogContainerPathField = dialogView.findViewById(R.id.activity_add_bookmark_dialog_container_path_field);
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CharSequence  rawActivityAddBookmarkDialogContainerNameFieldContent = activityAddBookmarkDialogContainerNameField.getText();
                        String activityAddBookmarkDialogContainerNameFieldContent = rawActivityAddBookmarkDialogContainerNameFieldContent.toString();
                        CharSequence rawActivityAddBookmarkDialogContainerPathFieldContent = activityAddBookmarkDialogContainerPathField.getText();
                        String activityAddBookmarkDialogContainerPathFieldContent = rawActivityAddBookmarkDialogContainerPathFieldContent.toString();
                        db.execSQL("INSERT INTO \"bookmarks\"(name, path) VALUES (\"" + activityAddBookmarkDialogContainerNameFieldContent + "\", \"" + activityAddBookmarkDialogContainerPathFieldContent  + "\");");
                        /*ColorDrawable a = new ColorDrawable();
                        a.setColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.SCREEN);
                        myMenu.findItem(R.id.activity_open_file_menu_star_btn).setIcon(a);*/
                        myMenu.findItem(R.id.activity_open_file_menu_star_btn).setIcon(R.drawable.filled_star);
                        isDetectedBookmark = true;
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("Закладка");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        activityAddBookmarkDialogContainerNameField.setText(new File(currentPath).getName());
                        activityAddBookmarkDialogContainerPathField.setText(currentPath);
                    }
                });
                alert.show();
            } else {
                db.execSQL("DELETE FROM bookmarks WHERE path=\"" + currentPath + "\";");
                String toastMessage = "Закладка удалена";
                Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
                toast.show();
                /*ColorDrawable a = new ColorDrawable();
                a.setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.SCREEN);
                myMenu.findItem(R.id.activity_open_file_menu_star_btn).setIcon(a);*/
                myMenu.findItem(R.id.activity_open_file_menu_star_btn).setIcon(R.drawable.star);
                isDetectedBookmark = false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initialize() {
        findViews();
        initializeActionBar();
        showFiles();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showFiles() {
        currentPath = getApplicationContext().getCacheDir().getPath();
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for (File fileInDir : listOfFiles){
                Log.d("debug", "manager: " + fileInDir.getPath());
                LinearLayout file = new LinearLayout(OpenFileActivity.this);
                file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                ImageView fileImg = new ImageView(OpenFileActivity.this);
                fileImg.setImageResource(R.drawable.folder);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(OpenFileActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                TextView fileName = new TextView(OpenFileActivity.this);
                String rawFileName = fileInDir.getName();
                fileName.setText(rawFileName);
                TextView fileMetaData = new TextView(OpenFileActivity.this);
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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    sdf = new SimpleDateFormat("dd MM yyyy г. HH:mm");
                }
                String formattedDate = sdf.format(new Date(fileInDir.lastModified() * 1000L));
                String rawFileMetaData = rawFileSize + " " + formattedDate;
                fileMetaData.setText(rawFileMetaData);
                fileInfo.addView(fileName);
                fileInfo.addView(fileMetaData);
                file.addView(fileImg);
                file.addView(fileInfo);
                activityOpenFileContainerScrollBody.addView(file);
                file.setContentDescription(fileInDir.getPath());
                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String filePath = view.getContentDescription().toString();
                        Log.d("debug", "filePath: " + filePath);
                        Intent intent = new Intent(OpenFileActivity.this, MainActivity.class);
                        intent.putExtra("filePath", filePath);
                        OpenFileActivity.this.startActivity(intent);
                    }
                });
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
        LinearLayout customView = new LinearLayout(OpenFileActivity.this);
        customView.setOrientation(LinearLayout.VERTICAL);
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        TextView customViewLabel = new TextView(OpenFileActivity.this);
        customViewLabel.setText("Открыть файл");
        TextView customViewPath = new TextView(OpenFileActivity.this);
        customViewPath.setText("/storage");
        LinearLayout.LayoutParams customViewPathLayoutParams = new LinearLayout.LayoutParams(50, 50);
        customViewPathLayoutParams.setMargins(25, 0, 0 , 0);
        customViewPath.setLayoutParams(customViewPathLayoutParams);
        customView.addView(customViewLabel);
        customView.addView(customViewPath);
        actionBar.setCustomView(customView);
    }

    public void findViews() {
        activityOpenFileContainerScrollBody = findViewById(R.id.activity_open_file_container_scroll_body);
    }

    @SuppressLint("WrongConstant")
    public void initDB() {
        db = openOrCreateDatabase("text-editor-database.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor bookmarksCursor = db.rawQuery("Select * from bookmarks", null);
        bookmarksCursor.moveToFirst();
        long bookmarksCount = DatabaseUtils.queryNumEntries(db, "bookmarks");
        boolean isHaveBookmarks = bookmarksCount >= 1;
        bookmarks = new ArrayList<HashMap<String, Object>>();
        if (isHaveBookmarks) {
            for (int i = 0; i < bookmarksCount; i++) {
                HashMap bookmark = new HashMap<String, Object>();
                String bookmarkName = bookmarksCursor.getString(1);
                String bookmarkPath = bookmarksCursor.getString(2);
                bookmark.put("name", bookmarkName);
                bookmark.put("path", bookmarkPath);
                bookmarks.add(bookmark);
            }
        }
        checkPath();
    }

    public void checkPath() {
        for (HashMap<String, Object> bookmark : bookmarks) {
            String bookmarkPath = String.valueOf(bookmark.get("path"));
            if (bookmarkPath.equalsIgnoreCase(currentPath)) {
                ColorDrawable a = new ColorDrawable();
                a.setColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.SCREEN);
                myMenu.findItem(R.id.activity_open_file_menu_star_btn).setIcon(R.drawable.filled_star);
                isDetectedBookmark = true;
            }
        }
    }

}
