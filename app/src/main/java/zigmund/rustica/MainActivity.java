package zigmund.rustica;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickScan(View v){
        try {
            Intent scanIntent = new Intent(ACTION_SCAN);
            scanIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(scanIntent, 0);
        }catch(ActivityNotFoundException e){
            showInstallDialog(this, "Install Scanner", "Yes", "No").show();
        }

    }

    private AlertDialog showInstallDialog(final Activity mainActivity, String title, String yes, String no) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(mainActivity);
        downloadDialog.setIcon(R.drawable.queue_cut);
        downloadDialog.setTitle(title);
        downloadDialog.setPositiveButton(yes, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try{
                    mainActivity.startActivity(intent);
                }catch(ActivityNotFoundException e){
                    Log.e("ERROR", "No appstore found");
                }
            }
        });
        downloadDialog.setNegativeButton(no, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){

            }
        });
        return downloadDialog.show();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent intent){
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Content : " + contents + " Format : " + format, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                openCreateEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCreateEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }
}
