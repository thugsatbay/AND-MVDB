package com.mvreview.www.mvr;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mvreview.www.mvr.APIMVDB.APInfo;
import com.mvreview.www.mvr.APIProvider.MovieIDProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gurleen on 23-11-2015.
 */
public class MovieFragment extends Fragment {
   public MovieFragment(){

   }

    private ProgressBar progressMovie;
    private String movieID;
    private ImageView backgroundImage;
    private FragmentActivity fragmentActivity;
    private LinearLayout movieDescription;
    private LinearLayout movieGenre;
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView budget;
    private TextView releaseDate;
    private TextView budgetTitle;
    private TextView releaseDateTitle;
    private TextView movieTagLine;
    private TextView [] genres;
    private float densityScreen;


    @Override
    public void onResume() {
        super.onResume();
        fragmentActivity=this.getActivity();
    }

    @Override
    public void onStart(){
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_movie,container,false);
        progressMovie = (ProgressBar) rootView.findViewById(R.id.movie_progress_bar);
        backgroundImage=(ImageView)rootView.findViewById(R.id.movie_background_image);
        movieTitle=(TextView)rootView.findViewById(R.id.movie_title);
        movieOverview=(TextView)rootView.findViewById(R.id.movie_overview);
        budget=(TextView)rootView.findViewById(R.id.movie_budget_info);
        releaseDate=(TextView) rootView.findViewById(R.id.movie_release_date_info);
        budgetTitle=(TextView)rootView.findViewById(R.id.movie_budget);
        releaseDateTitle=(TextView) rootView.findViewById(R.id.movie_release_date);
        movieTagLine=(TextView) rootView.findViewById(R.id.movie_tag_line);
        movieDescription=(LinearLayout) rootView.findViewById(R.id.movie_description);
        movieGenre=(LinearLayout) rootView.findViewById(R.id.movie_genres);
        budgetTitle.setVisibility(View.GONE);
        releaseDateTitle.setVisibility(View.GONE);
        movieDescription.setVisibility(View.GONE);
        progressMovie.setVisibility(View.VISIBLE);
        movieID=getActivity().getIntent().getStringExtra("ID");
        Toast.makeText(getActivity(),"Movie : "+getActivity().getIntent().getStringExtra("TITLE"), Toast.LENGTH_SHORT).show();
        fragmentActivity=this.getActivity();
        getActivity().setTitle(getActivity().getIntent().getStringExtra("TITLE"));
        new MovieInfoAsyncDownload().execute(movieID);
        return rootView;
    }





    public class MovieInfoAsyncDownload extends AsyncTask<String,Integer,MovieIDProvider> {

        private Uri baseURL;
        private String LOG_TAG;
        private URL urlLink;
        private HttpURLConnection httpConnection;
        private InputStream inputStream;
        private BufferedReader bufferedReader;
        private StringBuilder jsonResult;
        private String utilityString;
        private JSONObject startJSON;
        private MovieIDProvider movieIData;

        MovieInfoAsyncDownload(){
            LOG_TAG=this.getClass().getSimpleName();
            jsonResult=new StringBuilder();
            densityScreen=fragmentActivity.getResources().getDisplayMetrics().density;
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(MovieIDProvider movieIDProvider) {
            super.onPostExecute(movieIDProvider);
            progressMovie.setVisibility(View.GONE);
            movieDescription.setVisibility(View.VISIBLE);
            if(fragmentActivity.findViewById(R.id.movie_background_image)!=null) {
                int widthImageView=fragmentActivity.findViewById(R.id.movie_background_image).getWidth();
                backgroundImage.setImageBitmap(Bitmap.createScaledBitmap(movieIDProvider.getBitmap_background(),widthImageView,(widthImageView*3)/4,true));
            }
            movieTitle.setText(movieIDProvider.getTitle());
            movieOverview.setText(movieIDProvider.getOverview());
            releaseDate.setText(movieIDProvider.getRelease_date());
            budget.setText("$"+movieIDProvider.getBudget());
            if (movieIDProvider.getTagline()==null || movieIDProvider.getTagline().compareToIgnoreCase("")==0){
                movieTagLine.setVisibility(View.GONE);
            }
            else{
                movieTagLine.setText("- \""+movieIDProvider.getTagline()+"\"");
            }
            genres=new TextView[movieIDProvider.getGenres().length];
            for (int setGenres=0;setGenres<((movieIDProvider.getGenres().length>=3)?3:movieIDProvider.getGenres().length);++setGenres){
                genres[setGenres]=new TextView(fragmentActivity);
                genres[setGenres].setText(movieIDProvider.getGenres()[setGenres]);
                genres[setGenres].setBackground(fragmentActivity.getResources().getDrawable(R.drawable.border,fragmentActivity.getTheme()));
                genres[setGenres].setTextColor(fragmentActivity.getResources().getColor(R.color.colorText));
                genres[setGenres].setPadding((int)(densityScreen*10 + .5f),(int)(densityScreen*10 + .5f),(int)(densityScreen*10 + .5f),(int)(densityScreen*10 + .5f));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)(densityScreen*5 + .5f),(int)(densityScreen*5 + .5f),(int)(densityScreen*5 + .5f),(int)(densityScreen*5 + .5f));
                genres[setGenres].setLayoutParams(params);
                movieGenre.addView(genres[setGenres]);
            }
            budgetTitle.setVisibility(View.VISIBLE);
            releaseDateTitle.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieIDProvider doInBackground(String... params) {
            if (params.length==0)
                return null;

            try{
                Log.i(LOG_TAG,params[0]);
                baseURL=Uri.parse(APInfo.base_movie_url).buildUpon().appendPath(params[0]).appendQueryParameter(APInfo.QUERY_API_KEY,APInfo.API_KEY).build();
                Log.i(LOG_TAG,baseURL.toString());
                urlLink=new URL(baseURL.toString());
                httpConnection=(HttpURLConnection) urlLink.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                inputStream=httpConnection.getInputStream();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                while ((utilityString=bufferedReader.readLine())!=null){
                    jsonResult.append(utilityString+"\n");
                }
                if (jsonResult.length()==0)
                    return null;

            }
            catch (MalformedURLException mURLe) {
                Log.e(LOG_TAG+" Malformed Url","Error",mURLe);
            }
            catch (IOException ioe) {
                Log.e(LOG_TAG+" IOException","Error",ioe);
            }
            finally {
                if(httpConnection!=null){
                    httpConnection.disconnect();
                }
                if(bufferedReader!=null){
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException ioe){}
                }
            }

            try {
                return makingSenseOfJSONData(jsonResult.toString(),params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        private MovieIDProvider makingSenseOfJSONData(String input,String movieID)throws JSONException{
            movieIData=new MovieIDProvider(movieID);
            startJSON=new JSONObject(input);
            //Genres
            JSONArray genreArray=startJSON.getJSONArray(MovieIDProvider.name_genres);
            String [] genreNames=new String[genreArray.length()];
            for (int move=0;move<genreArray.length();++move){
                genreNames[move]=genreArray.getJSONObject(move).getString("name");
            }
            movieIData.setGenres(genreNames);
            movieIData.setAdult(startJSON.getString(MovieIDProvider.name_adult));
            movieIData.setBackdrop_path(startJSON.getString(MovieIDProvider.name_backdrop_path));
            movieIData.setBudget(startJSON.getString(MovieIDProvider.name_budget));
            movieIData.setRelease_date(startJSON.getString(MovieIDProvider.name_release_date));
            movieIData.setTagline(startJSON.getString(MovieIDProvider.name_tagline));
            movieIData.setTitle(startJSON.getString(MovieIDProvider.name_title));
            movieIData.setOverview(startJSON.getString(MovieIDProvider.name_overview));
            if ((movieIData.getBackdrop_path()!=null)||(movieIData.getBackdrop_path().compareToIgnoreCase("null")==0)){
                try {
                    Log.i(LOG_TAG,APInfo.base_image_url+movieIData.getBackdrop_path());
                    movieIData.setBitmap_background(BitmapFactory.decodeStream(new URL(APInfo.base_image_url+movieIData.getBackdrop_path()).openStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return movieIData;
        }

    }












}
