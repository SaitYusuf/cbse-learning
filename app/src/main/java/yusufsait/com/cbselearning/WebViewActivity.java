package yusufsait.com.cbselearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    String chapterPartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);

        chapterPartId = getIntent().getStringExtra("chapterPartId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Read");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        String fileLocation = "file:///android_asset/all/Text/" + chapterPartId + ".html";

        WebView bookView = findViewById(R.id.bookview);
        bookView.loadUrl(fileLocation);
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
    public void onClickQuizButton(View view){
        Intent QuizIntent = new Intent(this, QuizActivity.class);
        QuizIntent.putExtra("chapterPartId",chapterPartId);
        startActivityForResult(QuizIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == AppCompatActivity.RESULT_OK){
                int score = data.getIntExtra("score",0);
                int numberOfQuestions = data.getIntExtra("numberOfQuestions",-1);
                String chapterPartId = data.getStringExtra("chapter_part_id");
                SharedPreferences.Editor editor = getSharedPreferences("chapterPartScores", MODE_PRIVATE).edit();

                String numberOfQuestionsKey = chapterPartId + "_number_of_questions";
                String scoreKey = chapterPartId + "_score";
                editor.putInt(numberOfQuestionsKey,numberOfQuestions);
                editor.putInt(scoreKey,score);
                editor.apply();

                String emote = "";
                if(score/numberOfQuestions == 1){
                    emote = "!";
                }
                String resultMessage = "You got " + score + "/" + numberOfQuestions + " answers right" + emote;
                Toast toast = Toast.makeText(this,resultMessage,Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
