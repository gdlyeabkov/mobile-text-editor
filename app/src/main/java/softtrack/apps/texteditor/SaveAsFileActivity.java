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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SaveAsFileActivity extends AppCompatActivity {

    public Menu myMenu;
    public LinearLayout activityOpenFileContainerScrollBody;
    @SuppressLint("WrongConstant") public SQLiteDatabase db;
    public String currentPath = "";
    public ArrayList<HashMap<String, Object>> bookmarks;
    public boolean isDetectedBookmark = false;
    public boolean isSortByName = true;
    public FloatingActionButton activitySaveAsFileContainerCreateFolderBtn;
    public FloatingActionButton activitySaveAsFileContainerSaveAsBtn;
    public boolean isActivitySaveAsFileContainerAddBtnHidden = true;
    public ExtendedFloatingActionButton activitySaveAsFileContainerAddBtn;
    public String savedContent = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_as_file);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        boolean isListNameBtn = itemId == R.id.activity_open_file_menu_list_btn_menu_name_btn;
        boolean isListDateBtn = itemId == R.id.activity_open_file_menu_list_btn_menu_date_btn;
        boolean isBookmarkBtn = itemId == R.id.activity_open_file_menu_star_btn;
        boolean isFilterBtn = itemId == R.id.activity_open_file_menu_star_btn;
        if (isListNameBtn) {
            isSortByName = true;
            showFiles();
        } else if (isListDateBtn) {
            isSortByName = false;
            showFiles();
        } else if (isBookmarkBtn) {
            if (!isDetectedBookmark) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveAsFileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_add_bookmark_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
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
        getExtras();
        findViews();
        initializeActionBar();
        addHandlers();
        showFiles();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showFiles() {
        currentPath = getApplicationContext().getCacheDir().getPath();
        // File openedFile = new File(currentPath + "/music.mp3");
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            List<File> listOfSortedFiles = Arrays.asList(listOfFiles);
            Collections.sort(listOfSortedFiles);
            List<File> sortedList;
            if (isSortByName) {
                sortedList = listOfSortedFiles.stream()
                        .sorted(Comparator.comparing(File::getName))
                        .collect(Collectors.toList());
            } else {
                sortedList = listOfSortedFiles.stream()
                        .sorted(Comparator.comparing(File::lastModified))
                        .collect(Collectors.toList());
            }
            activityOpenFileContainerScrollBody.removeAllViews();
            for (File fileInDir : sortedList){
                Log.d("debug", "manager: " + fileInDir.getPath());
                LinearLayout file = new LinearLayout(SaveAsFileActivity.this);
                file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                ImageView fileImg = new ImageView(SaveAsFileActivity.this);
                fileImg.setImageResource(R.drawable.folder);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(SaveAsFileActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                TextView fileName = new TextView(SaveAsFileActivity.this);
                String rawFileName = fileInDir.getName();
                fileName.setText(rawFileName);
                TextView fileMetaData = new TextView(SaveAsFileActivity.this);
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
                        Intent intent = new Intent(SaveAsFileActivity.this, MainActivity.class);
                        intent.putExtra("filePath", filePath);
                        SaveAsFileActivity.this.startActivity(intent);
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
        LinearLayout customView = new LinearLayout(SaveAsFileActivity.this);
        customView.setOrientation(LinearLayout.VERTICAL);
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        TextView customViewLabel = new TextView(SaveAsFileActivity.this);
        customViewLabel.setText("Сохранить как");
        TextView customViewPath = new TextView(SaveAsFileActivity.this);
        customViewPath.setText("/storage");
        LinearLayout.LayoutParams customViewPathLayoutParams = new LinearLayout.LayoutParams(50, 50);
        customViewPathLayoutParams.setMargins(25, 0, 0 , 0);
        customViewPath.setLayoutParams(customViewPathLayoutParams);
        customView.addView(customViewLabel);
        customView.addView(customViewPath);
        actionBar.setCustomView(customView);
    }

    public void findViews() {
        activityOpenFileContainerScrollBody = findViewById(R.id.activity_save_file_container_scroll_body);
        activitySaveAsFileContainerCreateFolderBtn = findViewById(R.id.activity_save_as_file_container_create_folder_btn);
        activitySaveAsFileContainerSaveAsBtn = findViewById(R.id.activity_save_as_file_container_save_as_btn);
        activitySaveAsFileContainerAddBtn = findViewById(R.id.activity_save_as_file_container_add_btn);
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

    public void addHandlers() {
        activitySaveAsFileContainerAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isActivitySaveAsFileContainerAddBtnHidden) {
                    activitySaveAsFileContainerCreateFolderBtn.show();
                    activitySaveAsFileContainerSaveAsBtn.show();
                } else {
                    activitySaveAsFileContainerCreateFolderBtn.hide();
                    activitySaveAsFileContainerSaveAsBtn.hide();
                }
                isActivitySaveAsFileContainerAddBtnHidden = !isActivitySaveAsFileContainerAddBtnHidden;
            }
        });
        activitySaveAsFileContainerCreateFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveAsFileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_create_folder_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                EditText activityCreateFolderDialogContainerInput = dialogView.findViewById(R.id.activity_create_folder_dialog_container_input);
                builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CharSequence  rawActivityCreateFolderDialogContainerInputContent = activityCreateFolderDialogContainerInput.getText();
                        String activityCreateFolderDialogContainerInputContent = rawActivityCreateFolderDialogContainerInputContent.toString();
                        String filePath = currentPath + "/" + activityCreateFolderDialogContainerInputContent;
                        File newFile = new File(filePath);
                        try {
                            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
                        } catch (FileNotFoundException e) {
                            Log.d("debug", "ошибка при создании файла");
                        } catch (UnsupportedEncodingException e) {
                            Log.d("debug", "ошибка при создании файла");
                        }
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.setTitle("Создать папку");
                alert.show();
            }
        });
        activitySaveAsFileContainerSaveAsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSaveDialog();
            }
        });
    }

    public void openSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveAsFileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_save_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText activitySaveDialogFooterFileNameInput = dialogView.findViewById(R.id.activity_save_dialog_footer_file_name_input);
                CharSequence rawActivitySaveDialogFooterFileNameInputContent = activitySaveDialogFooterFileNameInput.getText();
                String activitySaveDialogFooterFileNameInputContent = rawActivitySaveDialogFooterFileNameInputContent.toString();
                String currentPath = getApplicationContext().getCacheDir().getPath();
                Log.d("debug", "currentPath: " + currentPath);
                String filePath = currentPath + "/" + activitySaveDialogFooterFileNameInputContent;
                File newFile = new File(filePath);
                CharSequence rawActivityMainContainerBodyLayoutInputContent = savedContent;
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                try {
                    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
                    writer.println(activityMainContainerBodyLayoutInputContent);
                    writer.close();
                    Log.d("debug", "savedialog debugMsg: " + activityMainContainerBodyLayoutInputContent);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SaveAsFileActivity.this);
                    builder.setTitle("Изменения сохранены");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(SaveAsFileActivity.this, MainActivity.class);
                            SaveAsFileActivity.this.startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (IOException e) {
                    Log.d("debug", "ошибка создания файла");
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        LinearLayout activitySaveDialogBody = dialogView.findViewById(R.id.activity_save_dialog_body);
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onShow(DialogInterface dialogInterface) {
                EditText activitySaveDialogFooterFileNameInput = dialogView.findViewById(R.id.activity_save_dialog_footer_file_name_input);
                showSaveFiles(activitySaveDialogBody, activitySaveDialogFooterFileNameInput);
            }
        });
        alert.show();
    }

    public void getExtras() {
        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        boolean isExtrasExists = extras != null;
        if (isExtrasExists) {
            savedContent = extras.getString("savedContent");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showSaveFiles(LinearLayout fileManager, EditText field) {
        String currentPath = getApplicationContext().getCacheDir().getPath();
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for(File fileInDir : listOfFiles){
                Log.d("debug", "manager: " + fileInDir.getPath());
                LinearLayout file = new LinearLayout(SaveAsFileActivity.this);
                file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                ImageView fileImg = new ImageView(SaveAsFileActivity.this);
                fileImg.setImageResource(R.drawable.folder);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(SaveAsFileActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                TextView fileName = new TextView(SaveAsFileActivity.this);
                String rawFileName = fileInDir.getName();
                fileName.setText(rawFileName);
                TextView fileMetaData = new TextView(SaveAsFileActivity.this);
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
                fileManager.addView(file);
                file.setContentDescription(fileInDir.getPath());
                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String filePath = view.getContentDescription().toString();
                        Log.d("debug", "filePath: " + filePath);
                        File file = new File(filePath);
                        field.setText(file.getName());
                    }
                });
            }
        } catch (InterruptedException e) {
            Log.d("debug", "ошибка в FileTask");
        } catch (ExecutionException e) {
            Log.d("debug", "ошибка в FileTask");
        }
    }

}