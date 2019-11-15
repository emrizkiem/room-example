package emrizkiem.dev.roomexample.ui.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import emrizkiem.dev.roomexample.R;
import emrizkiem.dev.roomexample.database.Note;
import emrizkiem.dev.roomexample.helper.NoteDiffCallback;
import emrizkiem.dev.roomexample.ui.insert.NoteAddUpdateActivity;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final ArrayList<Note> noteArrayList = new ArrayList<>();
    private final Activity activity;

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setNoteArrayList(List<Note> noteArrayList) {
        final NoteDiffCallback diffCallback = new NoteDiffCallback(this.noteArrayList, noteArrayList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.noteArrayList.clear();
        this.noteArrayList.addAll(noteArrayList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.tvTitle.setText(noteArrayList.get(position).getTitle());
        holder.tvDescription.setText(noteArrayList.get(position).getDescription());
        holder.tvDate.setText(noteArrayList.get(position).getDate());
        holder.cvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NoteAddUpdateActivity.class);
                intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, holder.getAdapterPosition());
                intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, noteArrayList.get(holder.getAdapterPosition()));
                activity.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvDate;
        private final CardView cvNote;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDescription = itemView.findViewById(R.id.tv_item_description);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item_note);
        }
    }
}
