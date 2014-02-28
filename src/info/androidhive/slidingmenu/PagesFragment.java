package info.androidhive.slidingmenu;

import info.androidhive.slidingmenu.CommunityFragment.LoadList;

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
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PagesFragment extends ListFragment {


    static final String URL = "https://choccity.herokuapp.com/api/google";

    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> songsList ;
    
	public PagesFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_pages, container, false);
         
        if(adapter==null){
        //	new LoadList().execute();
        }
        else{
        	setListAdapter(adapter);
        }
        return rootView;
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// TODO Auto-generated method stub
		
		inflater=this.getActivity().getMenuInflater();
		SupportMenu menu_=(SupportMenu) menu;
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem item = menu_.add(Menu.NONE, R.id.action_refresh, 200, "Add Event");
		item.setIcon(R.drawable.new_refresh);	
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
			
	}
    class LoadList extends AsyncTask<String, String, String> {


        XMLParser parser;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading ....");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {

            songsList= new ArrayList<HashMap<String, String>>();

            parser = new XMLParser();
            String xml = parser.getXmlFromUrl(URL);// getting XML from URL
            return xml;

        }
        protected void onPostExecute(String xml) {
            pDialog.dismiss();
            Document doc = parser.getDomElement(xml); // getting DOM element

            NodeList nl = doc.getElementsByTagName(KEY_SONG);
            for (int i = 0; i < nl.getLength(); i++) {
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(i);
                // adding each child node to HashMap key => value
                map.put(KEY_ID, parser.getValue(e, KEY_ID));
                map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
                map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
                map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
                map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));

                // adding HashList to ArrayList
                songsList.add(map);
                adapter=new LazyAdapter(getActivity(), songsList);
                setListAdapter(adapter);
            }

        }
    }
}
