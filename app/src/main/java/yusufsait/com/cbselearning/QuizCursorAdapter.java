package yusufsait.com.cbselearning;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class QuizCursorAdapter extends CursorAdapter {
    HashMap<String, String> quizSelectedAnswers = new HashMap<>();
    String selected;

    public QuizCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.quiz_question, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView quizQuestionTextView = view.findViewById(R.id.quiz_question);
        TextView rightAnswerTextView = view.findViewById(R.id.right_answer);
        final TextView idTextView = view.findViewById(R.id.id);
        RadioGroup radioGroup = view.findViewById(R.id.quiz_answers);
        RadioButton answer_01_button = view.findViewById(R.id.answer_01);
        RadioButton answer_02_button = view.findViewById(R.id.answer_02);
        RadioButton answer_03_button = view.findViewById(R.id.answer_03);
        RadioButton answer_04_button = view.findViewById(R.id.answer_04);

        String quizQuestion = cursor.getString(cursor.getColumnIndexOrThrow("question"));
        String rightAnswer = cursor.getString(cursor.getColumnIndexOrThrow("right"));
        String question_id = cursor.getString(cursor.getColumnIndexOrThrow("question_id"));

        quizQuestionTextView.setText(quizQuestion);
        rightAnswerTextView.setText(rightAnswer);
        idTextView.setText(question_id);

        ArrayList<String> answerArray = new ArrayList<String>();

        answerArray.add(cursor.getString(cursor.getColumnIndexOrThrow("wrong1")));
        answerArray.add(cursor.getString(cursor.getColumnIndexOrThrow("wrong2")));
        answerArray.add(cursor.getString(cursor.getColumnIndexOrThrow("wrong3")));
        answerArray.add(cursor.getString(cursor.getColumnIndexOrThrow("right")));

        Collections.shuffle(answerArray);

        answer_01_button.setText(answerArray.get(0));
        answer_02_button.setText(answerArray.get(1));
        answer_03_button.setText(answerArray.get(2));
        answer_04_button.setText(answerArray.get(3));

        if(answerArray.get(0).equals(quizSelectedAnswers.get(question_id))){
            answer_01_button.setChecked(true);
        }
        if(answerArray.get(1).equals(quizSelectedAnswers.get(question_id))){
            answer_02_button.setChecked(true);
        }
        if(answerArray.get(2).equals(quizSelectedAnswers.get(question_id))){
            answer_03_button.setChecked(true);
        }
        if(answerArray.get(3).equals(quizSelectedAnswers.get(question_id))){
            answer_04_button.setChecked(true);
        }

        if(answerArray.get(0).equals("0")){
            answer_01_button.setVisibility(View.GONE);
        }
        else {
            answer_01_button.setVisibility(View.VISIBLE);
        }
        if(answerArray.get(1).equals("0")){
            answer_02_button.setVisibility(View.GONE);
        }
        else {
            answer_02_button.setVisibility(View.VISIBLE);
        }
        if(answerArray.get(2).equals("0")){
            answer_03_button.setVisibility(View.GONE);
        }
        else {
            answer_03_button.setVisibility(View.VISIBLE);
        }
        if(answerArray.get(3).equals("0")){
            answer_04_button.setVisibility(View.GONE);
        }
        else {
            answer_04_button.setVisibility(View.VISIBLE);
        }



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                selected = radioButton.getText().toString();
                String id = idTextView.getText().toString();
                quizSelectedAnswers.put(id,selected);
            }
        });
    }
    @Override

    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public HashMap<String,String> getQuizAnswers(){
        return quizSelectedAnswers;
    }
}
