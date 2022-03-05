package softtrack.apps.texteditor;

import androidx.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        boolean isPenMenuItemInsertBtn = itemId == R.id.activity_main_menu_pen_btn_menu_insert_btn;
        /*
        if (isBurgerMenuItem) {
            int gravityAside = Gravity.LEFT;
            boolean isAsideOpen = activityMainDrawerLayout.isDrawerOpen(gravityAside);
            if (isAsideOpen) {
                activityMainDrawerLayout.closeDrawers();
            } else {
                activityMainDrawerLayout.openDrawer(gravityAside);
            }
        } else
        */
        if (isPenMenuItemInsertBtn) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {
        visible = View.VISIBLE;
        unvisible = View.GONE;
        disabledBtnColor = Color.rgb(200, 200, 200);
        enabledBtnColor = Color.rgb(0, 0, 0);
        historyRecords = new ArrayList<String>();
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LinearLayout customView = new LinearLayout(MainActivity.this);
        customView.setOrientation(LinearLayout.HORIZONTAL);
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        TextView customViewLabel = new TextView(MainActivity.this);
        customViewLabel.setText("Безымянный");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_find_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.setPositiveButton("Найти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
        });
        activityMainContainerFooterUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (historyRecords.size() >= 1) {
                    historyRecordsCursor--;
                    activityMainContainerBodyLayoutInput.setText(historyRecords.get(historyRecordsCursor));
                    if (historyRecords.size() <= 0) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_save_dialog, null);
                builder.setView(dialogView);
                builder.setCancelable(true);
                builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
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
                String currentPath = getApplicationContext().getCacheDir().getPath();
                Log.d("debug", "currentPath: " + currentPath);
                String filePath = currentPath + "/unnamed.txt";
                File newFile = new File(filePath);
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    Log.d("debug", "ошибка создания файла");
                }
            }
        });
    }

}