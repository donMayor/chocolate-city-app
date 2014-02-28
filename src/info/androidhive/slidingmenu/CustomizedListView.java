package info.androidhive.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CustomizedListView extends Activity {
	// All static variables
	static final String URL = "http://api.androidhive.info/music/music.xml";
	// XML node keys
	static final String KEY_SONG = "song"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_ARTIST = "artist";
	static final String KEY_DURATION = "duration";
	static final String KEY_THUMB_URL = "thumb_url";
	
	ListView list;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> songsList ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		list=(ListView)findViewById(R.id.list);
		new LoadList().execute();
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});		
	}	
	
	class LoadList extends AsyncTask<String, String, String> {


	    XMLParser parser;
	    
	    @Override
	    protected void onPreExecute() {
	   
	    }
	    @Override
	    protected String doInBackground(String... args) {
	    	
	    	songsList= new ArrayList<HashMap<String, String>>();

			parser = new XMLParser();
		//	String xml = parser.getXmlFromUrl(URL); // getting XML from URL
	        return "xml";
	        
	    }
	    protected void onPostExecute(String xml) {

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
				adapter=new LazyAdapter(CustomizedListView.this, songsList);      
				list.setAdapter(adapter);
			}
			
	    }
	}
}