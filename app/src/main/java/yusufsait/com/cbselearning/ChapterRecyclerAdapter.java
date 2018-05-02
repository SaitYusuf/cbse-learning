package yusufsait.com.cbselearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerAdapter.ViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a CursorAdapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;

    Context mContext;

    public ChapterRecyclerAdapter(Context context, Cursor c) {

        mContext = context;

        mCursorAdapter = new CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context).inflate(R.layout.chapter_card, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                TextView chapterNameTextView = view.findViewById(R.id.chapter_name);
                TextView chapterNumberTextView = view.findViewById(R.id.chapter_id);
                ImageView chapterIconImageView = view.findViewById(R.id.chapter_icon_image);
                RoundedHorizontalProgressBar quizCompletion = view.findViewById(R.id.quiz_completion);
                ImageView checkImageView = view.findViewById(R.id.check);

                //Extract properties from cursor
                String chapterName = cursor.getString(cursor.getColumnIndexOrThrow("chapter_name"));
                String chapterId = cursor.getString(cursor.getColumnIndexOrThrow("chapter_id"));
                int numberOfChapterParts = cursor.getInt(cursor.getColumnIndexOrThrow("number_of_chapter_parts"));
                int totalNumberOfQuestions = cursor.getInt(cursor.getColumnIndexOrThrow("number_of_questions"));
                String iconName = "chapter_icon_" + chapterId;
                int imageViewId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName() );

                SharedPreferences prefs = context.getSharedPreferences("chapterPartScores",Context.MODE_PRIVATE);

                int totalNumberCompleted = 0;

                for(int i = 1; i <= numberOfChapterParts; i++){
                    String chapterPartId;
                    if(i >= 10){
                        chapterPartId = chapterId + "_" + i;
                    }
                    else {
                        chapterPartId = chapterId + "_0" + i;
                    }
                    int numberCompleted = prefs.getInt(chapterPartId + "_score",0);
                    totalNumberCompleted += numberCompleted;
                }
                // Populate fields with extracted properties
                chapterNameTextView.setText(chapterName);
                chapterNumberTextView.setText(chapterId);
                chapterIconImageView.setImageResource(imageViewId);
                quizCompletion.setMax(totalNumberOfQuestions);
                quizCompletion.setProgress(totalNumberCompleted);
                if(totalNumberCompleted >= totalNumberOfQuestions){
                    DrawableCompat.setTint(checkImageView.getDrawable().mutate(), ContextCompat.getColor(context, R.color.colorPrimary));
                }
                else{
                    DrawableCompat.setTint(checkImageView.getDrawable().mutate(), ContextCompat.getColor(context,R.color.light_grey));
                }
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View v1;

        public ViewHolder(View itemView) {
            super(itemView);
            v1 = itemView.findViewById(R.id.chapter_list);
        }
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }
}