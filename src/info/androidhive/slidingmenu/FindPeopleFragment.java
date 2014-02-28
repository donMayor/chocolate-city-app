package info.androidhive.slidingmenu;

import android.content.Intent;
import android.util.Log;
import info.androidhive.slidingmenu.CommunityFragment.LoadList;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.net.Uri;

public class FindPeopleFragment extends ListFragment {


    static final String URL = "https://choccity.herokuapp.com/api/twitter";

    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";
	MainActivity act;
	ListView list;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> songsList ;
	public FindPeopleFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        act= new MainActivity();
        View rootView = inflater.inflate(R.layout.fragment_find_people, container, false);
        if(adapter==null){
        	new LoadList().execute();
        }
        else{
        	setListAdapter(adapter);
        }
        return rootView;
    }
    class LoadList extends AsyncTask<String,String, ArrayList<HashMap<String, String>>> {


        XMLParser parser;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ....");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... args) {
            Log.d("background", "backgroundthings");
            songsList= new ArrayList<HashMap<String, String>>();
            parser = new XMLParser();
            Log.d("Array_result", ""+parser.getJsonString(URL));
            return  parser.getJsonString(URL);
        }
        protected void onPostExecute(ArrayList<HashMap<String, String>> str) {
            adapter=new LazyAdapter(getActivity(),str);
            pDialog.dismiss();
            Log.d("dismiss", "dismiss things");
            setListAdapter(adapter);


        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.d("Entering her", position+"presntly here");
        Log.d("twitter","http://wwww.twitter.com/"+XMLParser.TwitterLink.get(position));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(XMLParser.TwitterLink.get(position)));
        startActivity(browserIntent);

    }
}
