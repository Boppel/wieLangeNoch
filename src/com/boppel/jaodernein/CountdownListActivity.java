package com.boppel.jaodernein;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wielangenoch.R;

public class CountdownListActivity extends Activity {

   // public ListView listView;
   public GridView gridView;

   private Bundle userBundle;

   // Zeug für die SharedPreferences
   public static final String UNIQUEUSERINPUT = "uniqueInput";
   public int anzahlUserFragen;
   public SharedPreferences userInput;
   
   // Adapter
   public MyCustomAdapter adapter;
   
// Define an Array that holds UserDataSaved Objects
   public ArrayList<UserDataSaved> values = new ArrayList<UserDataSaved>();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_countdown_list);

      // Erzeuge sharedPreferences
      userInput = getSharedPreferences(UNIQUEUSERINPUT, 0);

      // hole die Anzahl der gegenwärtigen Userfragen aus den SharedPrefs
      anzahlUserFragen = userInput.getInt("Counter", 0);

      if (anzahlUserFragen > 0) {
         // Grid füllen. For schleife geht den counter durch und schiebt es dem Grid in den Poppes
         // Packe die UserDataSaved Objekte in das Array
         for (int i = 0; i < anzahlUserFragen; i++) {
            // Erzeuge aus den SharedPreferences soviele UserDatasavedObjekte, wie es gibt und lege diese in das Array
            
            UserDataSaved paar = new UserDataSaved(userInput.getString("Frage" + i, ""), userInput.getFloat("Zeit" + i, -666));
            values.add(paar);
         }
         // Hinzufügen von '+' an der letzten Stelle des Arrays
         UserDataSaved ghettoAdd = new UserDataSaved("+", 0);
         values.add(ghettoAdd);

      } else {
         anzahlUserFragen = 0;
         UserDataSaved ghettoAdd = new UserDataSaved("+", 0);
         values.add(ghettoAdd);
      }

      // Get ListView object from xml
      gridView = (GridView) findViewById(R.id.grid_view);

      // Define a new Adapter
      adapter = new MyCustomAdapter(this, values);

      // Assign adapter to ListView
      gridView.setAdapter(adapter);
      
      // Register for ContextMenu
      registerForContextMenu(gridView);

      // GridView Item Click Listener
      gridView.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // GridView Clicked item index
            int itemPosition = position;

            // GridView Clicked item value
            UserDataSaved itemValue = (UserDataSaved) gridView.getItemAtPosition(position);

            // Show Alert
            Toast.makeText(getApplicationContext(),
                     "Position:" + itemPosition + "  ListItem: " + itemValue.getFrage() + "Zeit:" + itemValue.getZeit(), Toast.LENGTH_LONG).show();

            // Erstelle ein Bundle
            userBundle = new Bundle();
            // Extrahiere die jeweilige Frage und deadline
            userBundle.putString("Frage", itemValue.getFrage());
            userBundle.putFloat("Zeit", itemValue.getZeit());

            if (adapter.getItemViewType(itemPosition) == 0) {
               Intent countdown = new Intent(CountdownListActivity.this, CountdownActivity.class);
               countdown.putExtras(userBundle);
               startActivity(countdown);
            } else {
               Intent makerIntent = new Intent(CountdownListActivity.this, CountdownMakerActivity.class);
               startActivity(makerIntent);
            }
         }
      });
   }
   
   
   // this. provides a context menu for the selected item (on LongClick)
   @Override
   public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
      if(view.getId() == R.id.grid_view) {
         AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
         UserDataSaved itemValue = (UserDataSaved) gridView.getItemAtPosition(info.position);
         getMenuInflater().inflate(R.menu.item_menu, menu);
         menu.setHeaderTitle(itemValue.getFrage());
      }
   }
   
   // Handle the selected MenuItem
   @Override
   public boolean onContextItemSelected(MenuItem item) {
      
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
      UserDataSaved userItem = (UserDataSaved) gridView.getItemAtPosition(info.position);
      
      switch(item.getItemId()) {
         case R.id.bearbeiten:
            
            //TODO:
            // 1) öffne CountdownMakerActivity und zeige die Frage und das Datum bereits an
            // 2) nach dem klick auf den Button wird der entsprechende Eintrag in der SharedPreference geupdated,
            //    und man landet in der CountdownActivity
            
            Toast.makeText(this, "bearbeiten", Toast.LENGTH_LONG).show();
            break;
         case R.id.loeschen:
            // wichtig beim löschen: Item muss aus der GridView gelöscht werden und zusätzlich aus den Shared Preferences!
            
            // Setzte anzahlUserfragen um 1 herunter, da wir gerade eine Frage gelöscht haben
            anzahlUserFragen = anzahlUserFragen -1;
            // Gebe den neuen Wert in die Shared Preferences
            userInput.edit().putInt("Counter", anzahlUserFragen).commit();
            
            // lösche item aus den shared prefs
            if (userInput.contains("Frage"+info.position)){
               
               // lösche item aus der ArrayList
               values.remove(info.position);
               
               // lösche item aus der GridView
               adapter.remove(userItem);
               adapter.notifyDataSetChanged();
               
               // lösche komplette SharedPreference, da alle relevanten Daten in values und in der GridView stecken
               SharedPreferences.Editor editor = userInput.edit();
               editor.clear();
               // fülle die shared preferences mit den daten aus der ArrayList
               for (int i=0; i<values.size() - 1; i++) {
                  editor.putString("Frage"+i, values.get(i).getFrage());
                  editor.putFloat("Zeit"+i, values.get(i).getZeit());
               }
               // gebe die aktuelle Anzahl der Userfragen in die SharedPreferences
               editor.putInt("Counter", anzahlUserFragen);
               // erzeuge die neuen shared prefs
               editor.commit();
            }
            Toast.makeText(this, userItem.getFrage() + " wurde gelöscht", Toast.LENGTH_LONG).show();
            break;
      }
      return true;
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {

      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.countdown_list, menu);
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
   public void onBackPressed() {
      Intent back2main = new Intent(CountdownListActivity.this, MainActivity.class);
      startActivity(back2main);
      finish();
   }

   private static class MyCustomAdapter extends ArrayAdapter<UserDataSaved> {

      private final LayoutInflater layoutInflater;

      public MyCustomAdapter(final Context context, final List<UserDataSaved> values) {
         super(context, android.R.layout.simple_list_item_1, values);

         layoutInflater = LayoutInflater.from(context);
      }

      @Override
      public int getViewTypeCount() {
         return 2;
      }

      @Override
      public int getItemViewType(int position) {
         return position != getCount()-1 ? 0 : 1;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {

         View view = convertView;

         if (view == null) {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
         }

         TextView text = (TextView) view.findViewById(android.R.id.text1);

         text.setText(getItem(position).getFrage());

         return view;
      }
   }
}