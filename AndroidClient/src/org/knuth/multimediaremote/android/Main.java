package org.knuth.multimediaremote.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.knuth.multimediaremote.android.client.Client;

/**
 * Main Activity for the application.
 */
public class Main extends Activity{

    // TODO Try the connection in a AsyncTask and store values when okay.
    // TODO Create new class for the connection and their values
    // TODO If working, disable EditText and Connect-Button and enable others.
    // TODO Create filter/mask for EditText to enter IP-Address

    private Client client;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    /**
     * Called by all the control-buttons to send their given
     *  action to the connected server.
     * @param v
     */
    public void executeAction(View v){
        String tag = (String) v.getTag();
        try {
            Client.Actions action = Client.Actions.valueOf(tag);
            client.addAction(action);
        } catch (IllegalArgumentException e){
            Log.e("OnlyLog", "Unknown action: "+tag);
        }
    }

    /**
     * Called by the "connect"-button to create a Client to talk
     *  to and enable the control-buttons.
     * @param v
     */
    public void createClient(View v){
        // Get the inserted values:
        final EditText ip_v = (EditText) findViewById(R.id.ip_adress);
        final EditText port_v = (EditText) findViewById(R.id.port);
        final Button connect_v = (Button) findViewById(R.id.connect);
        String ip = ip_v.getText().toString();
        // Disable Views:
        ip_v.setEnabled(false);
        port_v.setEnabled(false);
        connect_v.setEnabled(false);
        // Do some tests:
        int port = 0;
        try{
            port = Integer.parseInt( port_v.getText().toString() );
        } catch (NumberFormatException e){
            Log.e("OnlyLog", "Parsing the Port: ", e);
        }
        if (ip.length() <= 0 || port == 0){
            Log.d("OnlyLog", "Can't use data...");
            return;
        }
        // Create Client:
        client = new Client(ip,port);
        enableControls();
    }

    /**
     * Enables the control-buttons on the Activity.
     */
    private void enableControls(){
        findViewById(R.id.pause_play).setEnabled(true);
        findViewById(R.id.stop).setEnabled(true);
        findViewById(R.id.next).setEnabled(true);
        findViewById(R.id.previous).setEnabled(true);
        findViewById(R.id.vol_low).setEnabled(true);
        findViewById(R.id.vol_up).setEnabled(true);
        findViewById(R.id.mute).setEnabled(true);
    }

}
