package yusufsait.com.cbselearning;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yarolegovich.discretescrollview.DiscreteScrollView;

public class ChemistryChaptersActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemistry_chapters);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Chemistry Chapters");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_request:
                                Intent subRequestIntent = new Intent(ChemistryChaptersActivity.this,SubjectPollActivity.class);
                                startActivity(subRequestIntent);
                                break;
                            case R.id.nav_about:
                                Intent aboutIntent = new Intent(ChemistryChaptersActivity.this, AboutActivity.class);
                                startActivity(aboutIntent);
                                break;
                            case R.id.nav_rate:
                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                // To count with Play market backstack, After pressing back button,
                                // to taken back to our application, we need to add following flags to intent.
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                                break;
                            case R.id.nav_feedback:
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                String[] strings = new String[]{"yusufsaitappfeedback@gmail.com"};
                                intent.putExtra(Intent.EXTRA_EMAIL, strings);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                                break;
                            case R.id.nav_more:
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/dev?id=5782570041305794849")));
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        MyDataBase chemistryDatabase = new MyDataBase(this);
        SQLiteDatabase chemSQLiteDatabase = chemistryDatabase.getReadableDatabase();
        Cursor chapterCursor = chemSQLiteDatabase.rawQuery("SELECT * FROM Chemistry_chapters",null);

        DiscreteScrollView chapterListView = findViewById(R.id.chapter_list);
        chapterListView.setSlideOnFling(true);
        chapterListView.setSlideOnFlingThreshold(2500);
        chapterListView.setNestedScrollingEnabled(false);

        ChapterRecyclerAdapter chapterListAdapter = new ChapterRecyclerAdapter(this,chapterCursor);
        chapterListView.setAdapter(chapterListAdapter);
        chapterListView.setItemTransitionTimeMillis(60);
    }
    public void onClick(View view) {
        Intent chapterPartsIntent = new Intent(this, ChapterPartsActivity.class);
        TextView chapterIdTextView = view.findViewById(R.id.chapter_id);
        TextView chapterNameTextView = view.findViewById(R.id.chapter_name);
        String chapterId = (String) chapterIdTextView.getText();
        String chapterName = (String) chapterNameTextView.getText();

        chapterPartsIntent.putExtra("chapterId",chapterId);
        chapterPartsIntent.putExtra("chapterName",chapterName);
        startActivityForResult(chapterPartsIntent,1);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
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
