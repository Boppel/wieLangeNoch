package com.boppel.jaodernein;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.wielangenoch.R;

public class CountdownMakerActivity extends Activity {

	static String Frage;
	
	// Shared Preferences
	public static SharedPreferences uniqueUserInput;
	public static final String UNIQUEUSERINPUT = "uniqueInput";
	public int counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown_maker);
		

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}		 
		
		uniqueUserInput = getSharedPreferences(UNIQUEUSERINPUT, 0);
		
		if(uniqueUserInput.getInt("Counter", -1) > 0) {
		   counter = uniqueUserInput.getInt("Counter", -1);
		} else {
		   counter = 0;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.countdown_maker, menu);
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

	public static class PlaceholderFragment extends Fragment {

		public static Button absenden;
		public static EditText dateField;
		public static EditText timeField;
		public static EditText frage;
		
		public static Calendar stichTest = Calendar.getInstance();
		public static Calendar deadline = Calendar.getInstance();
		
		public static int tmpYear, tmpMont, tmpDay, tmpHour, tmpMinute;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_countdown_maker,
					container, false);

			dateField = (EditText) rootView.findViewById(R.id.dateTextField);
		   timeField = (EditText) rootView.findViewById(R.id.timeTextField);
			absenden  = (Button)   rootView.findViewById(R.id.abschicken);
			frage     = (EditText) rootView.findViewById(R.id.frage);

			return rootView;
		}
	}

	// OnClick für Button,
	// Datum und Zeit vom User an CountdownActivity geben
	// neuen Intent machen und CountdownActivity starten
	public void onClick(View v) {
	   
	   float deadlineInMillis = 0.0f;
	   
	   // Setze Datum und Uhrzeit in den Calendar deadline
	   PlaceholderFragment.deadline.clear();
      PlaceholderFragment.deadline.set(PlaceholderFragment.tmpYear, 
                                       PlaceholderFragment.tmpMont, 
                                       PlaceholderFragment.tmpDay, 
                                       PlaceholderFragment.tmpHour, 
                                       PlaceholderFragment.tmpMinute);
      
      // Lese die Userfrage aus dem TextField in den String Frage
      Frage = PlaceholderFragment.frage.getText().toString();
      
      // Rechne die deadline in Millisekunden um, damit man diese in das Bundle packen kann
      deadlineInMillis = (PlaceholderFragment.deadline.getTimeInMillis());
	   
      // Erstelle Bundle das aus der Userfrage, und dem Datum in Millisekunden besteht
      Bundle CountdownBundle = new Bundle();
      CountdownBundle.putString("Frage", Frage);
      CountdownBundle.putFloat("Zeit", deadlineInMillis);
      
      // Erzeuge Intent, der die Daten übergibt und die CountdownActivity startet
      Intent startCountdownIntent = new Intent(CountdownMakerActivity.this, CountdownActivity.class);
      startCountdownIntent.putExtras(CountdownBundle);
      startActivity(startCountdownIntent);
      
      // Speichere Frage und Zeit in den SharedPreferences (uniquer Key)
      uniqueUserInput = getSharedPreferences(UNIQUEUSERINPUT, 0);
      uniqueUserInput.edit().putString("Frage" + counter, Frage).commit();
      uniqueUserInput.edit().putFloat("Zeit" + counter, deadlineInMillis).commit();
      //TODO: counter++ nach commit(), in der ListActivity dann ändern: getString("Frage" + (anzahlFragen - 1) in "Frage" + anzahlFragen
      counter++;
      uniqueUserInput.edit().putInt("Counter", counter).commit();
      
	}
	
	////////////////////////////////////////////
	// DATE PICKER ZEUGS + TIME PICKER ZEUGS //
	//////////////////////////////////////////
	
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			
		   // Zu aller erst mal das Ergebnis vom User in das TextField schreiben
			PlaceholderFragment.dateField.setText(new StringBuilder()
					.append(day).append(".").append(month + 1).append(".")
					.append(year).append(" "));

			// Speichere die Eingabe in den temporären Variablen
			PlaceholderFragment.tmpYear = year;
			PlaceholderFragment.tmpMont = month;
			PlaceholderFragment.tmpDay = day;
		}
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user

			// Ergebnis des Users in das TextField schreiben
			PlaceholderFragment.timeField.setText(new StringBuilder()
					.append(hourOfDay).append(":").append(minute));
			
			// Speichere die Eingabe des Users in den temorären Variablen
			PlaceholderFragment.tmpHour = hourOfDay;
			PlaceholderFragment.tmpMinute = minute;
			
		}
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

}
