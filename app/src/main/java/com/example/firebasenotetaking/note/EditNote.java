package com.example.firebasenotetaking.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebasenotetaking.MainActivity;
import com.example.firebasenotetaking.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    Intent data;
    EditText editTitle, editContent;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editTitle = findViewById(R.id.editNoteTitle);
        editContent = findViewById(R.id.editNoteContent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        data = getIntent();

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");

        editTitle.setText(noteTitle);
        editContent.setText(noteContent);

        final ProgressBar progressBarSave = findViewById(R.id.proBar4Edit);
        FloatingActionButton fab = findViewById(R.id.saveEditedNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = editTitle.getText().toString();
                String nContent = editContent.getText().toString();

                if (nTitle.isEmpty() || nContent.isEmpty()){
                    Toast.makeText(EditNote.this, "Cannot save with empty field", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBarSave.setVisibility(View.VISIBLE);

                //save notes
                DocumentReference docRef = fStore.collection("notes").document(user.getUid()).collection("myNotes")
                        .document(data.getStringExtra("noteId"));
                Map<String,Object> note = new HashMap<>();
                note.put("title",nTitle);
                note.put("content",nContent);

                docRef.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditNote.this, "Edit Note Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, "Error, Try Again!", Toast.LENGTH_SHORT).show();
                        progressBarSave.setVisibility(View.GONE);
                    }
                });
            }
        });

    }
}
