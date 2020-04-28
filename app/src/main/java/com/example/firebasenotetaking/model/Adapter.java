package com.example.firebasenotetaking.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firebasenotetaking.note.NoteDetails;
import com.example.firebasenotetaking.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> title;
    List<String> content;

    public Adapter(List<String> title, List<String> content){
        this.title = title;
        this.content = content;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noteview_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.noteTitle.setText(title.get(position));
        holder.noteContent.setText(content.get(position));
        final int code = getRandomColor();
        holder.mcardView.setCardBackgroundColor(holder.view.getResources().getColor(code,null));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoteDetails.class);
                intent.putExtra("title",title.get(position));
                intent.putExtra("content",content.get(position));
                intent.putExtra("code",code);
                v.getContext().startActivity(intent);
            }
        });
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.red);
        colorCode.add(R.color.notgreen);
        colorCode.add(R.color.blue);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.pink);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);

    }


    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;
        View view;
        CardView mcardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mcardView = itemView.findViewById(R.id.noteCard);
            view = itemView;

        }
    }
}
