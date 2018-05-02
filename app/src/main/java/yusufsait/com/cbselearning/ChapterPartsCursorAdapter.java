package yusufsait.com.cbselearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Yusuf on 14/3/18.
 */

public class ChapterPartsCursorAdapter extends CursorAdapter {

    public ChapterPartsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.chapter_part_card, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView chapterPartNameTextView = view.findViewById(R.id.chapter_part_name);
        TextView chapterPartIdTextView = view.findViewById(R.id.chapter_part_Id);
        TextView quizScoreTextView = view.findViewById(R.id.score);
        TextView numberTextView = view.findViewById(R.id.number);

        //Extract properties from cursor
        String chapterPartName = cursor.getString(cursor.getColumnIndexOrThrow("chapter_part_name"));
        String chapterPartId = cursor.getString(cursor.getColumnIndexOrThrow("chapter_part_id"));
        String number = chapterPartId.substring(3,5) + ".";

        SharedPreferences prefs = context.getSharedPreferences("chapterPartScores",Context.MODE_PRIVATE);
        String numberOfQuestionsKey = chapterPartId + "_number_of_questions";
        String scoreKey = chapterPartId + "_score";
        int quizScore = prefs.getInt(scoreKey,0);
        int numberOfQuestions = prefs.getInt(numberOfQuestionsKey,-1);
        String score = "";
        if(numberOfQuestions != -1){
            score = quizScore + "/" + numberOfQuestions;
            LinearLayout layout = view.findViewById(R.id.score_view);
            layout.setVisibility(View.VISIBLE);
        }
        SharedPreferences.Editor editor = context.getSharedPreferences("completedQuiz",Context.MODE_PRIVATE).edit();
        if(quizScore == numberOfQuestions){
            editor.putString(chapterPartId,"1");
        }
        else{
            editor.putString(chapterPartId,"0");
        }
        editor.apply();

        // Populate fields with extracted properties
        chapterPartNameTextView.setText(chapterPartName);
        chapterPartIdTextView.setText(chapterPartId);
        quizScoreTextView.setText(score);
        numberTextView.setText(number);

    }
    @Override

    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
