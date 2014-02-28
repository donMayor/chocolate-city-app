package info.androidhive.slidingmenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLParser {

    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";
    static ArrayList<String> TwitterLink= new ArrayList<String>();
    static ArrayList<String> youtubeLink= new ArrayList<String>();
    static ArrayList<String> FacebookLink= new ArrayList<String>();
	public XMLParser() {
		
	}

	/**
	 * Getting XML from URL making HTTP request
	 * @param url string
	 * */
	String xml = null;
	 HashMap<String, String> result = new HashMap<String, String>();

	 
	public String getXmlFromUrl(final String url) {
		
		
            	 try {
         			// defaultHttpClient
         			DefaultHttpClient httpClient = new DefaultHttpClient();
         			HttpPost httpPost = new HttpPost(url);

         			HttpResponse httpResponse = httpClient.execute(httpPost);
         			HttpEntity httpEntity = httpResponse.getEntity();
         			xml = EntityUtils.toString(httpEntity);

         		} catch (UnsupportedEncodingException e) {
         			e.printStackTrace();
         		} catch (ClientProtocolException e) {
         			e.printStackTrace();
         		} catch (IOException e) {
         			e.printStackTrace();
         		}
             

		return xml;
	}
	

	public Document getDomElement(String xml){
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(xml));
		        doc = db.parse(is); 

			} catch (ParserConfigurationException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			} catch (SAXException e) {
				Log.e("Error: ", e.getMessage());
	            return null;
			} catch (IOException e) {
				Log.e("Error: ", e.getMessage());
				return null;
			}

	        return doc;
	}
	
	/** Getting node value
	  * @param elem element
	  */
	 public final String getElementValue( Node elem ) {
	     Node child;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                 if( child.getNodeType() == Node.TEXT_NODE  ){
	                     return child.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }
	 

	 public String getValue(Element item, String str) {		
			NodeList n = item.getElementsByTagName(str);		
			return this.getElementValue(n.item(0));
		}

//	 public String getxml(String json_array){
//
//        XMLSerializer serializer = new XMLSerializer();
//        JSON json = JSONSerializer.toJSON( json_array );
//        serializer.setTypeHintsEnabled(false);
//        String xml = serializer.write( json );
//        Log.d("XML", xml);
//
//        return xml;
//    }
		public ArrayList<HashMap<String, String>> getJsonString(String url) {
            ArrayList<HashMap<String, String>> array_result= new ArrayList<HashMap<String,String>>();
			JSONArray jarray = new  JSONArray();

			StringBuilder builder = new StringBuilder();
			  HttpClient client = new DefaultHttpClient();
	          HttpGet httpGet = new HttpGet(url);
	          HttpResponse response;
			try {
				response = client.execute(httpGet);
			
	          StatusLine statusLine = response.getStatusLine();
	          int statusCode = statusLine.getStatusCode();
	          if (statusCode == 200) {
	            HttpEntity entity = response.getEntity();
                  Log.d("Debugging", "Hacker time");
	            InputStream content = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	            String line;
	            while ((line = reader.readLine()) != null) {
	              builder.append(line);
	            }
	          }
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
	            jarray = new JSONArray( builder.toString());
	            Log.d("Json response", "json is "+jarray);
                 if(url.endsWith("instagram")){
                    for(int i = 0; i<jarray.length();i++){
                        JSONObject jobject = new JSONObject();
                        jobject = jarray.getJSONObject(i);
                        result= new HashMap<String, String>();
                        if(jobject.get("id").toString().length()>1 && jobject.get("caption").toString().length()>1&& jobject.get("image_url").toString().length()>1){
                            result= new HashMap<String, String>();
                            result.put(KEY_ID, jobject.get("id").toString());
                            result.put(KEY_TITLE, " ");
                            result.put(KEY_ARTIST, jobject.get("caption").toString());
                            result.put(KEY_DURATION, jobject.get("created_time").toString());
                            result.put(KEY_THUMB_URL,jobject.get("image_url").toString() );
                            array_result.add(result);
                        }
                        else{
                            Log.d("println", "printing");
                        }
                    }
                }else if(url.endsWith("twitter")){
                    for(int i = 0; i<jarray.length();i++){
                        JSONObject jobject = new JSONObject();
                        jobject = jarray.getJSONObject(i);
                        result= new HashMap<String, String>();
                        if(jobject.get("screen_name").toString().length()>1 && jobject.get("caption").toString().length()>1&& jobject.get("profile_image_url").toString().length()>1){
                            result= new HashMap<String, String>();
                            result.put(KEY_ID, jobject.get("id").toString());
                            result.put(KEY_TITLE, "@"+jobject.get("screen_name").toString());
                            result.put(KEY_ARTIST, jobject.get("caption").toString());
                            result.put(KEY_DURATION, jobject.get("created_time").toString());
                            result.put(KEY_THUMB_URL,jobject.get("profile_image_url").toString() );

                            TwitterLink.add("http://www.twitter.com/"+jobject.get("screen_name").toString());
                            array_result.add(result);
                        }
                        else{
                            Log.d("println", "printing");
                        }
                    }
                }else if(url.endsWith("facebook")){
				for(int i = 0; i<jarray.length();i++){
	            	JSONObject jobject = new JSONObject();
	            	jobject = jarray.getJSONObject(i);
	            	if(jobject.get("story").toString().length()>1 && jobject.get("picture").toString().length()>1&& jobject.get("updated_time").toString().length()>1){
	            	result= new HashMap<String, String>();
	            	result.put(KEY_ID, jobject.get("id").toString());
	            	result.put(KEY_TITLE, jobject.get("name").toString());
	            	result.put(KEY_ARTIST, jobject.get("story").toString());
                    result.put(KEY_DURATION, jobject.get("updated_time").toString());
	            	result.put(KEY_THUMB_URL,jobject.get("picture").toString() );

                    FacebookLink.add(jobject.get("url").toString());

	            	array_result.add(result);
                    }
                    else{
                        Log.d("println", "printing");
                    }
	            }
			}else if(url.endsWith("youtube")){
                     for(int i = 0; i<jarray.length();i++){
                         JSONObject jobject = new JSONObject();
                         jobject = jarray.getJSONObject(i);
                         result= new HashMap<String, String>();
                         if(jobject.get("id").toString().length()>1 && jobject.get("title").toString().length()>1&& jobject.get("published").toString().length()>1){
                             result= new HashMap<String, String>();
                             result.put(KEY_ID, jobject.get("id").toString());
                             result.put(KEY_TITLE, " ");
                             result.put(KEY_ARTIST, jobject.get("title").toString());
                             result.put(KEY_DURATION, jobject.get("published").toString());
                             result.put(KEY_THUMB_URL,jobject.get("thumbnail_url").toString() );
                             youtubeLink.add(jobject.get("url").toString());
                             array_result.add(result);
                         }
                         else{
                             Log.d("println", "printing");
                         }
                     }
                 }
	           Log.d("hashmap", "hashmap returns " +array_result);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
			//for (int i =)
			return array_result;
		}

}
