package softtrack.apps.texteditor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public Menu myMenu;
    public TextInputEditText activityMainContainerBodyLayoutInput;
    public int lastCursorPosition = 0;
    public ClipboardManager clipboard;
    public NavigationView activityMainContainerAside;
    public DrawerLayout activityMainDrawerLayout;
    public NavigationView activityMainContainerArticle;
    public int visible;
    public int unvisible;
    public LinearLayout activityMainContainerAsideContentExit;
    public LinearLayout activityMainContainerAsideContentShare;
    public LinearLayout activityMainContainerAsideContentHideAds;
    public ImageButton activityMainContainerFooterSearch;
    public ImageButton activityMainContainerFooterUndo;
    public ImageButton activityMainContainerFooterRedo;
    public int disabledBtnColor;
    public int enabledBtnColor;
    public ArrayList<String> historyRecords;
    public int historyRecordsCursor = 0;
    public LinearLayout activityMainContainerAsideContentMemoryManager;
    public ImageButton activityMainContainerFooterSave;
    public TextView fileNameLabel;
    public LinearLayout activityMainContainerAsideContentInternalStorage;
    public LinearLayout activityMainContainerAsideContentHelp;
    public LinearLayout activityMainContainerAsideContentSettings;
    public boolean isChangesDetected = false;
    public ImageButton activityMainContainerFooterClose;
    public LinearLayout activityMainContainerArticleScrollBody;
    public ImageButton activityMainContainerFooterOpen;
    public boolean isSelectionMode = false;
    public LinearLayout activityMainContainerBodyContainerAside;
    public HorizontalScrollView activityMainContainerHeader;
    public TabLayout activityMainContainerHeaderBodyTabs;
//    public ArrayList<String> documents;
    public ArrayList<HashMap<String, Object>> documents;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInfalter = getMenuInflater();
        menuInfalter.inflate(R.menu.activity_main_menu, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
//        boolean isBurgerMenuItem = itemId == R.id.activity_main_menu_burger_btn;
        boolean isFolderMenuItemCreateBtn = itemId == R.id.activity_main_menu_folder_btn_menu_create_btn;
        boolean isFolderMenuItemOpenBtn = itemId == R.id.activity_main_menu_folder_btn_menu_open_btn;
        boolean isFolderMenuItemOpenSAFBtn = itemId == R.id.activity_main_menu_folder_btn_menu_open_saf_btn;
        boolean isPenMenuItemUndoBtn = itemId == R.id.activity_main_menu_pen_btn_menu_insert_btn;
        boolean isPenMenuItemRedoBtn = itemId == R.id.activity_main_menu_pen_btn_menu_insert_btn;
        boolean isPenMenuItemSelectAllBtn = itemId == R.id.activity_main_menu_pen_btn_menu_select_all_btn;
        boolean isPenMenuItemInsertBtn = itemId == R.id.activity_main_menu_pen_btn_menu_insert_btn;
        boolean isPenMenuItemInsertColorBtn = itemId == R.id.activity_main_menu_pen_btn_menu_insert_color_btn;
        boolean isPenMenuItemInsertTimeStampBtn = itemId == R.id.activity_main_menu_pen_btn_menu_insert_time_stamp_btn;
        boolean isPenMenuItemIncreaseBtn = itemId == R.id.activity_main_menu_pen_btn_menu_increase_indent_btn;
        boolean isPenMenuItemDecreaseBtn = itemId == R.id.activity_main_menu_pen_btn_menu_decrease_indent_btn;
        boolean isMoreMenuItemFindBtn = itemId == R.id.activity_main_menu_more_btn_menu_find_btn;
        boolean isMoreMenuItemSendBtn = itemId == R.id.activity_main_menu_more_btn_menu_send_btn;
        boolean isMoreMenuItemGoToStrokeBtn = itemId == R.id.activity_main_menu_more_btn_menu_goto_stroke_btn;
        boolean isMoreMenuItemStatisticsBtn = itemId == R.id.activity_main_menu_more_btn_menu_statistics_btn;
        boolean isMoreMenuItemPrintBtn = itemId == R.id.activity_main_menu_more_btn_menu_print_btn;
        if (isFolderMenuItemCreateBtn) {
            activityMainContainerBodyLayoutInput.setText("");
            fileNameLabel.setText("Безымянный");
//            documents.add("Безымянный");
            HashMap<String, Object> document = new HashMap<String, Object>();
            document.put("name", "Безымянный");
            document.put("text", "");
            document.put("isChangesDetected", false);
            documents.add(document);
            TabLayout.Tab newDocument = activityMainContainerHeaderBodyTabs.newTab();
            newDocument.setText("Безымянный");
            activityMainContainerHeaderBodyTabs.addTab(newDocument);
            activityMainContainerHeaderBodyTabs.selectTab(newDocument);
            if (documents.size() >= 2) {
                activityMainContainerHeader.setVisibility(visible);
            }
        } else if (isFolderMenuItemOpenBtn) {
            Intent intent = new Intent(MainActivity.this, OpenFileActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (isFolderMenuItemOpenSAFBtn) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            startActivity(intent);
        } else if (isPenMenuItemUndoBtn) {

        } else if (isPenMenuItemRedoBtn) {

        } else if (isPenMenuItemSelectAllBtn) {
            activityMainContainerBodyLayoutInput.selectAll();
        } else if (isPenMenuItemInsertBtn) {
            // код при клике на пункт меню
            if (clipboard.hasPrimaryClip()) {
                ClipData clipData = clipboard.getPrimaryClip();
                boolean isItemsExists = clipData.getItemCount() >= 1;
                if (isItemsExists) {
                    ClipData.Item clipDataItem = clipData.getItemAt(0);
                    CharSequence rawClipDataItemContent = clipDataItem.getText();
                    String clipDataItemContent = rawClipDataItemContent.toString();
                    Log.d("debug", "clipDataItemContent: " + clipDataItemContent);
                    int clipDataItemContentLength = clipDataItemContent.length();
                    CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                    String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                    String beforeInsertText = activityMainContainerBodyLayoutInputContent.substring(0, lastCursorPosition);
                    int activityMainContainerBodyLayoutInputContentLength = activityMainContainerBodyLayoutInputContent.length();
                    int activityMainContainerBodyLayoutInputContentLastIndex = activityMainContainerBodyLayoutInputContentLength - 1;
                    String afterInsertText = activityMainContainerBodyLayoutInputContent.substring(lastCursorPosition, activityMainContainerBodyLayoutInputContentLastIndex);
                    String updatedContent = beforeInsertText + clipDataItemContent + afterInsertText;
                    activityMainContainerBodyLayoutInput.setText(updatedContent);
                }
            }
        } else if (isPenMenuItemInsertColorBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            /*LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_go_to_stroke_dialog, null);
            builder.setView(dialogView);*/
            builder.setCancelable(true);
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (isPenMenuItemInsertTimeStampBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            /*LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_go_to_stroke_dialog, null);
            builder.setView(dialogView);*/
            builder.setCancelable(true);
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Вставить временную метку");
            alert.show();
        } else if (isPenMenuItemIncreaseBtn) {
            CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
            String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
            activityMainContainerBodyLayoutInput.setText("\t" + activityMainContainerBodyLayoutInputContent);
        } else if (isPenMenuItemDecreaseBtn) {
            CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
            String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
            int findIndex = activityMainContainerBodyLayoutInputContent.indexOf("\t");
            boolean isFound = findIndex != - 1;
            if (isFound) {
                activityMainContainerBodyLayoutInput.setText(activityMainContainerBodyLayoutInputContent.replaceFirst("\t", ""));
            }
        } else if (isMoreMenuItemFindBtn) {
            openFindDialog();
        } else if (isMoreMenuItemSendBtn) {

        } else if (isMoreMenuItemGoToStrokeBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_go_to_stroke_dialog, null);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activityMainContainerBodyLayoutInput.setSelection(0);
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Перейти к строке");
            alert.show();
            TextView activityGoToStrokeDialogContainerLabel = dialogView.findViewById(R.id.activity_go_to_stroke_dialog_container_label);
            int lastLineNumber = activityMainContainerBodyLayoutInput.getLineCount();
            String rawLastLineNumber = String.valueOf(lastLineNumber);
            activityGoToStrokeDialogContainerLabel.setText("Введите номер строки (1 .. " + lastLineNumber + ")");
        } else if (isMoreMenuItemStatisticsBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_statistics_dialog, null);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Статистика");
            alert.show();
            CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
            String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
            String[] words = activityMainContainerBodyLayoutInputContent.split(" ");
            int countWords = words.length;
            String rawCountWords = String.valueOf(countWords);
            int countChars = rawActivityMainContainerBodyLayoutInputContent.length();
            String rawCountChars = String.valueOf(countChars);
            TextView activityStatisticsDialogContainerCountWordsValue = dialogView.findViewById(R.id.activity_statistics_dialog_container_count_words_value);
            TextView activityStatisticsDialogContainerCountCharsValue = dialogView.findViewById(R.id.activity_statistics_dialog_container_count_chars_value);
            activityStatisticsDialogContainerCountWordsValue.setText(rawCountWords);
            activityStatisticsDialogContainerCountCharsValue.setText(rawCountChars);
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initialize() {
        visible = View.VISIBLE;
        unvisible = View.GONE;
        disabledBtnColor = Color.rgb(200, 200, 200);
        enabledBtnColor = Color.rgb(0, 0, 0);
        historyRecords = new ArrayList<String>();
        historyRecords.add("");
        activityMainContainerBodyLayoutInput = findViewById(R.id.activity_main_container_body_Layout_input);
        activityMainContainerAside = findViewById(R.id.activity_main_container_aside);
        activityMainDrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        activityMainContainerArticle = findViewById(R.id.activity_main_container_article);
        activityMainContainerAsideContentExit = findViewById(R.id.activity_main_container_aside_content_exit);
        activityMainContainerAsideContentShare = findViewById(R.id.activity_main_container_aside_content_share);
        activityMainContainerAsideContentHideAds = findViewById(R.id.activity_main_container_aside_content_hide_ads);
        activityMainContainerFooterSearch = findViewById(R.id.activity_main_container_footer_search);
        activityMainContainerFooterUndo = findViewById(R.id.activity_main_container_footer_undo);
        activityMainContainerFooterRedo = findViewById(R.id.activity_main_container_footer_redo);
        activityMainContainerAsideContentMemoryManager = findViewById(R.id.activity_main_container_aside_content_memory_manager);
        activityMainContainerFooterSave = findViewById(R.id.activity_main_container_footer_save);
        activityMainContainerAsideContentInternalStorage = findViewById(R.id.activity_main_container_aside_content_internal_storage);
        activityMainContainerAsideContentHelp = findViewById(R.id.activity_main_container_aside_content_help);
        activityMainContainerAsideContentSettings = findViewById(R.id.activity_main_container_aside_content_settings);
        activityMainContainerFooterClose = findViewById(R.id.activity_main_container_footer_close);
        activityMainContainerArticleScrollBody = findViewById(R.id.activity_main_container_article_scroll_body);
        activityMainContainerFooterOpen = findViewById(R.id.activity_main_container_footer_open);
        activityMainContainerBodyContainerAside = findViewById(R.id.activity_main_container_body_container_aside);
        activityMainContainerHeader = findViewById(R.id.activity_main_container_aside_container_header);
        activityMainContainerHeaderBodyTabs = findViewById(R.id.activity_main_container_aside_container_header_body_tabs);
//        documents = new ArrayList<String>();
        documents = new ArrayList<HashMap<String, Object>>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LinearLayout customView = new LinearLayout(MainActivity.this);
        customView.setOrientation(LinearLayout.HORIZONTAL);
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        TextView customViewLabel = new TextView(MainActivity.this);
        customViewLabel.setText("Безымянный");
        fileNameLabel = customViewLabel;
        ImageView customViewImg = new ImageView(MainActivity.this);
        customViewImg.setImageResource(R.drawable.burger);
        customViewImg.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams customViewImgLayoutParams = new LinearLayout.LayoutParams(50, 50);
        customViewImgLayoutParams.setMargins(25, 0, 0 , 0);
        customViewImg.setLayoutParams(customViewImgLayoutParams);
        customViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gravityAside = Gravity.LEFT;
                boolean isAsideOpen = activityMainDrawerLayout.isDrawerOpen(gravityAside);
                if (isAsideOpen) {
                    activityMainDrawerLayout.closeDrawers();
                } else {
                    activityMainDrawerLayout.openDrawer(gravityAside);
                }
            }
        });
        customView.addView(customViewLabel);
        customView.addView(customViewImg);
        actionBar.setCustomView(customView);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

//        String firstDocumentName = "Безымянный";
        String firstDocumentName = "Безымянный";
        String firstDocumentText = "";

        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        if (extras != null) {
            String filePath = extras.getString("filePath");
            BufferedReader reader = null;
            try {
                String readedLine = new String(Files.readAllBytes(Paths.get(filePath)));
                Log.d("debug", "openialog debugMsg: " + readedLine + ", " + filePath);
                activityMainContainerBodyLayoutInput.setText(readedLine);
                File openedFile = new File(filePath);
                fileNameLabel.setText(openedFile.getName());

                firstDocumentName = openedFile.getName();
                firstDocumentText = readedLine;

            } catch (FileNotFoundException e) {
                Log.d("debug", "ошибка при открытии файла");
            } catch (IOException e) {
                Log.d("debug", "ошибка при открытии файла");
            }
        }

//        documents.add(firstDocumentName);
        HashMap<String, Object> firstDocument = new HashMap<String, Object>();
        firstDocument.put("name", firstDocumentName);
        firstDocument.put("text", firstDocumentText);
        firstDocument.put("isChangesDetected", false);
        documents.add(firstDocument);

        activityMainContainerBodyLayoutInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastCursorPosition = activityMainContainerBodyLayoutInput.getSelectionStart();
            }
        });
        activityMainContainerBodyLayoutInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("debug", "onTextChanged: charSequense: " + charSequence.toString() + ", i: " + String.valueOf(i) + ", i1: " + String.valueOf(i1) + ", i2: " + String.valueOf(i2));
                lastCursorPosition = i;
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                historyRecords.add(activityMainContainerBodyLayoutInputContent);
                activityMainContainerFooterUndo.setColorFilter(enabledBtnColor);
                activityMainContainerFooterUndo.setEnabled(true);
                activityMainContainerFooterRedo.setColorFilter(disabledBtnColor);
                activityMainContainerFooterRedo.setEnabled(false);

                historyRecordsCursor++;

                int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
                HashMap<String, Object> document = documents.get(selectedPosition);
                boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesSelected")));

//                if (!isChangesDetected) {
                if (!isLocalChangesDetected) {
                    isChangesDetected = true;
                    fileNameLabel.setText("* " + fileNameLabel.getText().toString());
                    activityMainContainerFooterSave.setColorFilter(enabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(true);

                    document.put("isChangesDetected", true);

                }

                int countChars = rawActivityMainContainerBodyLayoutInputContent.length();
//                int countLines = countChars / 34 + 1;
                activityMainContainerBodyContainerAside.removeAllViews();
                int countLines = activityMainContainerBodyLayoutInput.getLineCount();
                for (int lineNumberIndex = 0; lineNumberIndex < countLines; lineNumberIndex++) {
                    TextView lineNumberLabel = new TextView(MainActivity.this);
                    String lineNumberLabelContent = String.valueOf(lineNumberIndex + 1);
                    lineNumberLabel.setText(lineNumberLabelContent);
                    LinearLayout.LayoutParams lineNumberLabelLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 33);
                    if (lineNumberIndex == 0) {
                        lineNumberLabelLayoutParams.setMargins(0, 35, 0, 0);
                    } else {
                        lineNumberLabelLayoutParams.setMargins(0, 5, 0, 0);
                    }
                    lineNumberLabel.setLayoutParams(lineNumberLabelLayoutParams);
                    activityMainContainerBodyContainerAside.addView(lineNumberLabel);
                }

                document.put("text", activityMainContainerBodyLayoutInputContent);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        activityMainContainerAsideContentExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
                    finishAffinity();
                } else if(Build.VERSION.SDK_INT>=21){
                    finishAndRemoveTask();
                }
            }
        });
        activityMainContainerAsideContentShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });
        activityMainContainerAsideContentHideAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "market://details?id=com.google.android.youtube";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        activityMainContainerFooterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFindDialog();
            }
        });
        activityMainContainerFooterUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (historyRecords.size() >= 2) {
                    historyRecordsCursor--;
                    activityMainContainerBodyLayoutInput.setText(historyRecords.get(historyRecordsCursor));
                    activityMainContainerFooterRedo.setColorFilter(enabledBtnColor);
                    activityMainContainerFooterRedo.setEnabled(true);
                    if (historyRecords.size() <= 1) {
                        activityMainContainerFooterUndo.setColorFilter(disabledBtnColor);
                        activityMainContainerFooterUndo.setEnabled(false);
                    }
                }
            }
        });
        activityMainContainerFooterRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                historyRecordsCursor++;
                historyRecords.add(activityMainContainerBodyLayoutInputContent);
                activityMainContainerBodyLayoutInput.setText(historyRecords.get(historyRecordsCursor));
                activityMainContainerFooterUndo.setColorFilter(enabledBtnColor);
                activityMainContainerFooterUndo.setEnabled(true);
                activityMainContainerFooterRedo.setColorFilter(disabledBtnColor);
                activityMainContainerFooterRedo.setEnabled(false);
            }
        });
        activityMainContainerAsideContentMemoryManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MemoryManagerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        activityMainContainerFooterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
                HashMap<String, Object> document = documents.get(selectedPosition);
                boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesSelected")));

                if (isLocalChangesDetected) {
//                if (isChangesDetected) {
                    openSaveDialog();
                }
            }
        });
        activityMainContainerAsideContentInternalStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenFileActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        activityMainContainerAsideContentHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        activityMainContainerAsideContentSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        activityMainContainerFooterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFile();
            }
        });
        activityMainDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                int drawerViewId = drawerView.getId();
                boolean isArticle = drawerViewId == R.id.activity_main_container_article;
                if (isArticle) {
                    showFiles(activityMainContainerArticleScrollBody);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                super.onDrawerOpened(drawerView);
                int drawerViewId = drawerView.getId();
                boolean isArticle = drawerViewId == R.id.activity_main_container_article;
                if (isArticle) {
                    activityMainContainerArticleScrollBody.removeAllViews();
                }
            }
        });
        activityMainContainerFooterOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenFileActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        activityMainContainerHeaderBodyTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
                HashMap<String, Object> document = documents.get(selectedPosition);
                activityMainContainerBodyLayoutInput.setText(String.valueOf(document.get("text")));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void openFindDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_find_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setPositiveButton("Найти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText activityFindDialogContainerFindField = dialogView.findViewById(R.id.activity_find_dialog_container_find_field);
                CharSequence  rawActivityFindDialogContainerFindFieldContent = activityFindDialogContainerFindField.getText();
                String activityFindDialogContainerFindFieldContent = rawActivityFindDialogContainerFindFieldContent.toString();
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                int findIndex = activityMainContainerBodyLayoutInputContent.indexOf(activityFindDialogContainerFindFieldContent);
                boolean isFound = findIndex != - 1;
                if (isFound) {
                    /*String a = activityMainContainerBodyLayoutInputContent.substring(findIndex, activityMainContainerBodyLayoutInputContent.length() - 1);
                    int endFindIndex = a.indexOf(" ");
                    if (endFindIndex != - 1) {
                        activityMainContainerBodyLayoutInput.setSelection(findIndex, findIndex + endFindIndex);
                    } else {
                        activityMainContainerBodyLayoutInput.setSelection(findIndex, findIndex + 1);
                    }*/
                    activityMainContainerBodyLayoutInput.setSelection(findIndex, findIndex + activityFindDialogContainerFindFieldContent.length());
                } else {
                    String toastMessage = "Ничего не найдено";
                    Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        builder.setNeutralButton("Заменить Все", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Заменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*ScrollView weightSelectorActivityRealPartScroll = dialogView.findViewById(R.id.weight_selector_activity_real_part_scroll);
                ScrollView weightSelectorActivityImaginaryPartScroll = dialogView.findViewById(R.id.weight_selector_activity_imaginary_part_scroll);
                int destinationBetweenScrollItems = 105;
                int realScrollY = Integer.valueOf(settedGrowth) * destinationBetweenScrollItems;
                int imaginaryScrollY = Integer.valueOf(settedWeight) * destinationBetweenScrollItems;
                weightSelectorActivityRealPartScroll.scrollTo(0, realScrollY);
                weightSelectorActivityImaginaryPartScroll.scrollTo(0, imaginaryScrollY);*/
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Найти");
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showFiles(LinearLayout fileManager) {
        String currentPath = getApplicationContext().getCacheDir().getPath();
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for(File fileInDir : listOfFiles){
                Log.d("debug", "manager: " + fileInDir.getPath());
                LinearLayout file = new LinearLayout(MainActivity.this);
                file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                ImageView fileImg = new ImageView(MainActivity.this);
                fileImg.setImageResource(R.drawable.folder);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(MainActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                TextView fileName = new TextView(MainActivity.this);
                String rawFileName = fileInDir.getName();
                fileName.setText(rawFileName);
                TextView fileMetaData = new TextView(MainActivity.this);
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
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("filePath", filePath);
                        MainActivity.this.startActivity(intent);
                    }
                });
            }
        } catch (InterruptedException e) {
            Log.d("debug", "ошибка в FileTask");
        } catch (ExecutionException e) {
            Log.d("debug", "ошибка в FileTask");
        }
    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void openSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                try {
                    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
                    writer.println(activityMainContainerBodyLayoutInputContent);
                    writer.close();
                    Log.d("debug", "savedialog debugMsg: " + activityMainContainerBodyLayoutInputContent);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Изменения сохранены");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    fileNameLabel.setText(activitySaveDialogFooterFileNameInputContent);
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

    public void closeFile() {

        int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
        HashMap<String, Object> document = documents.get(selectedPosition);
        boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesSelected")));

//        if (isChangesDetected) {
        if (isLocalChangesDetected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Вы хотите сохранить изменения?");
            builder.setCancelable(true);
            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activityMainContainerBodyLayoutInput.setText("");
                    fileNameLabel.setText("Безымянный");
                    activityMainContainerFooterSave.setColorFilter(disabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(false);
                    isChangesDetected = false;

                    document.put("isChangesDetected", true);

                }
            });
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    openSaveDialogWithExit();
                }
            });
            builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Сохранить");
            alert.show();
        } else {
            activityMainContainerBodyLayoutInput.setText("");
            fileNameLabel.setText("Безымянный");
            activityMainContainerFooterSave.setColorFilter(disabledBtnColor);
            activityMainContainerFooterSave.setEnabled(false);
            isChangesDetected = false;

            if (activityMainContainerHeaderBodyTabs.getTabCount() >= 2) {
                activityMainContainerHeaderBodyTabs.removeTabAt(selectedPosition);
                documents.remove(selectedPosition);
                if (activityMainContainerHeaderBodyTabs.getTabCount() == 1) {
                    activityMainContainerHeader.setVisibility(unvisible);
                }
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showSaveFiles(LinearLayout fileManager, EditText field) {
        String currentPath = getApplicationContext().getCacheDir().getPath();
        try {
            File[] listOfFiles = new FileTask().execute(currentPath).get();
            for(File fileInDir : listOfFiles){
                Log.d("debug", "manager: " + fileInDir.getPath());
                LinearLayout file = new LinearLayout(MainActivity.this);
                file.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                ImageView fileImg = new ImageView(MainActivity.this);
                fileImg.setImageResource(R.drawable.folder);
                fileImg.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                fileImg.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout fileInfo = new LinearLayout(MainActivity.this);
                fileInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams fileInfoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                fileInfoLayoutParams.setMargins(35, 0, 0, 0);
                fileInfo.setLayoutParams(fileInfoLayoutParams);
                TextView fileName = new TextView(MainActivity.this);
                String rawFileName = fileInDir.getName();
                fileName.setText(rawFileName);
                TextView fileMetaData = new TextView(MainActivity.this);
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

    public void openSaveDialogWithExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_save_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText activitySaveDialogFooterFileNameInput = dialogView.findViewById(R.id.activity_save_dialog_footer_file_name_input);
                CharSequence rawActivitySaveDialogFooterFileNameInputContent = activitySaveDialogFooterFileNameInput.getText();
                String activitySaveDialogFooterFileNameInputContent = rawActivitySaveDialogFooterFileNameInputContent.toString();
                String currentPath = getApplicationContext().getCacheDir().getPath();
                Log.d("debug", "currentPath: " + currentPath);
                String filePath = currentPath + "/" + activitySaveDialogFooterFileNameInputContent;
                File newFile = new File(filePath);
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                try {
                    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
                    writer.println(activityMainContainerBodyLayoutInputContent);
                    writer.close();
                    Log.d("debug", "savedialog debugMsg: " + activityMainContainerBodyLayoutInputContent);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Изменения сохранены");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Готово", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    fileNameLabel.setText(activitySaveDialogFooterFileNameInputContent);

                    activityMainContainerBodyLayoutInput.setText("");
                    fileNameLabel.setText("Безымянный");
                    activityMainContainerFooterSave.setColorFilter(disabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(false);
                    isChangesDetected = false;

                    int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
                    if (activityMainContainerHeaderBodyTabs.getTabCount() >= 2) {
                        activityMainContainerHeaderBodyTabs.removeTabAt(selectedPosition);
                        documents.remove(selectedPosition);
                        if (activityMainContainerHeaderBodyTabs.getTabCount() == 1) {
                            activityMainContainerHeader.setVisibility(unvisible);
                            documents.forEach((document) -> {
                                if (document != null) {
                                    String documentName = String.valueOf(document.get("name"));
                                    String documentText = String.valueOf(document.get("text"));
                                    fileNameLabel.setText(documentName);
                                    activityMainContainerBodyLayoutInput.setText(documentText);
                                }
                            });
                        }
                    }

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

}