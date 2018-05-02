package yusufsait.com.cbselearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ChapterPartsActivity extends AppCompatActivity {
    MyDataBase chemistryDatabase;
    SQLiteDatabase chemSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_parts);

        String chapter_id = getIntent().getStringExtra("chapterId");
        String chapter_name = getIntent().getStringExtra("chapterName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(chapter_name);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        chemistryDatabase = new MyDataBase(this);
        chemSQLiteDatabase = chemistryDatabase.getReadableDatabase();

        Cursor chapterPartsCursor = chemSQLiteDatabase.rawQuery("SELECT * FROM Chapter_parts WHERE chapter_id = ?", new String[]{chapter_id});
        ListView chapterPartsListView = findViewById(R.id.chapter_parts_list);

        ChapterPartsCursorAdapter chapterPartsListAdapter = new ChapterPartsCursorAdapter(this,chapterPartsCursor);
        chapterPartsListView.setAdapter(chapterPartsListAdapter);

        int numberOfChapterParts = chapterPartsListView.getChildCount();
        SharedPreferences.Editor editor = getSharedPreferences("numberOfChapterParts",MODE_PRIVATE).edit();
        editor.putInt(chapter_id,numberOfChapterParts);
        editor.apply();
    }
    public void onClickChapterPart(View view) {
        Intent BookViewerIntent = new Intent(this, WebViewActivity.class);
        TextView chapterPartIdTextView = view.findViewById(R.id.chapter_part_Id);
        String chapterPartId = (String) chapterPartIdTextView.getText();
        BookViewerIntent.putExtra("chapterPartId",chapterPartId);
        startActivityForResult(BookViewerIntent,1);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_CANCELED) {
            recreate();
        }
    }
}
