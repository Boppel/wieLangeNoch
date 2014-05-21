package com.boppel.jaodernein;

public class UserDataSaved {

   private String frageDesUsers;
   private float deadlineDesUsers;
   
   public UserDataSaved(String mfrageDesUsers, float mdeadlineDesUsers) {
      
      frageDesUsers    = mfrageDesUsers;
      deadlineDesUsers = mdeadlineDesUsers;
   }
   
   public String getFrage(){
      return frageDesUsers;
   }
   
   public void setFrage(String t){
      frageDesUsers = t;
   }
   
   public float getZeit(){
      return deadlineDesUsers;
   }
   
   public void setZeit(float t){
      deadlineDesUsers = t;
   }
   
   // Mit dieser Methode wird der String der Frage im Gridview angezeigt
   @Override
   public String toString(){
      return this.frageDesUsers;
   }
}
