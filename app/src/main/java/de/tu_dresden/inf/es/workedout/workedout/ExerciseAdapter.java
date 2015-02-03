package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;

/**
 * Created by vincent on 02.02.15.
 */
public class ExerciseAdapter extends BaseAdapter {

    Context mContext;
    private WorkOutPlan mWorkOutPlan;

    public ExerciseAdapter(Context context, WorkOutPlan workOutPlan){
        super();
        this.mContext = context;
        this.mWorkOutPlan = workOutPlan;
    }

    @Override
    public int getCount() {
        return mWorkOutPlan.getExercises().size();
    }

    @Override
    public Object getItem(int position) {
        return mWorkOutPlan.getExercises().get(position);
    }

    @Override
    public long getItemId(int position) {
        return mWorkOutPlan.getExercises().get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ExerciseHolder holder;

        if(row == null)  {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.atom_exercise_list_view, parent, false);

            holder = new ExerciseHolder();
            holder.delete   = (Button)   row.findViewById(R.id.delete_btn);
            holder.start    = (Button)   row.findViewById(R.id.start_btn);
            holder.txtTitle = (TextView) row.findViewById(R.id.list_item_string);

            // Start exercise
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,ExerciseExecutionActivity.class);
                    intent.putExtra("exercise", ((ExerciseHolder) ((View) v.getParent()).getTag()).exercise.getId());
                    mContext.startActivity(intent);
                }
            });

            // Delete exercise
            holder.delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mWorkOutPlan.removeExercise(((ExerciseHolder) ((View) v.getParent()).getTag()).exercise);
                    notifyDataSetChanged();
                }
            });
            row.setTag(holder);
        }
        else {
            holder = (ExerciseHolder) row.getTag();
        }
        holder.exercise = mWorkOutPlan.getExercises().get(position);
        holder.txtTitle.setText(holder.exercise.name);

        return row;


    }
    static class ExerciseHolder
    {
        Button delete;
        Button start;
        TextView txtTitle;
        Exercise exercise;
    }



}
