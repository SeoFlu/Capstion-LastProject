package capstion.fluseo.dataController;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import capstion.fluseo.R;
import capstion.fluseo.Vo.BoardMenu;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BoardListViewAdapter extends BaseAdapter {
    /** * ListView에 세팅할 Item 정보들 */
//    private List articleList;
    private List<BoardMenu> boardList;
    /** * ListView에 Item을 세팅할 요청자의 정보가 들어감 */
    private Context context;
    /** * 생성자 * * @param articleList * @param context */
//    public BoardListViewAdapter(List articleList, Context context) {
//        this.articleList = articleList;
//        this.context = context;
//    }


    public BoardListViewAdapter(List<BoardMenu> boardList, Context context) {
        this.boardList = boardList;
        this.context = context;
    }

    /** * ListView에 세팅할 아이템의 갯수 * @return */
    @Override
    public int getCount() { return boardList.size(); }
    /** * position 번째 Item 정보를 가져옴 * @param position * @return */
    @Override
    public Object getItem(int position) { return boardList.get(position); }
    /** * 아이템의 index를 가져옴 * Item index == position * @param position * @return */
    @Override
    public long getItemId(int position) { return position; }
    /** * ListView에 Item들을 세팅함 * position 번 째 있는 아이템을 가져와서 converView에 넣은다음 parent에서 보여주면된다.
     * * @param position : 현재 보여질 아이템의 인덱스, 0 ~ getCount() 까지 증가
     * * @param convertView : ListView의 Item Cell(한 칸) 객체를 가져옴
     * * @param parent : ListView * @return */
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        /** * 가장 간단한 방법 * 사용자가 처음으로 Flicking을 할 때, 아래쪽에 만들어지는 Cell(한 칸)은 Null이다. */
//        if( convertView == null ) {
//            // Item Cell에 Layout을 적용시킬 Inflater 객체를 생성한다.
//             LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//            // Item Cell에 Layout을 적용시킨다.
//            // 실제 객체는 이곳에 있다.
//             convertView = inflater.inflate(R.layout.boarditemlist, parent, false); }
//             TextView tvSubject = (TextView) convertView.findViewById(R.id.tvName);
//             TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
//             TextView tvHitCount = (TextView) convertView.findViewById(R.id.tvHitCount);
//             BoardMenu article = (BoardMenu) getItem(position);
//             tvSubject.setText(article.getBoardName());
//             tvAuthor.setText(article.getBoardAuthor());
//             tvHitCount.setText(article.getBoardHitCount() + "");
//             return convertView;

        View v = View.inflate(context,R.layout.boarditemlist,null);
        TextView boardName = (TextView)v.findViewById(R.id.tvName);
        TextView boarfDate = (TextView)v.findViewById(R.id.tvDate);
        TextView boardAuth = (TextView)v.findViewById(R.id.tvAuthor);
        TextView boardHitCount = (TextView)v.findViewById(R.id.tvHitCount);

        boardName.setText(boardList.get(position).getBoardName());
        boarfDate.setText(boardList.get(position).getBoardDate());
        boardAuth.setText(boardList.get(position).getBoardAuthor());
        String count = String.valueOf(boardList.get(position).getBoardHitCount());
        boardHitCount.setText(count) ;

        return v;
    }
}

