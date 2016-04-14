package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import deepmehtait.com.githubissues.R;
import modal.Comments;


/**
 * Created by Deep on 13-Apr-16.
 */
// Custom Base Adapter Class for displaying ListView in Comments Dialog.
public class CommentsListAdapter extends BaseAdapter {
    ArrayList<Comments> comments = new ArrayList<Comments>();
    Context context;
    private static LayoutInflater inflater = null;

    public CommentsListAdapter(Context context, ArrayList<Comments> comments) {
        this.comments = comments;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView username,body;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View row;
        row=inflater.inflate(R.layout.comments_list_view,null);
        holder.username=(TextView)row.findViewById(R.id.username);

        holder.username.setText(comments.get(position).getUser().getLogin());
        holder.body=(TextView)row.findViewById(R.id.comments);
        holder.body.setText(comments.get(position).getBody());

        return row;
    }
}
