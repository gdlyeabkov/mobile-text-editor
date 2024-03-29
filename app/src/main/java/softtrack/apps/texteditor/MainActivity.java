package softtrack.apps.texteditor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.w3c.dom.Text;

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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
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
    public ArrayList<HashMap<String, Object>> documents;
    public String timeStamp = "";
    public HashMap<Integer, String> monthLabels;
    public LinearLayout activityMainContainerAsideContentRecentFiles;
    public LinearLayout activityMainContainerAsideContentBookmarks;
    public LinearLayout activityMainContainerFooter;
    public LinearLayout activityMainContainerBodyLayout;
    @SuppressLint("WrongConstant") public SQLiteDatabase db;

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
        boolean isFolderMenuItemCreateBtn = itemId == R.id.activity_main_menu_folder_btn_menu_create_btn;
        boolean isFolderMenuItemOpenBtn = itemId == R.id.activity_main_menu_folder_btn_menu_open_btn;
        boolean isFolderMenuItemOpenSAFBtn = itemId == R.id.activity_main_menu_folder_btn_menu_open_saf_btn;
        boolean isFolderMenuItemSaveBtn = itemId == R.id.activity_main_menu_folder_btn_menu_save_btn;
        boolean isFolderMenuItemSaveAsBtn = itemId == R.id.activity_main_menu_folder_btn_menu_saveas_btn;
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
        boolean isMoreMenuItemEncodingBtn = itemId == R.id.activity_main_menu_more_btn_menu_encoding_btn;
        boolean isMoreMenuItemStyleBtn = itemId == R.id.activity_main_menu_more_btn_menu_style_btn;
        boolean isMoreMenuItemStatisticsBtn = itemId == R.id.activity_main_menu_more_btn_menu_statistics_btn;
        boolean isMoreMenuItemPrintBtn = itemId == R.id.activity_main_menu_more_btn_menu_print_btn;
        boolean isMoreMenuItemToolBarBtn = itemId == R.id.activity_main_menu_more_btn_menu_tool_bar_btn;
        boolean isMoreMenuItemReadOnlyBtn = itemId == R.id.activity_main_menu_more_btn_menu_readonly_btn;
        if (isFolderMenuItemCreateBtn) {
            fileNameLabel.setText("Безымянный.txt");
            HashMap<String, Object> document = new HashMap<String, Object>();
            document.put("name", "Безымянный.txt");
            document.put("text", "");
            document.put("isChangesDetected", false);
            documents.add(document);
            TabLayout.Tab newDocument = activityMainContainerHeaderBodyTabs.newTab();
            newDocument.setText("Безымянный.txt");
            activityMainContainerHeaderBodyTabs.addTab(newDocument);
            activityMainContainerHeaderBodyTabs.selectTab(newDocument);
            int countDocuments = documents.size();
            boolean isMoreDocuments = countDocuments >= 2;
            if (isMoreDocuments) {
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
        } else if (isFolderMenuItemSaveBtn) {
            openSaveDialogIfNeed();
        } else if (isFolderMenuItemSaveAsBtn) {
            CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
            String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
            Intent intent = new Intent(MainActivity.this, SaveAsFileActivity.class);
            intent.putExtra("savedContent", activityMainContainerBodyLayoutInputContent);
            MainActivity.this.startActivity(intent);
        } else if (isPenMenuItemUndoBtn) {

        } else if (isPenMenuItemRedoBtn) {

        } else if (isPenMenuItemSelectAllBtn) {
            activityMainContainerBodyLayoutInput.selectAll();
        } else if (isPenMenuItemInsertBtn) {
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
            new ColorPickerDialog.Builder(this)
                    .setPreferenceName("MyColorPickerDialog")
                    .setPositiveButton("ОК",
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                String hexCode = envelope.getHexCode();
                                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                                String beforeInsertText = activityMainContainerBodyLayoutInputContent.substring(0, lastCursorPosition);
                                int activityMainContainerBodyLayoutInputContentLength = activityMainContainerBodyLayoutInputContent.length();
                                int activityMainContainerBodyLayoutInputContentLastIndex = activityMainContainerBodyLayoutInputContentLength - 1;
                                String updatedContent = activityMainContainerBodyLayoutInputContent + hexCode;
                                activityMainContainerBodyLayoutInput.setText(updatedContent);

                            }
                        })
                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                    .attachAlphaSlideBar(true) // the default value is true.
                    .attachBrightnessSlideBar(true)  // the default value is true.
                    .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                    .show();

        } else if (isPenMenuItemInsertTimeStampBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_insert_time_stamp, null);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                    String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                    String updatedContent = activityMainContainerBodyLayoutInputContent + timeStamp;
                    activityMainContainerBodyLayoutInput.setText(updatedContent);
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Вставить временную метку");
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Calendar calendar = Calendar.getInstance();
                    boolean isAddPrefix = false;
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    String rawDayOfMonth = String.valueOf(dayOfMonth);
                    int rawDayOfMonthLength = rawDayOfMonth.length();
                    isAddPrefix = rawDayOfMonthLength == 1;
                    if (isAddPrefix) {
                        rawDayOfMonth = "0" + rawDayOfMonth;
                    }
                    int month = calendar.get(Calendar.MONTH);
                    String rawMonth = String.valueOf(month);
                    int rawMonthLength = rawMonth.length();
                    isAddPrefix = rawMonthLength == 1;
                    if (isAddPrefix) {
                        rawMonth = "0" + rawMonth;
                    }
                    int year = calendar.get(Calendar.YEAR);
                    String rawYear = String.valueOf(year);
                    int hours = calendar.get(Calendar.HOUR_OF_DAY);
                    String rawHours = String.valueOf(hours);
                    int rawHoursLength = rawHours.length();
                    isAddPrefix = rawHoursLength == 1;
                    if (isAddPrefix) {
                        rawHours = "0" + rawHours;
                    }
                    int minutes = calendar.get(Calendar.MINUTE);
                    String rawMinutes = String.valueOf(minutes);
                    int rawMinutesLength = rawMinutes.length();
                    isAddPrefix = rawMinutesLength == 1;
                    if (isAddPrefix) {
                        rawMinutes = "0" + rawMinutes;
                    }
                    int seconds = calendar.get(Calendar.SECOND);
                    String rawSeconds = String.valueOf(seconds);
                    int rawSecondsLength = rawSeconds.length();
                    isAddPrefix = rawSecondsLength == 1;
                    if (isAddPrefix) {
                        rawSeconds = "0" + rawSeconds;
                    }
                    int amPm = calendar.get(Calendar.AM_PM);
                    String amPmLabel = "";
                    boolean isAm = amPm == Calendar.AM;
                    if (isAm) {
                        amPmLabel = "AM";
                    } else {
                        amPmLabel = "PM";
                    }
                    TimeZone tz = TimeZone.getTimeZone("Europe/Moscow");
                    int tzOffset = tz.getOffset(new Date().getTime()) / 1000 / 60;
                    int tzOffsetHours = tzOffset / 60;
                    String rawTzOffsetHours = String.valueOf(tzOffsetHours);
                    int rawTzOffsetHoursLength = rawTzOffsetHours.length();
                    isAddPrefix = rawTzOffsetHoursLength == 1;
                    if (isAddPrefix) {
                        rawTzOffsetHours = "0" + rawTzOffsetHours;
                    }
                    int tzOffsetMinutes = 60 / tzOffset % 60;
                    String rawTzOffsetMinutes = String.valueOf(tzOffsetMinutes);
                    int rawTzOffsetMinutesLength = rawTzOffsetMinutes.length();
                    isAddPrefix = rawTzOffsetMinutesLength == 1;
                    if (isAddPrefix) {
                        rawTzOffsetMinutes = "0" + rawTzOffsetMinutes;
                    }
                    String rawTzOffset = rawTzOffsetHours + ":" + rawTzOffsetMinutes;
                    Log.d("debug", "rawTzOffset: " + rawTzOffset);
                    timeStamp = rawYear + "/" + rawMonth + "/" + rawDayOfMonth + " " + rawHours + ":" + rawMinutes;
                    RadioButton activityTimeStampContainerBodyFirstLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_first_label);
                    activityTimeStampContainerBodyFirstLabel.setText(timeStamp);
                    timeStamp = rawYear + "/" + rawMonth + "/" + rawDayOfMonth + " " + rawHours + ":" + rawMinutes + " " + amPmLabel;
                    RadioButton activityTimeStampContainerBodySecondLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_second_label);
                    activityTimeStampContainerBodySecondLabel.setText(timeStamp);
                    timeStamp = rawYear + "/" + rawMonth + "/" + rawDayOfMonth + " " + rawHours + ":" + rawMinutes + ":" + rawSeconds + " GMT" + rawTzOffset;
                    RadioButton activityTimeStampContainerBodyThirdLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_third_label);
                    activityTimeStampContainerBodyThirdLabel.setText(timeStamp);
                    timeStamp = rawMonth + "/" + rawDayOfMonth + "/" + rawYear + " " + rawHours + ":" + rawMinutes;
                    RadioButton activityTimeStampContainerBodyFourthLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_fourth_label);
                    activityTimeStampContainerBodyFourthLabel.setText(timeStamp);
                    timeStamp = rawMonth + "/" + rawDayOfMonth + "/" + rawYear + " " + rawHours + ":" + rawMinutes + " " + amPmLabel;
                    RadioButton activityTimeStampContainerBodyFifthLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_fifth_label);
                    activityTimeStampContainerBodyFifthLabel.setText(timeStamp);
                    timeStamp = rawMonth + "/" + rawDayOfMonth + "/" + rawYear + " " + rawHours + ":" + rawMinutes + ":" + rawSeconds + " GMT" + rawTzOffset;
                    RadioButton activityTimeStampContainerBodySixthLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_sixth_label);
                    activityTimeStampContainerBodySixthLabel.setText(timeStamp);
                    timeStamp = monthLabels.get(month) + " " + rawDayOfMonth + ", " + rawYear + " " + rawHours + ":" + rawMinutes;
                    RadioButton activityTimeStampContainerBodySeventhLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_seventh_label);
                    activityTimeStampContainerBodySeventhLabel.setText(timeStamp);
                    timeStamp = monthLabels.get(month) + " " + rawDayOfMonth + ", " + rawYear + " " + rawHours + ":" + rawMinutes + " " + amPmLabel;
                    RadioButton activityTimeStampContainerBodyEigthLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_eighth_label);
                    activityTimeStampContainerBodyEigthLabel.setText(timeStamp);
                    timeStamp = monthLabels.get(month) + " " + rawDayOfMonth + ", " + rawYear + " " + rawHours + ":" + rawMinutes + ":" + rawSeconds + " GMT" + rawTzOffset;
                    RadioButton activityTimeStampContainerBodyNinethLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_nineth_label);
                    activityTimeStampContainerBodyNinethLabel.setText(timeStamp);
                    timeStamp = rawDayOfMonth + "/" + monthLabels.get(month) + "/" + rawYear + " " + rawHours + ":" + rawMinutes;
                    RadioButton activityTimeStampContainerBodyTenthLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_tenth_label);
                    activityTimeStampContainerBodyTenthLabel.setText(timeStamp);
                    timeStamp = rawDayOfMonth + "/" + monthLabels.get(month) + "/" + rawYear + " " + rawHours + ":" + rawMinutes + " " + amPmLabel;
                    RadioButton activityTimeStampContainerBodyEleventhLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_eleventh_label);
                    activityTimeStampContainerBodyEleventhLabel.setText(timeStamp);
                    timeStamp = rawDayOfMonth + "/" + monthLabels.get(month) + "/" + rawYear + " " + rawHours + ":" + rawMinutes + ":" + rawSeconds + " GMT" + rawTzOffset;
                    RadioButton activityTimeStampContainerBodyTwelthLabel = dialogView.findViewById(R.id.activity_time_stamp_container_body_twelth_label);
                    activityTimeStampContainerBodyTwelthLabel.setText(timeStamp);
                }
            });
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
            openShareDialog();
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
        } else if (isMoreMenuItemEncodingBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_encoding_dialog, null);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    RadioGroup activityEncodingDialogContainerBodyGroup = dialogView.findViewById(R.id.activity_encoding_dialog_container_body_group);
                    int activityEncodingDialogContainerBodyGroupSelectedItemId = activityEncodingDialogContainerBodyGroup.getCheckedRadioButtonId();
                    RadioButton activityEncodingDialogContainerBodyGroupSelectedItem = dialogView.findViewById(activityEncodingDialogContainerBodyGroupSelectedItemId);
                    CharSequence rawActivityStyleDialogContainerBodyGroupSelectedItemContent = activityEncodingDialogContainerBodyGroupSelectedItem.getText();
                    String activityStyleDialogContainerBodyGroupSelectedItemContent = rawActivityStyleDialogContainerBodyGroupSelectedItemContent.toString();
                    boolean isUtf8Encoding = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("UTF-8");
                    boolean isUtf16Encoding = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("UTF-16");
                    boolean isUtf16BeEncoding = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("UTF-16BE");
                    CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                    String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                    byte[] bytes = activityMainContainerBodyLayoutInputContent.getBytes(StandardCharsets.UTF_8);
                    String encodedString = "";
                    if (isUtf8Encoding) {
                        encodedString = new String(bytes, StandardCharsets.UTF_8);
                    } else if (isUtf16Encoding) {
                        encodedString = new String(bytes, StandardCharsets.UTF_16);
                    } else if (isUtf16BeEncoding) {
                        encodedString = new String(bytes, StandardCharsets.UTF_16BE);
                    }
                    activityMainContainerBodyLayoutInput.setText(encodedString);
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Кодировка");
            alert.show();
        } else if (isMoreMenuItemStyleBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_style_dialog, null);
            builder.setView(dialogView);
            builder.setCancelable(true);
            builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    RadioGroup activityStyleDialogContainerBodyGroup = dialogView.findViewById(R.id.activity_style_dialog_container_body_group);
                    int activityStyleDialogContainerBodyGroupSelectedItemId = activityStyleDialogContainerBodyGroup.getCheckedRadioButtonId();
                    RadioButton activityStyleDialogContainerBodyGroupSelectedItem = dialogView.findViewById(activityStyleDialogContainerBodyGroupSelectedItemId);
                    CharSequence rawActivityStyleDialogContainerBodyGroupSelectedItemContent = activityStyleDialogContainerBodyGroupSelectedItem.getText();
                    String activityStyleDialogContainerBodyGroupSelectedItemContent = rawActivityStyleDialogContainerBodyGroupSelectedItemContent.toString();
                    boolean isDefaultStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Default");
                    boolean isGitHubStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("GitHub");
                    boolean isGitHubV2Style = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("GitHub v2");
                    boolean isTommorowStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Tommorow");
                    boolean isHemisuStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Hemisu");
                    boolean isAtelierCaveStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Cave");
                    boolean isAtelierDuneStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Dune");
                    boolean isAtelierEstuaryStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Estuary");
                    boolean isAtelierForestStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Forest");
                    boolean isAtelierHeathStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Heath");
                    boolean isAtelierLakesideStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Lakeside");
                    boolean isAtelierPlateauStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Plateau");
                    boolean isAtelierSavannaStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Savanna");
                    boolean isAtelierSeasideStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Seaside");
                    boolean isAtelierSulphurpoolStyle = activityStyleDialogContainerBodyGroupSelectedItemContent.equalsIgnoreCase("Atelier Sulphurpool");
                    if (isDefaultStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(255, 255, 255));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(255, 255, 255));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(0, 0, 0));
                    } else if (isGitHubStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(250, 250, 250));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(250, 250, 250));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(5, 5, 5));
                    } else if (isGitHubV2Style) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(245, 245, 245));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(245, 245, 245));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(10, 10, 10));
                    } else if (isTommorowStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(240, 240, 240));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(240, 240, 240));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(15, 15, 15));
                    } else if (isHemisuStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(235, 235, 235));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(235, 235, 235));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(20, 20, 20));
                    } else if (isAtelierCaveStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(230, 230, 230));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(230, 230, 230));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(25, 25, 25));
                    } else if (isAtelierDuneStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(225, 225, 225));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(225, 225, 225));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(30, 30, 30));
                    } else if (isAtelierEstuaryStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(220, 220, 220));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(220, 220, 220));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(35, 35, 35));
                    } else if (isAtelierForestStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(215, 215, 215));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(215, 215, 215));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(40, 40, 40));
                    } else if (isAtelierHeathStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(210, 210, 210));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(210, 210, 210));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(45, 45, 45));
                    } else if (isAtelierLakesideStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(205, 205, 205));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(205, 205, 205));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(50, 50, 50));
                    } else if (isAtelierPlateauStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(200, 200, 200));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(200, 200, 200));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(55, 55, 55));
                    } else if (isAtelierSavannaStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(195, 195, 195));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(195, 195, 195));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(60, 60, 60));
                    } else if (isAtelierSeasideStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(190, 190, 190));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(190, 190, 190));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(65, 65, 65));
                    } else if (isAtelierSulphurpoolStyle) {
                        activityMainContainerBodyLayout.setBackgroundColor(Color.rgb(185, 185, 185));
                        activityMainContainerBodyLayoutInput.setBackgroundColor(Color.rgb(185, 185, 185));
                        activityMainContainerBodyLayoutInput.setTextColor(Color.rgb(60, 60, 60));
                    }
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Стиль оформления");
            alert.show();
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
        } else if (isMoreMenuItemPrintBtn) {

        } else if (isMoreMenuItemToolBarBtn) {
            int activityMainContainerFootervisibility = activityMainContainerFooter.getVisibility();
            boolean isActivityMainContainerFootervisibile = activityMainContainerFootervisibility == visible;
            if (isActivityMainContainerFootervisibile) {
                activityMainContainerFooter.setVisibility(unvisible);
                myMenu.findItem(R.id.activity_main_menu_more_btn_menu_tool_bar_btn).setChecked(false);
            } else {
                activityMainContainerFooter.setVisibility(visible);
                myMenu.findItem(R.id.activity_main_menu_more_btn_menu_tool_bar_btn).setChecked(true);
            }
        } else if (isMoreMenuItemReadOnlyBtn) {
            boolean isNotReadOnly = activityMainContainerBodyLayoutInput.isEnabled();
            if (isNotReadOnly) {
                activityMainContainerBodyLayoutInput.setEnabled(false);
                myMenu.findItem(R.id.activity_main_menu_more_btn_menu_readonly_btn).setChecked(false);
            } else {
                activityMainContainerBodyLayoutInput.setEnabled(true);
                myMenu.findItem(R.id.activity_main_menu_more_btn_menu_readonly_btn).setChecked(true);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initialize() {
        initDB();
        monthLabels = new HashMap<Integer, String>();
        monthLabels.put(1, "янв.");
        monthLabels.put(2, "фев.");
        monthLabels.put(3, "мар.");
        monthLabels.put(4, "апр.");
        monthLabels.put(5, "мая");
        monthLabels.put(6, "июн.");
        monthLabels.put(7, "июл.");
        monthLabels.put(8, "авг.");
        monthLabels.put(9, "сен.");
        monthLabels.put(10, "окт.");
        monthLabels.put(11, "ноя.");
        monthLabels.put(12, "дек.");
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
        activityMainContainerAsideContentRecentFiles = findViewById(R.id.activity_main_container_aside_content_recent_files);
        activityMainContainerAsideContentBookmarks = findViewById(R.id.activity_main_container_aside_content_bookmarks);
        activityMainContainerFooter = findViewById(R.id.activity_main_container_footer);
        activityMainContainerBodyLayout = findViewById(R.id.activity_main_container_body_layout);

        documents = new ArrayList<HashMap<String, Object>>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        LinearLayout customView = new LinearLayout(MainActivity.this);
        customView.setOrientation(LinearLayout.HORIZONTAL);
        customView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        TextView customViewLabel = new TextView(MainActivity.this);
        customViewLabel.setText("Безымянный.txt");
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

        String firstDocumentName = "Безымянный.txt";
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

                Log.d("debug", "undo and redo: " + String.valueOf(historyRecords.size()));
                historyRecords.add(activityMainContainerBodyLayoutInputContent);
                historyRecordsCursor++;

                int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
                HashMap<String, Object> document = documents.get(selectedPosition);
                boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));
                boolean isNotLocalChanges = !isLocalChangesDetected;
                int activityMainContainerBodyLayoutInputContentLength = activityMainContainerBodyLayoutInputContent.length();
                boolean isActivityMainContainerBodyLayoutInputContentExists = activityMainContainerBodyLayoutInputContentLength >= 1;
                boolean isSetLocalChanges = isNotLocalChanges && isActivityMainContainerBodyLayoutInputContentExists;
                if (isSetLocalChanges) {
                    fileNameLabel.setText("* " + fileNameLabel.getText().toString());
                    activityMainContainerFooterSave.setColorFilter(enabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(true);
                    document.put("isChangesDetected", true);
                }

                int countChars = rawActivityMainContainerBodyLayoutInputContent.length();
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
                closeApp();
            }
        });
        activityMainContainerAsideContentShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShareDialog();
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
                openSaveDialogIfNeed();
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
                String documentText = String.valueOf(document.get("text"));
                activityMainContainerBodyLayoutInput.setText(documentText);
                String documentName = String.valueOf(document.get("name"));
                fileNameLabel.setText(documentName);
                boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));
                if (isLocalChangesDetected) {
                    activityMainContainerFooterSave.setColorFilter(enabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(true);
                } else {
                    activityMainContainerFooterSave.setColorFilter(disabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        activityMainContainerAsideContentRecentFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecentFilesActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        activityMainContainerAsideContentBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BookmarksActivity.class);
                MainActivity.this.startActivity(intent);
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
                EditText activityFindDialogContainerFindField = dialogView.findViewById(R.id.activity_find_dialog_container_find_field);
                CharSequence  rawActivityFindDialogContainerFindFieldContent = activityFindDialogContainerFindField.getText();
                String activityFindDialogContainerFindFieldContent = rawActivityFindDialogContainerFindFieldContent.toString();
                EditText activityFindDialogContainerReplaceField = dialogView.findViewById(R.id.activity_find_dialog_container_replace_field);
                CharSequence  rawActivityFindDialogContainerReplaceFieldContent = activityFindDialogContainerReplaceField.getText();
                String activityFindDialogContainerReplaceFieldContent = rawActivityFindDialogContainerReplaceFieldContent.toString();
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                String replacedContent = activityMainContainerBodyLayoutInputContent.replaceAll(activityFindDialogContainerFindFieldContent, activityFindDialogContainerReplaceFieldContent);
                activityMainContainerBodyLayoutInput.setText(replacedContent);
            }
        });
        builder.setNegativeButton("Заменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText activityFindDialogContainerFindField = dialogView.findViewById(R.id.activity_find_dialog_container_find_field);
                CharSequence  rawActivityFindDialogContainerFindFieldContent = activityFindDialogContainerFindField.getText();
                String activityFindDialogContainerFindFieldContent = rawActivityFindDialogContainerFindFieldContent.toString();
                EditText activityFindDialogContainerReplaceField = dialogView.findViewById(R.id.activity_find_dialog_container_replace_field);
                CharSequence  rawActivityFindDialogContainerReplaceFieldContent = activityFindDialogContainerReplaceField.getText();
                String activityFindDialogContainerReplaceFieldContent = rawActivityFindDialogContainerReplaceFieldContent.toString();
                CharSequence rawActivityMainContainerBodyLayoutInputContent = activityMainContainerBodyLayoutInput.getText();
                String activityMainContainerBodyLayoutInputContent = rawActivityMainContainerBodyLayoutInputContent.toString();
                String replacedContent = activityMainContainerBodyLayoutInputContent.replaceFirst(activityFindDialogContainerFindFieldContent, activityFindDialogContainerReplaceFieldContent);
                activityMainContainerBodyLayoutInput.setText(replacedContent);
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

                    int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
                    HashMap<String, Object> document = documents.get(selectedPosition);
                    document.put("isChangesDetected", false);
                    document.put("name", activitySaveDialogFooterFileNameInputContent);
                    activityMainContainerHeaderBodyTabs.getTabAt(selectedPosition).setText(activitySaveDialogFooterFileNameInputContent);

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
        boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));

        String documentName = String.valueOf(document.get("name"));

        if (isLocalChangesDetected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Вы хотите сохранить изменения?");
            builder.setCancelable(true);
            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activityMainContainerBodyLayoutInput.setText("");
                    fileNameLabel.setText("Безымянный.txt");
                    activityMainContainerFooterSave.setColorFilter(disabledBtnColor);
                    activityMainContainerFooterSave.setEnabled(false);
                    isChangesDetected = false;

                    document.put("isChangesDetected", true);

                }
            });
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    openSaveDialogWithExit(documentName);
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
            fileNameLabel.setText("Безымянный.txt");
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

    public void openSaveDialogWithExit(String documentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_save_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        EditText activitySaveDialogFooterFileNameInput = dialogView.findViewById(R.id.activity_save_dialog_footer_file_name_input);

        int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();

        activitySaveDialogFooterFileNameInput.setText(documentName);

        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                    fileNameLabel.setText("Безымянный.txt");
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

    @Override
    public void onBackPressed() {
        boolean isSomeChangesDetected = false;
        for (HashMap<String, Object> document : documents) {
            boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));
            if (isLocalChangesDetected) {
                isSomeChangesDetected = true;
                break;
            }
            Log.d("debug", "isLocalChangesDetected: " + String.valueOf(document.get("isChangesDetected")));
        }
        Log.d("debug", "documents: " + String.valueOf(documents.size()) + ", isSomeChangesDetected: " + Boolean.toString(isSomeChangesDetected));
        if (isSomeChangesDetected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_exit_dialog, null);
            builder.setView(dialogView);
            builder.setCancelable(false);
            builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (HashMap<String, Object> document : documents) {
                        boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));
                        if (isLocalChangesDetected) {
                            String name = String.valueOf(document.get("name"));
                            openSaveDialogWithExit(name);
                        }
                    }
                }
            });
            builder.setNegativeButton("Выход", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNeutralButton("Позже", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.super.onBackPressed();
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Выход");
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    LinearLayout activityExitDialogContainerBody = dialogView.findViewById(R.id.activity_exit_dialog_container_body);
                    for (HashMap<String, Object> document : documents) {
                        boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));
                        if (isLocalChangesDetected) {
                            LinearLayout activityExitDialogContainerBodyItem = new LinearLayout(MainActivity.this);
                            ImageView activityExitDialogContainerBodyItemIcon = new ImageView(MainActivity.this);
                            activityExitDialogContainerBodyItemIcon.setImageResource(R.drawable.document);
                            LinearLayout.LayoutParams activityExitDialogContainerBodyItemIconLayoutParams = new LinearLayout.LayoutParams(50, 50);
                            activityExitDialogContainerBodyItemIconLayoutParams.setMargins(25, 5, 25, 5);
                            activityExitDialogContainerBodyItemIcon.setLayoutParams(activityExitDialogContainerBodyItemIconLayoutParams);
                            TextView activityExitDialogContainerBodyItemName = new TextView(MainActivity.this);
                            String name = String.valueOf(document.get("name"));
                            activityExitDialogContainerBodyItemName.setText(name);
                            activityExitDialogContainerBodyItem.addView(activityExitDialogContainerBodyItemIcon);
                            activityExitDialogContainerBodyItem.addView(activityExitDialogContainerBodyItemName);
                            activityExitDialogContainerBody.addView(activityExitDialogContainerBodyItem);
                        }
                    }
                }
            });
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    public void closeApp() {
        if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
            finishAffinity();
        } else if(Build.VERSION.SDK_INT>=21){
            finishAndRemoveTask();
        }
    }

    @SuppressLint("WrongConstant")
    public void initDB() {
        db = openOrCreateDatabase("text-editor-database.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bookmarks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, path TEXT);");
    }

    public void openShareDialog() {
        Intent share = new Intent(Intent.ACTION_SEND);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void openSaveDialogIfNeed() {
        int selectedPosition = activityMainContainerHeaderBodyTabs.getSelectedTabPosition();
        HashMap<String, Object> document = documents.get(selectedPosition);
        boolean isLocalChangesDetected = Boolean.valueOf(String.valueOf(document.get("isChangesDetected")));
        if (isLocalChangesDetected) {
            openSaveDialog();
        }
    }

}