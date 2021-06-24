package com.example.myfavoritebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText inputTitle;
    EditText inputAuthor;
    EditText inputDescription;
    EditText inputId;

    Button btnAdd;
    Button btnDelete;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);

        inputAuthor = findViewById(R.id.inputAuthor);
        inputDescription = findViewById(R.id.inputDescription);
        inputTitle = findViewById(R.id.inputTitle);
        inputId = findViewById(R.id.inputId);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = inputTitle.getText().toString();
                String desc = inputDescription.getText().toString();
                String author = inputAuthor.getText().toString();
                String id = UUID.randomUUID().toString();
                addBook(id , title , desc, author);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = inputId.getText().toString();

                deleteBook(id);

            }
        });

    }

    private void deleteBook(String id) {
        if (!id.isEmpty()){
            db.collection("Books").document(id).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Registro deletado!!", Toast.LENGTH_SHORT).show();
                                inputId.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Falha no processo !!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "O campo id deve ser preenchido!", Toast.LENGTH_SHORT).show();
        }
    }


    public void addBook(String id , String title , String desc, String author) {
        if (!title.isEmpty() && !desc.isEmpty() && !author.isEmpty()){
            HashMap<String , Object> map = new HashMap<>();
            map.put("id" , id);
            map.put("title" , title);
            map.put("desc" , desc);
            map.put("author", author);

            db.collection("Books").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Registro salvo!!", Toast.LENGTH_SHORT).show();

                                inputTitle.setText("");
                                inputDescription.setText("");
                                inputAuthor.setText("");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Falha no processo !!", Toast.LENGTH_SHORT).show();
                }
            });

        }else
            Toast.makeText(this, "Campos vazios não são permitidos!", Toast.LENGTH_SHORT).show();
    }
}
