package com.boppel.jaodernein;

import java.util.Calendar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wielangenoch.R;

public class CountdownActivity extends ActionBarActivity {

	public static boolean isTrue;
	public static String userFrage;
	public String grosseAntwort;
	
	public static float userDeadline;
	

	// String arrays mit blöden Sätzen als Pool
   public static String[] dauertNochPool = new String [10];
   public static String[] schonGeschehenPool = new String[10];
   private Bundle zielBundle;
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}		
		
		zielBundle = getIntent().getExtras();
		
      // Die Userfrage und Userdeadline aus dem Intent übernehmen und anzeigen
      userFrage = zielBundle.getString("Frage");
      
      // userSetDayX muss nun den Wert der deadline annehmen
      userDeadline = zielBundle.getFloat("Zeit");
	    
      // Die Pools mal füllen
		
      dauertNochPool[0] = "dauert noch ";
      dauertNochPool[1] = "noch ";
      dauertNochPool[2] = "nur noch ";
      dauertNochPool[3] = "warte lieber noch ";
      dauertNochPool[4] = "nicht mehr lange.  ";
      dauertNochPool[5] = "warte noch ";
      dauertNochPool[6] = "dauert immer noch ";
      dauertNochPool[7] = "erst in ";
      dauertNochPool[8] = "sind nur noch ";
      dauertNochPool[9] = "quasi in ";
      
      schonGeschehenPool[0] = "seit ";
      schonGeschehenPool[1] = "schon seit ";
      schonGeschehenPool[2] = "bereits vor ";
      schonGeschehenPool[3] = "vor ";
      schonGeschehenPool[4] = "schon ";
      schonGeschehenPool[5] = "geil oder? Schon seit ";
      schonGeschehenPool[6] = "bereits ";
      schonGeschehenPool[7] = "konntest du dir das nicht merken? seit ";
      schonGeschehenPool[8] = "allerdings schon seit ";
      schonGeschehenPool[9] = "vor ";
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.countdown, menu);
		return true;
	}
	
	@Override
	public void onBackPressed(){
	   
	   // Falle in die CountdownListActivity, schicke Intent mit den Eingaben des Users mit
	   Intent backToMainIntent = new Intent(this, CountdownListActivity.class);
	   backToMainIntent.putExtras(zielBundle);
	   startActivity(backToMainIntent);
		finish();
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

	public static class PlaceholderFragment extends Fragment implements
			View.OnClickListener {

		Button button;
		TextView InfoTxtAnzeige;
		TextView Frage;

		private TextView mNein;
		private View mLoadingIndicator;

		private float differenz;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_countdown,
					container, false);
			
			//Den ganzen Mist belogen lasani

			button = (Button) rootView.findViewById(R.id.gieftime);
			button.setOnClickListener(this);
			
			Frage = (TextView) rootView.findViewById(R.id.frage);
			Frage.setText(userFrage);
			
			InfoTxtAnzeige = (TextView) rootView.findViewById(R.id.grossantwort);

			mNein = (TextView) rootView.findViewById(R.id.antwort);
			mLoadingIndicator = rootView.findViewById(R.id.loading_spinner);

			// NEIN/JA nicht anzeigen
			mNein.setAlpha(0f);
			mNein.setVisibility(View.VISIBLE);
			
			// grosseAntwort nicht anzeigen lassen
			InfoTxtAnzeige.setAlpha(0f);
			InfoTxtAnzeige.setVisibility(View.VISIBLE);

			// //Indicator nicht anzeigen
			mLoadingIndicator.setAlpha(0f);
			mLoadingIndicator.setVisibility(View.VISIBLE);

			
			//hilf = (int) Math.round(wieLangeNochTage());

			return rootView;
		}
		
		public void onClick(View v) {

		   switch (v.getId()) {
		      case R.id.gieftime:
               mNein.setAlpha(0f);
               //
               InfoTxtAnzeige.setAlpha(0f);
               //
               mLoadingIndicator.setAlpha(1f);
               mLoadingIndicator.setVisibility(View.VISIBLE);
               mLoadingIndicator.animate().setDuration(5000);

               // Crossfade
               crossFade();
         
               differenz = wieLangeNochSekunden(userDeadline);
               
               String datumErgebnis = sec2date(differenz);
               int randomNumber = (int) (Math.random()*9);
               
               if(isTrue) {
                  
                  // POSITIV
                  mNein.setText("Ja.");
                  
                  String randomShit = schonGeschehenPool[randomNumber];
                  InfoTxtAnzeige.setText(randomShit + datumErgebnis);
                  
               }
               else {
                  
                  // NEGATIV
                  mNein.setText("Nein.");
                  String randomShizzle = dauertNochPool[randomNumber];
                  InfoTxtAnzeige.setText(randomShizzle + datumErgebnis);
               }
               break;
            default:
               break;
         }
			
		}

		public void crossFade() {

			mLoadingIndicator.animate().alpha(0f).setDuration(800)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoadingIndicator.setVisibility(View.INVISIBLE);
							mNein.animate().alpha(1f).setDuration(3000).setListener(null);
							InfoTxtAnzeige.animate().alpha(1f).setDuration(4000);
						}
					});

		}
	}
	
	public static void setFrage(String oFrage){
		userFrage = oFrage;
	}
	               
	public static float wieLangeNochSekunden(float deadline) {

		Calendar heute = Calendar.getInstance();
		
		float diffInMillis =  deadline - heute.getTimeInMillis();

		if (diffInMillis > 0)
			isTrue = false;
		else
			isTrue = true;

		float result = diffInMillis / 1000;
		
		return result;
	}
	
	public static String sec2date(float sec){
		@SuppressWarnings("unused")
      String result = null;
		String error ="Das ist ein Fehler. Bitte Felix hauen";
		
		float copyOfSec = sec;
		
		float minutesInSec	= 60;
		float hoursInSec	= minutesInSec * 60;
		float daysInSec		= hoursInSec * 24;
		
		float days = sec / daysInSec;
		sec = sec % daysInSec;
		
		float hours = sec / hoursInSec;
		sec = sec % hoursInSec;
		
		float minutes = sec / minutesInSec;
		sec = sec % minutesInSec;
		
		int roundedDays    = (int) Math.round(days); 
		int roundedHours   = (int) Math.round(hours);
		int roundedMinutes = (int) Math.round(minutes);
		
		// Abfragen, wenn ereignis noch in der Zukunft liegt
		//1 tag = 86400s
		if(copyOfSec >= 86400) {
			// only display X days left
			StringBuffer sb0 = new StringBuffer();
			
			if(roundedDays == 1){
				sb0.append(String.valueOf(roundedDays) + " Tag");
				return result = sb0.toString();
			} 
			else {
				sb0.append(String.valueOf(roundedDays) + " Tage");
				return result = sb0.toString();
			}
		}
		if(copyOfSec < 86400 && copyOfSec > 3600) {
			StringBuffer sb666 = new StringBuffer();
			sb666.append(String.valueOf(roundedHours) + " Stunden und " + String.valueOf(roundedMinutes) + " Minuten");
			return result = sb666.toString();
		}
		//1 Stunde = 3600s
		if(copyOfSec < 3600 && copyOfSec >= 0) {
			// only display XX minutes left
			StringBuffer sb1 = new StringBuffer();
			sb1.append(String.valueOf(roundedMinutes) + " Minuten");
			return result = sb1.toString();
		}
		
		// Abfragen, wenn Ereignis bereits stattgefunden hat
	   if(copyOfSec <= -86400) {
	         //only display since XX days
	         StringBuffer sb4 = new StringBuffer();
	       
	         if(roundedDays == 1) {
	            sb4.append(String.valueOf((roundedDays * -1)) + " Tag");
	            return result = sb4.toString();
	         } else {
	            sb4.append(String.valueOf(roundedDays) + " Tagen");
	         }
	   }
	   if(copyOfSec < -3600 && copyOfSec > -86400) {
	         // only display since XX hours and XX minutes
	         StringBuffer sb3 = new StringBuffer();
	         sb3.append(String.valueOf((roundedHours *-1)) + " Stunden und " + String.valueOf(roundedMinutes) + " Minuten");
	         return result = sb3.toString();
	   }
		if(copyOfSec > -3600 && copyOfSec <= 0) {
		   //only display since XX minutes
		   StringBuffer sb2 = new StringBuffer();
		   sb2.append(String.valueOf((roundedMinutes *-1)) + " Minuten");
		   return result = sb2.toString();
		}
		else {
		   return error;
		}
	}
}
