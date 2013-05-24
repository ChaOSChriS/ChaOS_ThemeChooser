package com.chaosware.chaos.theme.chooser;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.androidhive.xmlparsing.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class SingleMenuItemActivity  extends Activity {
	 


	static final String KEY_NAME = "name";
	static final String KEY_COST = "cost";
	static final String KEY_DESC = "description";
	static final String KEY_DL = "dl";
	public static ProgressDialog mProgressDialog;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);

        Intent in = getIntent();
        
      
        

        final String name = in.getStringExtra(KEY_NAME);
        String cost = in.getStringExtra(KEY_COST);
        final String description = in.getStringExtra(KEY_DESC);
        final String download = in.getStringExtra(KEY_DL);
  

       
        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
        .execute(cost);

        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblCost = (TextView) findViewById(R.id.cost_label);
        TextView lblDesc = (TextView) findViewById(R.id.description_label);
        TextView lblDL = (TextView) findViewById(R.id.download);

        
        lblName.setText(name);
        lblCost.setText(cost);
        lblDesc.setText(description);
      
        
        
        
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
			public void onClick(View v) {
            	Toast.makeText(getApplicationContext(), "Starte Download: " + download , Toast.LENGTH_SHORT).show();
            	
            	String url = download;
            	DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            	request.setDescription(description);
            	request.setTitle("Downloading: " + name);
            	request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name+".zip");
            	DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            	final long downloadReference = manager.enqueue(request);
            	IntentFilter filter = new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            	BroadcastReceiver receiver = new BroadcastReceiver() { 
            	    @Override public void onReceive( Context context, Intent intent) { 
            	      long reference = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, -1); 
            	       if (downloadReference == reference) { 
            	    	  
            	            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            	            alert.setTitle("Download Finished"); //Set Alert dialog title here
            	            alert.setMessage("Reboot to Recovery?"); //Message here
            	 
            	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            	            public void onClick(DialogInterface dialog, int whichButton) {
            	            
            	            	  Runtime runtime = Runtime.getRuntime();
            	                  Process proc = null;
            	                  OutputStreamWriter osw = null;
            	                  String command="/system/bin/reboot recovery";

            	                  try { 

            	                      proc = runtime.exec("su");
            	                      osw = new OutputStreamWriter(proc.getOutputStream());
            	                                          osw.write(command);
            	                              osw.flush();
            	                      osw.close();

            	                  } catch (IOException ex) {
            	                      ex.printStackTrace();
            	                  } finally {
            	                      if (osw != null) {
            	                          try {
            	                              osw.close();
            	                          } catch (IOException e) {
            	                              e.printStackTrace();                    
            	                          }
            	                      }
            	                  }
            	                  try {
            	                      if (proc != null)
            	                          proc.waitFor();
            	                  } catch (InterruptedException e) {
            	                      e.printStackTrace();
            	                  }
            	                 
            	                  if (proc.exitValue() != 0) {
            	                              }
            	 
            	              }
            	            });
            	 
            	            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            	              public void onClick(DialogInterface dialog, int whichButton) {
            	                // Canceled.
            	                  dialog.cancel();
            	              }
            	            });
            	            AlertDialog alertDialog = alert.create();
            	            alertDialog.show();

            	        } 
            	     } 
            	}; 
            	registerReceiver( receiver, filter);
            	
            }
        });
      
}
	
	
    }

