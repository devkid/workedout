package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by vincent on 02.02.15.
 */
public class ExerciseAdapter extends BaseAdapter{

    private ArrayList<Exercise> exercises= new ArrayList<>();
    private int resourceId;
    Context context;
    private WorkOutPlan workOutPlan;

    public ExerciseAdapter(Context context ,int resourceId,WorkOutPlan workOutPlan){
        super();
        this.resourceId=resourceId;
        this.context = context;
        this.workOutPlan=workOutPlan;
        this.exercises = workOutPlan.getExercises();
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int position) {
        return exercises.get(position);
    }

    @Override
    public long getItemId(int position) {
        return exercises.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ExerciseHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);

            holder = new ExerciseHolder();
            holder.delete = (Button) row.findViewById(R.id.delete_btn);
            holder.start = (Button)row.findViewById(R.id.start_btn);
            holder.txtTitle = (TextView)row.findViewById(R.id.list_item_string);
            //Execise Start
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent =new Intent(context,ExerciseExecutionActivity.class);
                    intent.putExtra("exercise", exercises.get(position).name);
                    context.startActivity(intent);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    workOutPlan.removeExercise(exercises.get(position));
                    exercises.remove(position);
                    notifyDataSetChanged();
                }
            });
            row.setTag(holder);
        }
        else
        {
            holder = (ExerciseHolder)row.getTag();
        }

        Exercise exercise = exercises.get(position);
        holder.txtTitle.setText(exercise.name);


        return row;


    }
    static class ExerciseHolder
    {
        Button delete;
        Button start;
        TextView txtTitle;
    }



}
