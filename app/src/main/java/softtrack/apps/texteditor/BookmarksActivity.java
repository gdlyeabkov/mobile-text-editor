package softtrack.apps.texteditor;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BookmarksActivity extends AppCompatActivity {

    public LinearLayout activityBookmarksContainerBody;
    @SuppressLint("WrongConstant") public SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        initialize();

    }

    public void initialize() {
        findViews();
        initializeActionBar();
        initDB();
        showBookmarks();
    }

    public void findViews() {
        activityBookmarksContainerBody = findViewById(R.id.activity_bookmarks_container_body);
    }

    @SuppressLint("WrongConstant")
    public void initDB() {
        db = openOrCreateDatabase("text-editor-database.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
    }

    public void showBookmarks() {
        Cursor bookmarksCursor = db.rawQuery("Select * from bookmarks", null);
        bookmarksCursor.moveToFirst();
        long bookmarksCount = DatabaseUtils.queryNumEntries(db, "bookmarks");
        boolean isHaveBookmarks = bookmarksCount >= 1;
        if (isHaveBookmarks) {
            for (int i = 0; i < bookmarksCount; i++) {
                String name = bookmarksCursor.getString(1);
                String path = bookmarksCursor.getString(2);
                LinearLayout bookmark = new LinearLayout(BookmarksActivity.this);
                bookmark.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams bookmarkLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
                bookmarkLayoutParams.setMargins(25, 5, 0, 5);
                bookmark.setLayoutParams(bookmarkLayoutParams);
                ImageView bookmarkIcon = new ImageView(BookmarksActivity.this);
                bookmarkIcon.setImageResource(R.drawable.star);
                LinearLayout.LayoutParams bookmarkIconLayoutParams = new LinearLayout.LayoutParams(75, 75);
                bookmarkIconLayoutParams.setMargins(0, 35, 15, 35);
                bookmarkIcon.setLayoutParams(bookmarkIconLayoutParams);
                LinearLayout bookmarkAside = new LinearLayout(BookmarksActivity.this);
                bookmarkAside.setOrientation(LinearLayout.VERTICAL);
                TextView bookmarkName = new TextView(BookmarksActivity.this);
                bookmarkName.setText(name);
                TextView bookmarkPath = new TextView(BookmarksActivity.this);
                bookmarkPath.setText(path);
                bookmarkAside.addView(bookmarkName);
                bookmarkAside.addView(bookmarkPath);
                bookmark.addView(bookmarkIcon);
                bookmark.addView(bookmarkAside);
                activityBookmarksContainerBody.addView(bookmark);
                bookmarksCursor.moveToNext();
            }
        } else {
            TextView notFoundBookmarksLabel = new TextView(BookmarksActivity.this);
            notFoundBookmarksLabel.setText("Не найдено закладок");
            activityBookmarksContainerBody.addView(notFoundBookmarksLabel);
        }
    }

    public void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Закладки");
    };

}
