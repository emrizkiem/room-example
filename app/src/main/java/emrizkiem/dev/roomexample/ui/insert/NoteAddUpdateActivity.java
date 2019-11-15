package emrizkiem.dev.roomexample.ui.insert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import emrizkiem.dev.roomexample.R;
import emrizkiem.dev.roomexample.database.Note;
import emrizkiem.dev.roomexample.helper.DateHelper;
import emrizkiem.dev.roomexample.helper.ViewModelFactory;

public class NoteAddUpdateActivity extends AppCompatActivity {
    private EditText editTitle, editDescription;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Note note;
    private int position;

    private NoteAddUpdateViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);

        viewModel = obtainViewModel(NoteAddUpdateActivity.this);

        editTitle = findViewById(R.id.edit_title);
        editDescription = findViewById(R.id.edt_description);
        Button buttonSubmit = findViewById(R.id.btn_submit);

        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            actionBarTitle = getString(R.string.change);
            btnTitle = getString(R.string.update);

            if (note != null) {
                editTitle.setText(note.getTitle());
                editDescription.setText(note.getDescription());
            }
        } else {
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        buttonSubmit.setText(btnTitle);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString().trim();
                String description = editDescription.getText().toString().trim();

                if (title.isEmpty()) {
                    editTitle.setError(getString(R.string.title_empty));
                } else if (description.isEmpty()) {
                    editDescription.setError(getString(R.string.description_empty));
                } else {
                    note.setTitle(title);
                    note.setDescription(description);

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_NOTE, note);
                    intent.putExtra(EXTRA_POSITION, position);

                    if (isEdit) {
                        viewModel.update(note);
                        setResult(RESULT_UPDATE, intent);
                        finish();
                    } else {
                        note.setDate(DateHelper.getCurrentDate());
                        viewModel.insert(note);
                        setResult(RESULT_ADD, intent);
                        finish();
                    }
                }
            }
        });
    }

    @NonNull
    private NoteAddUpdateViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(this, factory).get(NoteAddUpdateViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        } else {
            dialogMessage = getString(R.string.message_delete);
            dialogTitle = getString(R.string.delete);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            viewModel.delete(note);

                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
