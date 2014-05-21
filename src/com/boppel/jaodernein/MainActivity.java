package com.boppel.jaodernein;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wielangenoch.R;

public class MainActivity extends ActionBarActivity implements OnClickListener {
   
   private Button uebersicht;
   private Button maker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		if (savedInstanceState == null) { 
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}

		uebersicht = (Button) findViewById(R.id.zurUebersicht);
		maker      = (Button) findViewById(R.id.zumMaker);
		
		uebersicht.setOnClickListener(this);
		maker.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View view) {
	   
	   switch(view.getId()) {
	      case R.id.zumMaker:
	      //Starte CountdownMakerActivity
	      Intent i = new Intent(this, CountdownMakerActivity.class);
	      startActivity(i);
	      break;
	      
	      case R.id.zurUebersicht:
	      // Starte CountdownListActivity
	      Intent listActivity = new Intent(this, CountdownListActivity.class);
	      startActivity(listActivity);
	      break;
	   }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed(){
	   finish();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
		
		
	}

}
