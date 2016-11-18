package com.mvreview.www.mvr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mvreview.www.mvr.APIMVDB.APInfo;
import com.mvreview.www.mvr.APIProvider.MovieIDProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Faizi on 23-11-2015.
 */
public class MovieFragment extends Fragment {
   public MovieFragment(){}

    private ProgressBar progressMovie;
    private String movieID;
    private ImageView backgroundImage;
    private FragmentActivity fragmentActivity;
    private TextView movieTitle;
    private TextView movieOverview;


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
        progressMovie.setVisibility(View.VISIBLE);
        movieID=getActivity().getIntent().getStringExtra("ID");
        Toast.makeText(getActivity(),"Movie : "+getActivity().getIntent().getStringExtra("TITLE"), Toast.LENGTH_LONG).show();
        fragmentActivity=this.getActivity();
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
        }


        @Override
        protected void onPostExecute(MovieIDProvider movieIDProvider) {
            super.onPostExecute(movieIDProvider);
            if(fragmentActivity.findViewById(R.id.movie_background_image)!=null) {
                int widthImageView=fragmentActivity.findViewById(R.id.movie_background_image).getWidth();
                backgroundImage.setImageBitmap(Bitmap.createScaledBitmap(movieIDProvider.getBitmap_background(),widthImageView,(widthImageView*3)/4,true));
            }
            movieTitle.setText(movieIDProvider.getTitle());
            movieOverview.setText(movieIDProvider.getOverview());
            progressMovie.setVisibility(View.GONE);
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
