package cn.edu.xmu.ForDream.group;

import java.util.ArrayList;     
import java.util.List;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.friend.WXMessage;
import cn.edu.xmu.ForDream.util.AsynImageLoader;
import cn.edu.xmu.ForDream.util.FinalUrl;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


public class GroupAdapter extends BaseExpandableListAdapter implements Filterable{
    /**
     * 上下文对象
     */
    private Context mContext = null;
    List<GroupEntity> Gdata;
    List<GroupEntity> mOriginalValues; 
    private List<List<GroupPerson>> mData = new ArrayList<List<GroupPerson>>();
    private LayoutInflater mInflater = null;
    public int mRightWidth = 0;
    private AsynImageLoader asynImageLoader;
    /**
     * @param mainActivity
     */
    public GroupAdapter(Context ctx,List<GroupEntity> data, List<List<GroupPerson>> data2, int rightWidth) {
        mContext = ctx;
        mRightWidth = rightWidth;
        Gdata=data;
        mData=data2;
        mInflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        asynImageLoader=new AsynImageLoader();
    }

    public static class GroupViewHolder {
    	RelativeLayout item_left;
    	LinearLayout item_right;

        TextView tv_title;
        TextView tv_msg;
        TextView tv_time;
        TextView tv_number;
        TextView tv_type;
        ImageView iv_icon;

        TextView item_right_txt;
        TextView item_left_txt;
        TextView item_mid_txt;
        
        public boolean needInflate;
    }


    private class ChildViewHolder {
    	RelativeLayout item_left;
    	LinearLayout item_right;
        ImageView mIcon;
        ImageView sign;
        TextView mChildName;
        TextView mDetail;
        
        TextView item_right_txt;
        TextView item_left_txt;
    }
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;
    private onLeftItemClickListener nListener = null;
    private onCRightItemClickListener cmListener = null;
    private onCLeftItemClickListener cnListener = null;
    private onMidItemClickListener dListener = null;
    private onPictureClickListener pLisener = null;
    
    public void setOnMidItemClickListener(onMidItemClickListener listener){
    	dListener = listener;
    }

    public interface onMidItemClickListener {
        void onMidItemClick(View v, int position);
    }
    public void setOnPictureClickListener(onPictureClickListener listener){
    	pLisener= listener;
    }

    public interface onPictureClickListener {
        void onPictureClick(int position);
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
    /** **/
    public void setOnCRightItemClickListener(onCRightItemClickListener listener){
    	cmListener = listener;
    }
    public interface onCRightItemClickListener {
        void onCRightItemClick(View v, int position);
    }
    
    public void setOnCLeftItemClickListener(onCLeftItemClickListener listener){
    	cnListener = listener;
    }
    public interface onCLeftItemClickListener {
        void onCLeftItemClick(View v, int groupPosition, int childPosition);
    }

    

	public GroupPerson getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).get(childPosition);
	}
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).size();
	}
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return Gdata.size();
	}
	public List<GroupPerson> getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return  mData.get(groupPosition);
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildViewHolder holder = new ChildViewHolder();
		 if (convertView == null) {
        	convertView = mInflater.inflate(R.layout.child_item_layout, null);//LayoutInflater.from(mContext).inflate(R.layout.child_item_layout, null, false);
		 }
        holder.item_left = (RelativeLayout)convertView.findViewById(R.id.left);
        holder.item_right = (LinearLayout)convertView.findViewById(R.id.right);
        holder.item_left_txt = (TextView) convertView.findViewById(R.id.item_left_txt);
        holder.item_right_txt = (TextView) convertView.findViewById(R.id.item_right_txt);
        holder.mChildName=(TextView) convertView.findViewById(R.id.item_name);
        holder.mIcon=(ImageView) convertView.findViewById(R.id.image_name);
        holder.sign=(ImageView) convertView.findViewById(R.id.image_host);
        if(!Gdata.get(groupPosition).getUserid().equals(Gdata.get(groupPosition).getCreateid()))
        {
        	holder.item_left_txt.setVisibility(View.GONE);
        }
        if(holder.sign.getVisibility() == View.VISIBLE)
        {
        	holder.sign.setVisibility(View.INVISIBLE);
        }
        if(holder.item_left_txt.getVisibility() == View.GONE)
        {
        	holder.item_left_txt.setVisibility(View.VISIBLE);
        }
        if(Gdata.get(groupPosition).getCreateid().equals(getChild(groupPosition,childPosition).getId()))
        {
        	holder.item_left_txt.setVisibility(View.GONE);
        	holder.sign.setVisibility(View.VISIBLE);
        	Log.i("tag","success");
        }
        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        holder.mChildName.setText(getChild(groupPosition,childPosition).getNickname());
        asynImageLoader.showImageAsyn(holder.mIcon, FinalUrl.USERIMAGE_URL+getChild(groupPosition,childPosition).getUserimage(), R.drawable.default_video_img); 
        holder.item_left_txt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (cnListener != null) {
                 cnListener.onCLeftItemClick(v,groupPosition,childPosition);
                }
            }
        });
        
        holder.item_right_txt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (cmListener != null) {
                    cmListener.onCRightItemClick(v, childPosition);
                }
            }
        });
        
        
        return convertView;
	}

	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder;
	      final View view;
	      if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.group_list_item, null);//LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
	            holder = new GroupViewHolder();
	            holder.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
	            holder.item_right = (LinearLayout)convertView.findViewById(R.id.item_right);
	            
	            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
	            holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
	            holder.tv_msg = (TextView)convertView.findViewById(R.id.tv_msg);
	            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
	            holder.tv_number = (TextView)convertView.findViewById(R.id.tv_number);
	            holder.tv_type = (TextView)convertView.findViewById(R.id.tv_type);
	            
	            holder.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
	            holder.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
	            holder.item_mid_txt = (TextView)convertView.findViewById(R.id.item_mid_txt);
	            convertView.setTag(holder);
	        } else {// 有直接获得ViewHolder
	            holder = (GroupViewHolder)convertView.getTag();
	        }
	        if(!Gdata.get(groupPosition).getUserid().equals(Gdata.get(groupPosition).getCreateid()))
	        {
	        	holder.item_left_txt.setVisibility(View.GONE);
	        }
            LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
	                LayoutParams.MATCH_PARENT);
	        holder.item_left.setLayoutParams(lp1);
	        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
	        holder.item_right.setLayoutParams(lp2);
	        
	        
	        Log.i("tag",String.valueOf(mRightWidth));
	        holder.tv_title.setText(Gdata.get(groupPosition).getNickname());
	        holder.tv_msg.setText(Gdata.get(groupPosition).getDeclare());
	        holder.tv_number.setText(" (" + mData.get(groupPosition).size() + ") 人");
	        holder.tv_time.setText(Gdata.get(groupPosition).getRemarks());
	        holder.tv_type.setText(" ["+Gdata.get(groupPosition).getType()+"] ");
	        asynImageLoader.showImageAsyn(holder.iv_icon, FinalUrl.GroupIMAGE_URL+Gdata.get(groupPosition).getGroupimage(), R.drawable.default_video_img); 
	       // holder.tv_time.setText(msg.getTime());
	        
	       // holder.iv_icon.setImageResource(msg.getIcon_id());
	        holder.iv_icon.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	 if (pLisener != null) {
	            		 pLisener.onPictureClick(groupPosition);
		                }
	            }
	        });
	        holder.item_left_txt.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                if (nListener != null) {
	                 nListener.onLeftItemClick(v, groupPosition);
	                }
	            }
	        });
	        
	        holder.item_right_txt.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                if (mListener != null) {
	                    mListener.onRightItemClick(v, groupPosition);
	                }
	            }
	        });
	        holder.item_mid_txt.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                if (dListener != null) {
	                    dListener.onMidItemClick(v, groupPosition);
	                }
	            }
	        });
	        return convertView;
		// TODO Auto-generated method stub
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	public Filter getFilter() {
		// TODO Auto-generated method stub
		Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                Gdata = (List<GroupEntity>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<GroupEntity> FilteredArrList = new ArrayList<GroupEntity>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<GroupEntity>(Gdata); // saves the original data in mOriginalValues
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
                    	GroupEntity data = mOriginalValues.get(i);
                        if (data.getRemarks().toLowerCase().startsWith(constraint.toString())||data.getType().toLowerCase().startsWith(constraint.toString())||data.getNickname().toLowerCase().startsWith(constraint.toString())||data.getDeclare().toLowerCase().startsWith(constraint.toString())) {
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