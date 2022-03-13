package softtrack.apps.texteditor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SettingsActivity extends AppCompatActivity {

    public LinearLayout activitySettingsContainerBodyLang;
    public LinearLayout activitySettingsContainerBodyEncoding;
    public LinearLayout activitySettingsContainerBodyBreakLine;
    public LinearLayout activitySettingsContainerBodyParagraphChar;
    public LinearLayout activitySettingsContainerBodyNotifications;
    public LinearLayout activitySettingsContainerBodyFontFamily;
    public LinearLayout activitySettingsContainerBodyFontSize;
    public SeekBar activitySettingsContainerBodyFontSizeSlider;
    public LinearLayout activitySettingsContainerBodyLineHeight;
    public SeekBar activitySettingsContainerBodyLineHeightSlider;
    public Switch activitySettingsContainerBodyAutoSaveSwitch;
    public LinearLayout activitySettingsContainerBodyAutoSaveInterval;
    public LinearLayout activitySettingsContainerBodyTheme;
    public LinearLayout activitySettingsContainerBodyFullScreen;
    public Switch activitySettingsContainerBodyFullScreenSwitch;
    public LinearLayout activitySettingsContainerBodyWatchNews;
    public LinearLayout activitySettingsContainerBodyFeedback;
    public LinearLayout activitySettingsContainerBodyRemoveAds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();

    }

    public void initialize() {
        findViews();
        addHandlers();
        initializeActionBar();
    }

    public void findViews() {
        activitySettingsContainerBodyLang = findViewById(R.id.activity_settings_container_body_language);
        activitySettingsContainerBodyEncoding = findViewById(R.id.activity_settings_container_body_encoding);
        activitySettingsContainerBodyBreakLine = findViewById(R.id.activity_settings_container_body_break_line);
        activitySettingsContainerBodyParagraphChar = findViewById(R.id.activity_settings_container_body_paragraph_char);
        activitySettingsContainerBodyNotifications = findViewById(R.id.activity_settings_container_body_notifications);
        activitySettingsContainerBodyFontFamily = findViewById(R.id.activity_settings_container_body_font_family);
        activitySettingsContainerBodyFontSize = findViewById(R.id.activity_settings_container_body_font_size);
        // activitySettingsContainerBodyFontSizeSlider = findViewById(R.id.activity_settings_font_size_dialog_container_slider);
        activitySettingsContainerBodyLineHeight = findViewById(R.id.activity_settings_container_body_line_height);
        // activitySettingsContainerBodyLineHeightSlider = findViewById(R.id.activity_settings_container_body_line_height_);
        activitySettingsContainerBodyAutoSaveSwitch = findViewById(R.id.activity_settings_container_body_auto_save_switch);
        activitySettingsContainerBodyAutoSaveInterval = findViewById(R.id.activity_settings_container_body_auto_save_interval);
        activitySettingsContainerBodyTheme = findViewById(R.id.activity_settings_container_body_theme);
        activitySettingsContainerBodyFullScreen = findViewById(R.id.activity_settings_container_body_full_screen);
        activitySettingsContainerBodyFullScreenSwitch = findViewById(R.id.activity_settings_container_body_full_screen_switch);
        activitySettingsContainerBodyWatchNews = findViewById(R.id.activity_settings_container_body_watch_news);
        activitySettingsContainerBodyFeedback = findViewById(R.id.activity_settings_container_body_feedback);
        activitySettingsContainerBodyRemoveAds = findViewById(R.id.activity_settings_container_body_remove_ads);
    }

    public void addHandlers() {
        activitySettingsContainerBodyLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_lang_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Язык");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyEncoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_encoding_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Кодировка по умолчанию");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyBreakLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_break_line_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Разрыв строки");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyParagraphChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_paragraph_char_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Символ абзаца");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_notifications_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Показывать подсказки");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyFontFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_font_family_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Тип шрифта");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_font_size_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Размер шрифта: 18");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        activitySettingsContainerBodyFontSizeSlider = dialogView.findViewById(R.id.activity_settings_font_size_dialog_container_slider);
                        activitySettingsContainerBodyFontSizeSlider.setMin(8);
                        activitySettingsContainerBodyFontSizeSlider.setMax(56);
                        activitySettingsContainerBodyFontSizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                alert.setTitle("Размер шрифта: " + String.valueOf(i));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyLineHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_line_height_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Интервал между строками");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        activitySettingsContainerBodyLineHeightSlider = dialogView.findViewById(R.id.activity_settings_line_height_dialog_container_slider);
                        activitySettingsContainerBodyLineHeightSlider.setMin(0);
                        activitySettingsContainerBodyLineHeightSlider.setMax(6);
                        activitySettingsContainerBodyLineHeightSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                alert.setTitle("Интервал между строками: " + String.valueOf(i));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyAutoSaveInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAutoSaveChecked = activitySettingsContainerBodyAutoSaveSwitch.isChecked();
                if (isAutoSaveChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.activity_settings_auto_save_interval_dialog, null);
                    builder.setView(dialogView);
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
                    alert.setTitle("Интервал автосохранения");
                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {

                        }
                    });
                    alert.show();
                }
            }
        });
        activitySettingsContainerBodyTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_settings_theme_dialog, null);
                builder.setView(dialogView);
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
                alert.setTitle("Тема");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSwitchChecked = activitySettingsContainerBodyFullScreenSwitch.isChecked();
                boolean toggledValue = !isSwitchChecked;
                activitySettingsContainerBodyFullScreenSwitch.setChecked(toggledValue);
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Этот параметр будет применен\\nпосле перезапуска приложения.");
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
                alert.setTitle("Тема");
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }
                });
                alert.show();
            }
        });
        activitySettingsContainerBodyWatchNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.facebook.com/QuickEditTextEditor";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        activitySettingsContainerBodyFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://forum.xda-developers.com/t/app-4-0-3-quickedit-text-editor.2899385/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        activitySettingsContainerBodyRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "market://details?id=com.google.android.youtube";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Настройки");
    };

}
