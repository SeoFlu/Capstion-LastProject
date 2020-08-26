package capstion.fluseo.dataController;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import capstion.fluseo.R;
import capstion.fluseo.Vo.NoticeList;


public class NoticesListAdapter extends BaseAdapter {
    private Context context;
    private List<NoticeList> noticeList;

    public NoticesListAdapter(Context context, List<NoticeList> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.noticellist,null);
        TextView noticeNum = (TextView)v.findViewById(R.id.noticenum);
        TextView noticeTitle = (TextView)v.findViewById(R.id.noticeTitle);
        TextView noticeUser = (TextView)v.findViewById(R.id.noticeUser);
        TextView noticeDate = (TextView)v.findViewById(R.id.noticeDate);

        noticeNum.setText(noticeList.get(i).getNoticeNum());
        noticeTitle.setText(noticeList.get(i).getNoticeTitle());
        noticeUser.setText(noticeList.get(i).getNoticeUser());
        noticeDate.setText(noticeList.get(i).getNoticeDate());

        return v;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

}
