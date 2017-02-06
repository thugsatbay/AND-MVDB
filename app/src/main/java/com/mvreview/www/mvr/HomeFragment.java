package com.mvreview.www.mvr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mvreview.www.mvr.APIProvider.MovieCollectionProvider;
import com.mvreview.www.mvr.Adapters.CustomGridAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private boolean flagLoad;
    private final String LOGTAG=HomeFragment.class.getName();
    private Context homeFragmentContext;
    private int totalMovies;
    private int pageLoaded;
    private int moviesLoadedAtRefresh;
    GridView gv;
    ProgressBar pb;
    EditText et;


    CustomGridAdapter adapter;
    @Override
    public void onStart() {
        super.onStart();
        homeFragmentContext=this.getActivity();
        //Clearing the adapter to load again
        /*
        adapter.clear();
            moviesLoadedAtRefresh=0;
            totalMovies=0;
            pageLoaded=1;
        }
        Log.i(LOGTAG,""+pageLoaded);
         */
        if (adapter.getCount()==0) {
            updateMovieList(pageLoaded);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public HomeFragment() {
        flagLoad=false;
        moviesLoadedAtRefresh=0;
        totalMovies=0;
        pageLoaded=1;
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            //updateMovieList(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        gv = (GridView) rootView.findViewById(R.id.home_grid_view);
        pb=(ProgressBar)rootView.findViewById(R.id.home_progress_bar);
        //et=(EditText)rootView.findViewById(R.id.home_search_edit_text);
        adapter = new CustomGridAdapter(this.getActivity(),R.layout.grid_image_view, new ArrayList<MovieCollectionProvider>());
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.e(LOGTAG,"Scroll State : "+scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Log.e(LOGTAG,"Visible view Items"+visibleItemCount+" "+firstVisibleItem);
//                if(firstVisibleItem==openViewVisibleItemsInGrid){
//                    openViewVisibleItemsInGrid=visibleItemCount;
//                    Log.e(LOGTAG,"Open view Items"+openViewVisibleItemsInGrid);
//                }
                if((firstVisibleItem+visibleItemCount>=totalItemCount)&&(flagLoad)){
                    Toast.makeText(getActivity(),"Creating Theater For New Movies!",Toast.LENGTH_SHORT).show();
                    flagLoad=false;
                    ++pageLoaded;
                    updateMovieList(pageLoaded);
                }
            }
        });
        gv.setAdapter(adapter);
        return rootView;
    }


    private void updateMovieList(int pageNumber){
        if(adapter.isEmpty()){
            gv.setVisibility(View.GONE);
            //et.setVisibility(View.GONE);
        }
        //et.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        FetchMovieTask fmt=new FetchMovieTask();
        fmt.execute(""+pageNumber);
        Log.i(LOGTAG, "Updating Movie List");
    }


    public class FetchMovieTask extends AsyncTask<String,Integer,List<MovieCollectionProvider>> {

        @Override
        protected void onPostExecute(List<MovieCollectionProvider> movieProperties) {
            if (movieProperties!=null){
                //adapter.clear();
                for(MovieCollectionProvider mps : movieProperties){
                    adapter.add(mps);
                    //Log.e(LOG_TAG, "added movie to adapter "+mps.getId());
                }
                if(gv.getVisibility()==View.GONE){
                    gv.setVisibility(View.VISIBLE);
                }
                pb.setVisibility(View.GONE);
                //et.setVisibility(View.VISIBLE);
                flagLoad=true;
                Log.i(LOG_TAG,"Added all Movies to adapter "+adapter.getCount());
                Toast.makeText(homeFragmentContext,"New Theater Opened!",Toast.LENGTH_SHORT).show();
            }
        }

        private final String LOG_TAG=FetchMovieTask.class.getName().toString();
        private HttpURLConnection httpurl;
        private BufferedReader br;
        private InputStream ist;


        FetchMovieTask()
        {
            httpurl=null;
            br=null;
            ist=null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (((values[0]/2)-5)==values[1]){
                //Log.e(LOG_TAG,"half load");
                Toast.makeText(homeFragmentContext,"Putting Movie Posters ... Arranging Chairs!",Toast.LENGTH_SHORT).show();

            }
            if(values[0]==values[1])
            {
                moviesLoadedAtRefresh=values[2];
                totalMovies+=moviesLoadedAtRefresh;
            }
        }

        @Override
        protected List<MovieCollectionProvider> doInBackground(String... params) {
            //If no parameters passed
            if (params.length==0){
                return null;
            }

            //Query Values
            Date dd;
            DateFormat df=new SimpleDateFormat("yyyy");
            StringBuffer sb= new StringBuffer();
            String line;
            String currentYear;


            final String base_url="http://api.themoviedb.org/3/discover/movie";
            final String base_configuration="http://api.themoviedb.org/3/configuration";
            final String APIkey="0ffac534f18c20387c12c071460f72bd";


            //Query Parameters
            final String QUERY_KEY="api_key";
            //final String QUERY_EXTRA="append_to_response";
            final String QUERY_YEAR="year";
            final String QUERY_PAGE="page";
            final String QUERY_PRIMARY_YEAR="primary_release_year";
            dd=new Date();
            currentYear=df.format(dd);


            try {
                Uri ur = Uri.parse(base_url).buildUpon().appendQueryParameter(QUERY_KEY, APIkey).appendQueryParameter(QUERY_PRIMARY_YEAR, currentYear).appendQueryParameter(QUERY_YEAR, currentYear).appendQueryParameter(QUERY_PAGE,params[0]).build();
                Log.i(LOG_TAG, ur.toString());
                URL mvdburl = new URL(ur.toString());
                httpurl=(HttpURLConnection)mvdburl.openConnection();
                httpurl.setRequestMethod("GET");
                httpurl.connect();
                ist=httpurl.getInputStream();
                if (ist==null){
                    return null;
                }
                br=new BufferedReader(new InputStreamReader(ist));
                if (br==null){
                    return null;
                }
                while((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
                if(sb.length()==0){
                    return null;
                }

            }
            catch (MalformedURLException mURLe) {
                Log.e(LOG_TAG+" malformed url","Error",mURLe);
            }
            catch (IOException ioe) {
                Log.e(LOG_TAG+" IOException","Error",ioe);
            }
            finally {
                if(httpurl!=null){
                    httpurl.disconnect();
                }
                if(br!=null){
                    try {
                        br.close();
                    }
                    catch (IOException ioe){}
                }
            }
            try {
                return JSONtoArray(sb.toString());
            }
            catch (JSONException jsone) {

            }
            return null;
        }

        private List<MovieCollectionProvider> JSONtoArray(String q)throws JSONException{
            final String RESULT="results";

            JSONObject jsonObject=new JSONObject(q);
            JSONArray jsonArray=jsonObject.getJSONArray(RESULT);
            MovieCollectionProvider[]mv=new MovieCollectionProvider[jsonArray.length()];
            List<MovieCollectionProvider> lmv=new ArrayList<MovieCollectionProvider>();

            int moviesFound=0;
            for (int i=0;i<jsonArray.length();++i){
                JSONObject mvObject=jsonArray.getJSONObject(i);
                mv[i]=new MovieCollectionProvider();
                mv[i].setBackdrop_path(mvObject.getString(MovieCollectionProvider.name_backdrop_path));
                mv[i].setId(mvObject.getString(MovieCollectionProvider.name_id));
                mv[i].setOverview(mvObject.getString(MovieCollectionProvider.name_overview));
                mv[i].setPoster_path("https://image.tmdb.org/t/p/w154" + mvObject.getString(MovieCollectionProvider.name_poster_path));
                //mv.setPoster_path("https://pbs.twimg.com/profile_images/1077920034/F3_100x100falloutav-vb.gif");
                mv[i].setTitle(mvObject.getString(MovieCollectionProvider.name_title));
                Bitmap bp;
                bp=null;
                if(mvObject.getString(MovieCollectionProvider.name_poster_path)=="null" || mvObject.getString(MovieCollectionProvider.name_poster_path)==null)
                {

                }
                else {
                    try {
                        InputStream in = new java.net.URL(mv[i].getPoster_path()).openStream();
                        mv[i].setBitmap_poster(bp = BitmapFactory.decodeStream(in));
                        //Log.e(LOG_TAG, mv[i].getPoster_path() + " " + bp.getWidth());

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                        mv[i].setBitmap_poster(null);
                        e.printStackTrace();
                    }
                    //if(i==0){

                    //Toast.makeText(getActivity(),mv.getBackdrop_path(),Toast.LENGTH_LONG).show();
                    //}
                    lmv.add(mv[i]);
                    ++moviesFound;
                }
                //if((jsonArray.length()/2)<=i) {
                    publishProgress(jsonArray.length(), i,moviesFound);
                //}

//            mv=null;
//            mv=new MovieCollectionProvider();
            }

            return lmv;
        }

    }





}
