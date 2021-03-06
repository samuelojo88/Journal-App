package journal.samuel.ojo.com.journalapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import journal.samuel.ojo.com.journalapp.R;
import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.Journal;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelServiceFactory;
import journal.samuel.ojo.com.journalapp.util.AppUtil;

public class RecyclerViewJournalAdapter extends RecyclerView.Adapter<RecyclerViewJournalAdapter.ViewHolder> {

    private Context context;
    private List<Journal> journalItems;
    private JournalItemClickListener journalItemClickListener;
    private JournalLabelServiceFactory journalLabelServiceFactory;

    public RecyclerViewJournalAdapter(Context context, JournalItemClickListener journalItemClickListener, JournalDatabase journalDatabase) {
        this.context = context;
        this.journalItemClickListener = journalItemClickListener;
        this.journalLabelServiceFactory = new JournalLabelServiceFactory(journalDatabase);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Journal journalItem = this.journalItems.get(position);

        holder.tvTitle.setText(journalItem.getTitle());
        holder.tvJournalText.setText(journalItem.getJournalText());

        Integer journalLabelId = journalItem.getJournalLabelId();
        if(journalLabelId != null) {
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.tvLabel.setText(this.journalLabelServiceFactory.findById(journalLabelId).getLabel());
        } else {
            holder.tvLabel.setVisibility(View.GONE);
        }

        Long createdOn = journalItem.getCreatedOn();
        CharSequence formattedCreatedOnDate = AppUtil.getFormattedDate(createdOn);
        CharSequence formattedCreatedOnTime = AppUtil.getFormattedTime(createdOn);

        holder.tvCreatedOnDate.setText(formattedCreatedOnDate);
        holder.tvCreatedOnTime.setText(formattedCreatedOnTime);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journalItemClickListener.onItemClickListener(journalItem.getId());
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journalItemClickListener.onEditClickListener(journalItem.getId());
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journalItemClickListener.onDeleteClickListener(journalItem.getId());
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToShare = "Check out my Journal entry: " + journalItem.getTitle() + "\n" + journalItem.getJournalText();
                journalItemClickListener.onShareClickListener(textToShare);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.journalItems == null ? 0 : this.journalItems.size();
    }

    public List<Journal> getJournalItems() {
        return journalItems;
    }

    public void setJournalItems(List<Journal> journalItems) {
        this.journalItems = journalItems;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tvTitle;
        TextView tvJournalText;
        TextView tvCreatedOnDate;
        TextView tvCreatedOnTime;
        ImageButton btnEdit;
        ImageButton btnShare;
        ImageButton btnDelete;
        TextView tvLabel;


        ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvJournalText = itemView.findViewById(R.id.tvJournalText);
            tvCreatedOnDate = itemView.findViewById(R.id.tvCreatedOnDate);
            tvCreatedOnTime = itemView.findViewById(R.id.tvCreatedOnTime);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvLabel = itemView.findViewById(R.id.tvLabel);
        }
    }

    public interface JournalItemClickListener {
        void onItemClickListener(int journalId);
        void onEditClickListener(int journalId);
        void onDeleteClickListener(int journalId);
        void onShareClickListener(String textToShare);
    }
}
