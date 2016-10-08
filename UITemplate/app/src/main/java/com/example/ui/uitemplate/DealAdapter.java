package com.example.ui.uitemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Administrator on 9/30/2016.
 */

public class DealAdapter extends BaseAdapter {

    Context context;
    int[] imgs;
    private int lastPosition = -1;

    public DealAdapter(Context context,int[] imgs)
    {
        this.context=context;
        this.imgs=imgs;

    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return imgs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_deal, null);

            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.imgDealImage);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();

        }

          viewHolder.imageView.setImageResource(imgs[0]);

     //   Animation animation = AnimationUtils.loadAnimation(context, (position < lastPosition) ? R.anim.abc_fade_in  : R.anim.abc_fade_out);
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom   : R.anim.down_from_bottom);
        convertView.startAnimation(animation);
        lastPosition = position;


        return convertView;
    }

    public static class ViewHolder
    {
        ImageView imageView;

    }
}
