package com.example.firebasenotetaking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.firebasenotetaking.model.Adapter;
import com.example.firebasenotetaking.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Adapter adapter;
    RecyclerView list_notes;
    FirebaseFirestore fStore;
    FirestoreRecyclerAdapter<Note,NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_notes = findViewById(R.id.recyvler_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        List<String> title = new ArrayList<>();
        List<String> content = new ArrayList<>();

        fStore = FirebaseFirestore.getInstance();
        Query query = fStore.collection("notes").orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull final Note note) {
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContent.setText(note.getContent());
                final int code = getRandomColor();
                noteViewHolder.mcardView.setCardBackgroundColor(noteViewHolder.view.getResources().getColor(code,null));

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), NoteDetails.class);
                        intent.putExtra("title",note.getTitle());
                        intent.putExtra("content",note.getContent());
                        intent.putExtra("code",code);
                        v.getContext().startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noteview_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        list_notes.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        list_notes.setAdapter(noteAdapter);

        FloatingActionButton fab = findViewById(R.id.addNoteFloat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddNote.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.createNote:
                startActivity(new Intent(getApplicationContext(),AddNote.class));


            default:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteContent;
        View view;
        CardView mcardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mcardView = itemView.findViewById(R.id.noteCard);
            view = itemView;

        }
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
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter !=null){
            noteAdapter.stopListening();
        }
    }
}
