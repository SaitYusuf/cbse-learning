package yusufsait.com.cbselearning;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {
    QuizCursorAdapter quizAdapter;
    String chapter_part_id;
    MyDataBase chemistryDatabase;
    SQLiteDatabase chemSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        chapter_part_id = getIntent().getStringExtra("chapterPartId");


        chemistryDatabase = new MyDataBase(this);
        chemSQLiteDatabase = chemistryDatabase.getWritableDatabase();

        Cursor quizQuestionsCursor = chemSQLiteDatabase.rawQuery("SELECT * FROM Chemistry_Questions WHERE chapter_part_id = ?", new String[]{chapter_part_id});
        ListView quizQuestionsListView = findViewById(R.id.quiz_question_list);

        quizAdapter = new QuizCursorAdapter(this,quizQuestionsCursor);
        try {
            quizQuestionsListView.setAdapter(quizAdapter);
        }
        catch (IllegalArgumentException e){
            CardView comingSoonTextView = findViewById(R.id.coming_soon);
            comingSoonTextView.setVisibility(View.VISIBLE);
            findViewById(R.id.frame_layout).setVisibility(View.GONE);
        }
    }
    public void onClickSubmitButton(View view){
        int score = 0;
        ListView quizQuestionList = findViewById(R.id.quiz_question_list);
        int numberOfQuestions = quizQuestionList.getCount();

        for(int i = 0; i < numberOfQuestions; i++) {
            ConstraintLayout quizQuestionCard = (ConstraintLayout) quizQuestionList.getAdapter().getView(i,null,quizQuestionList);
            TextView idTextView = quizQuestionCard.findViewById(R.id.id);
            String id = idTextView.getText().toString();

            HashMap quizAnswers = quizAdapter.getQuizAnswers();
            String selectedAnswer = (String) quizAnswers.get(id);
            if(selectedAnswer == null){
                String selectAnswer = "Please complete all the questions";
                Toast toast = Toast.makeText(this,selectAnswer,Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            String rightAnswer = ((TextView) quizQuestionCard.findViewById(R.id.right_answer)).getText().toString();
            if(selectedAnswer.equals(rightAnswer)){
                score++;
            }
        }
        Intent returnScoreIntent = new Intent();
        returnScoreIntent.putExtra("score",score);
        returnScoreIntent.putExtra("numberOfQuestions", numberOfQuestions);
        returnScoreIntent.putExtra("chapter_part_id",chapter_part_id);
        setResult(AppCompatActivity.RESULT_OK,returnScoreIntent);
        finish();
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
    public void onClickSuggestButton(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        String[] strings = new String[]{"cbselearningfeedback@gmail.com"};
        String string = chapter_part_id + "_question";
        intent.putExtra(Intent.EXTRA_EMAIL, strings);
        intent.putExtra(Intent.EXTRA_SUBJECT, string);
        intent.putExtra(Intent.EXTRA_TEXT,"Enter question and answer here:\n");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
