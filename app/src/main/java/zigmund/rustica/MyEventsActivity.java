package zigmund.rustica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import android.provider.Settings.Secure;


public class MyEventsActivity extends ActionBarActivity {

    private String android_id ;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflator = getMenuInflater();
        menuInflator.inflate(R.menu.menu_create_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.create_event:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyEventsActivity.this);
                LayoutInflater inflator = LayoutInflater.from(context);
                View dialogView = inflator.inflate(R.layout.dialog_create_event, null);

                dialogBuilder.setView(dialogView);
                final EditText eventNameEditText = (EditText)dialogView.findViewById(R.id.eventNameET);

                dialogBuilder.setPositiveButton(R.string.createEventConfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("debug", eventNameEditText.getText().toString());
                        String eventName = eventNameEditText.getText().toString();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("eventName", eventName);
                        params.put("eventCreator", android_id);
                        new CreateEventTask().execute(params);
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    private class CreateEventTask extends AsyncTask<HashMap<String, String>, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(HashMap<String, String>... params) {
            String url = "http://45.55.157.109:7701/createEvent";
            HttpHandler httpHandler = new HttpHandler();
            JSONObject result = httpHandler.getJSONFromUrl(url, params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
