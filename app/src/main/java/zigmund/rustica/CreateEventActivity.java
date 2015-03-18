package zigmund.rustica;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class CreateEventActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

    }

    public void onClickCreateEvent(View v){
        EditText eventNameEditText = (EditText)findViewById(R.id.eventNameEditText);
        String eventName = eventNameEditText.getText().toString();
        new ConnectServerTask().execute(eventName);
    }

    private class ConnectServerTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            HttpResponse response = postData(params[0]);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent())) ;
                StringBuilder responseBody = new StringBuilder();
                String line = null;
                while((line = br.readLine()) != null){
                    responseBody.append(line);
                }
                Log.e("LOG", new JSONObject(responseBody.toString()).get("eventId").toString());
                return new JSONObject(responseBody.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        private HttpResponse postData(String data) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://45.55.157.109:7701/createEvent");
            try {
                List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("data", data));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                return response;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
                Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_LONG).show();

        }
    }

}
