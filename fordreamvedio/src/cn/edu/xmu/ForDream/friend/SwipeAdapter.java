package cn.edu.xmu.ForDream.friend;

import java.util.ArrayList; 
import java.util.List; 


import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.AsynImageLoader;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.RoundImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


public class SwipeAdapter extends BaseAdapter implements Filterable{
    /**
     * 上下文对象
     */
    private Context mContext = null;
    List<WXMessage> arrayList;      
    List<WXMessage> mOriginalValues; // Original Values
    private AsynImageLoader asynImageLoader;
    public int mRightWidth,size=1;

    /**
     * @param mainActivity
     */
    public SwipeAdapter(Context ctx,List<WXMessage> data) {
        mContext = ctx;
        arrayList = data;
        asynImageLoader = new AsynImageLoader();
    }

    public int getCount() {
        return arrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        ViewHolder holder;
        final View view;   
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            holder.item_right = (LinearLayout)convertView.findViewById(R.id.item_right);
            
            holder.iv_icon = (RoundImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            holder.tv_msg = (TextView)convertView.findViewById(R.id.tv_msg);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            
            holder.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
            holder.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
            holder.item_mid_txt = (TextView)convertView.findViewById(R.id.item_mid_txt);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder)convertView.getTag();
        }
        
        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        
        WXMessage msg = arrayList.get(position);
        
        holder.tv_title.setText(msg.getTitle());
        holder.tv_msg.setText(msg.getMsg());
        holder.tv_time.setText(msg.getTime());
        
       // holder.iv_icon.setImageBitmap(msg.getIcon_id());
   
        asynImageLoader.showImageAsyn(holder.iv_icon, FinalUrl.USERIMAGE_URL+msg.getUserimage(), R.drawable.default_video_img); 
	//asynImageLoader.showImageAsyn(cache.authorImageView, String.valueOf(map.get("headdrawable")), R.drawable.square_item_defaulthead); 
        
        holder.iv_icon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                   
            }
        });
        
        holder.item_left_txt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (nListener != null) {
                 nListener.onLeftItemClick(v, position);
                }
            }
        });
        
        holder.item_right_txt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        holder.item_mid_txt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (dListener != null) {
                    dListener.onMidItemClick(v, position);
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder {
    	RelativeLayout item_left;
    	LinearLayout item_right;

        TextView tv_title;
        TextView tv_msg;
        TextView tv_time;
        RoundImageView iv_icon;

        TextView item_right_txt;
        TextView item_left_txt;
        TextView item_mid_txt;
        
        public boolean needInflate;
    }
    
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;
    private onLeftItemClickListener nListener = null;
    private onMidItemClickListener dListener = null;
    
    public void setOnMidItemClickListener(onMidItemClickListener listener){
    	dListener = listener;
    }

    public interface onMidItemClickListener {
        void onMidItemClick(View v, int position);
    }
    
    
    public void setOnRightItemClickListener(onRightItemClickListener listener){
    	mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
    
    public void setOnLeftItemClickListener(onLeftItemClickListener listener){
    	nListener = listener;
    }
    public interface onLeftItemClickListener {
        void onLeftItemClick(View v, int position);
    }


	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                arrayList = (List<WXMessage>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<WXMessage> FilteredArrList = new ArrayList<WXMessage>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<WXMessage>(arrayList); // saves the original data in mOriginalValues
                }

                /********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                    	WXMessage data = mOriginalValues.get(i);
                        if (data.getTitle().toLowerCase().startsWith(constraint.toString())||data.getMsg().toLowerCase().startsWith(constraint.toString())||data.getTime().toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}