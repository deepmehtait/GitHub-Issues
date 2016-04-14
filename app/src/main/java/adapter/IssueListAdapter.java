package adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import deepmehtait.com.githubissues.R;
import modal.Issues;

/**
 * Created by Deep on 13-Apr-16.
 */
// Custom Base Adapter Class for displaying List of Issues.
public class IssueListAdapter extends BaseAdapter {
    ArrayList<Issues> issue = new ArrayList<Issues>();
    Context context;
    private static LayoutInflater inflater = null;

    public IssueListAdapter(Context context, ArrayList<Issues> issue) {
        this.issue = issue;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return issue.size();
    }

    @Override
    public Issues getItem(int position) {
        // TODO Auto-generated method stub
        return issue.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView title, body;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = inflater.inflate(R.layout.issue_list_view, null);
        holder.title = (TextView) row.findViewById(R.id.listviewTitle);
        holder.title.setText(issue.get(position).getTitle());
        holder.body = (TextView) row.findViewById(R.id.listviewbody);
        holder.body.setText(issue.get(position).getBody());
        return row;
    }
}
