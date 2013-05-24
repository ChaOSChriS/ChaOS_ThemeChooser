package com.chaosware.chaos.theme.chooser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.androidhive.xmlparsing.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class AndroidXMLParsingActivity extends ListActivity {

	// All static variables
	static final String URL = "server/theme database xml";
	// XML node keys
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_NAME = "name";
	static final String KEY_COST = "cost";
	static final String KEY_DESC = "description";
	static final String KEY_DL = "dl";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        String line = "";
        String CorrectDevice = "YP-GI1";
        try {
         Process ifc = Runtime.getRuntime().exec("getprop ro.product.name");
         BufferedReader bis = new BufferedReader(new InputStreamReader(ifc.getInputStream()));
         line = bis.readLine();
        } catch (java.io.IOException e) {
        }

        if (!line.equals(CorrectDevice)) {
        final AlertDialog.Builder IncorrectDevice = new AlertDialog.Builder(this);
        IncorrectDevice.setMessage("You are probably not running this application on the Galaxy Player 4.2 (YP-GI1). Please note that this application is only meant to run on the Galaxy Player 4.2 (YP-GI1).");
        IncorrectDevice.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			finish();
			
			}});
        IncorrectDevice.show();
        } 
		
		
		ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); 
		Document doc = parser.getDomElement(xml); 
		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		for (int i = 0; i < nl.getLength(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			map.put(KEY_ID, parser.getValue(e, KEY_ID));
			map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
			map.put(KEY_COST, parser.getValue(e, KEY_COST));
			map.put(KEY_DESC, parser.getValue(e, KEY_DESC));
			map.put(KEY_DL, parser.getValue(e, KEY_DL));
			menuItems.add(map);
		}

		ListAdapter adapter = new SimpleAdapter(this, menuItems,
				R.layout.list_item,
				new String[] { KEY_NAME, KEY_DESC, KEY_COST, KEY_DL }, new int[] {
						R.id.name, R.id.desciption, R.id.cost, R.id.download });

		setListAdapter(adapter);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
				String cost = ((TextView) view.findViewById(R.id.cost)).getText().toString();
				String description = ((TextView) view.findViewById(R.id.desciption)).getText().toString();
				String download = ((TextView) view.findViewById(R.id.download)).getText().toString();

				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				in.putExtra(KEY_NAME, name);
				in.putExtra(KEY_COST, cost);
				in.putExtra(KEY_DESC, description);
				in.putExtra(KEY_DL, download);
				startActivity(in);

			}
		});
	}
}