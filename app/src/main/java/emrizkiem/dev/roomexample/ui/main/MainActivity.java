package emrizkiem.dev.roomexample.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import emrizkiem.dev.roomexample.R;
import emrizkiem.dev.roomexample.database.Note;
import emrizkiem.dev.roomexample.helper.ViewModelFactory;
import emrizkiem.dev.roomexample.ui.insert.NoteAddUpdateActivity;
import emrizkiem.dev.roomexample.ui.main.adapter.NoteAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = obtainViewModel(MainActivity.this);
        viewModel.getAllNotes().observe(this, noteObserver);

        noteAdapter = new NoteAdapter(MainActivity.this);

        recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(noteAdapter);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_add) {
                    Intent intent = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
                    startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    showSnackbarMessage(getString(R.string.added));
                }
            } else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {
                    showSnackbarMessage(getString(R.string.changed));
                } else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE) {
                    showSnackbarMessage(getString(R.string.deleted));
                }
            }
        }
    }

    private final Observer<List<Note>> noteObserver = new Observer<List<Note>>() {
        @Override
        public void onChanged(@Nullable List<Note> notes) {
            if (notes != null) {
                noteAdapter.setNoteArrayList(notes);
            }
        }
    };

    @NonNull
    private static MainViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(MainViewModel.class);
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }
}
