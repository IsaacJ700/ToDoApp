package com.example.remind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomePageActivity extends AppCompatActivity {

    private static final String TAG = "HomePageActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDesc;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collection = db.collection("List");
    private DocumentReference reference = db.document("List/ToDo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        editTextTitle = findViewById(R.id.titleText);
        editTextDesc = findViewById(R.id.descriptionText);
        textViewData = findViewById(R.id.textViewData);

    }

    @Override
    protected void onStart() {
        super.onStart();
        collection.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Entry entry = documentSnapshot.toObject(Entry.class);
                    entry.setEntryID(documentSnapshot.getId());

                    String title = entry.getTitle();
                    String description = entry.getDescription();

                    data += "Title: " + title + "\nDescription: " + description + "\n\n";
                }
                textViewData.setText(data);
            }
        });
    }

    public void addNewEntry(View v){
        final String title = editTextTitle.getText().toString();
        final String desc = editTextDesc.getText().toString();

        if (title.length() == 0 || desc.length() == 0){
            Toast.makeText(this, "Please enter in a title and description!", Toast.LENGTH_SHORT).show();
        } else {

                Entry entry = new Entry(title, desc);

                collection.add(entry).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(HomePageActivity.this, "TODO Added!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomePageActivity.this, "Error adding TODO!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
            }
        }

    public void deleteEntry(View v){
        final String title = editTextTitle.getText().toString();
        final String desc = editTextDesc.getText().toString();

        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Entry entry = documentSnapshot.toObject(Entry.class);
                    entry.setEntryID(documentSnapshot.getId());
                    if (title.equals(entry.getTitle()) && desc.equals(entry.getDescription())) {
                        collection.document(entry.getEntryID()).delete();
                    }
                }
            }
        });
    }

    public void loadEntries(){
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Entry entry = documentSnapshot.toObject(Entry.class);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePageActivity.this, "Error loading TODO!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

}
