package com.mvreview.www.mvr.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvreview.www.mvr.APIProvider.MovieCollectionProvider;
import com.mvreview.www.mvr.MovieActivity;
import com.mvreview.www.mvr.R;

import java.util.ArrayList;

/**
 * Created by Gurleen on 16-11-2015.
 */

public class CustomGridAdapter extends ArrayAdapter<MovieCollectionProvider> implements View.OnClickListener {

    private Context ctxt;
    private String LOGTAG=CustomGridAdapter.class.getName();
    private int widthScreen;
    private int position;


    public class ViewModule{
        public ImageView iv;
        public String movieID;
        public String movieTitle;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag()!=null){
            Log.i(LOGTAG+" Movie ID Called",((ViewModule)v.getTag()).movieID);
            if((ctxt!=null)) {
                Intent mvIntent=new Intent();
                mvIntent.putExtra("ID", ((ViewModule)v.getTag()).movieID);
                mvIntent.putExtra("TITLE", ((ViewModule)v.getTag()).movieTitle);
                Log.e(LOGTAG, "Intent Called with ID is : "+((ViewModule)v.getTag()).movieID);
                mvIntent.setClass(ctxt, MovieActivity.class);
                ctxt.startActivity(mvIntent);
            }}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewModule vm=new ViewModule();
        this.position=position;
        //Log.e(LOGTAG, "position " + position);
        MovieCollectionProvider mp = this.getItem(position);



        if (mp==null){
            return convertView;
        }
        LayoutInflater lif = (LayoutInflater) ctxt.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //iv=new ImageView(ctxt);
            //iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //tvv=new TextView(ctxt);

            convertView = lif.inflate(R.layout.grid_image_view, null);
            vm.iv = (ImageView) convertView.findViewById(R.id.home_picture_view);
            convertView.setOnClickListener(this);

            //vm.movieID=mp.getId();
            //vm.tv = (TextView) convertView.findViewById(R.id.home_text_view);
            convertView.setTag(vm);
        } else {
            //iv = (ImageView) convertView;
            //tvv=(TextView)convertView;
            //vm.movieID=mp.getId();
            vm = (ViewModule) convertView.getTag();
        }
        //tvv.setText(mp.getTitle());
        //vm.tv.setText(mp.getTitle());
        if (mp.getBitmap_poster() == null) {
            vm.iv.setImageResource(R.mipmap.ic_launcher);
        } else {
            //iv.setImageBitmap(mp.getBitmap_poster());}
            //Log.e(LOGTAG,"Grid View Width : "+((GridView)(((Activity)ctxt).findViewById(R.id.home_grid_view))).getWidth()+" "+widthScreen);
            vm.iv.setImageBitmap(Bitmap.createScaledBitmap(mp.getBitmap_poster(),widthScreen/2, (widthScreen*3)/4, true));
            vm.movieID=mp.getId();
            vm.movieTitle=mp.getTitle();
            //vm.iv.setImageBitmap(mp.getBitmap_poster());
            //Log.e(LOGTAG, "Image Set In Grid View");
            //return super.getView(position, convertView, parent);
        }
        return convertView;
        //return convertView;
    }

    public CustomGridAdapter(Context context, int resource, ArrayList<MovieCollectionProvider> objects) {
        super(context, resource, objects);
        this.ctxt=context;
        WindowManager wm = (WindowManager) ctxt.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point pp=new Point();
        display.getSize(pp);
        try {
            widthScreen = ((GridView) (((Activity) ctxt).findViewById(R.id.home_grid_view))).getWidth();
        }
        catch (NullPointerException npe) {
            widthScreen=pp.x;
        }
        if (widthScreen==0){
            widthScreen=pp.x;
        }
        display=null;
        wm=null;

    }
}