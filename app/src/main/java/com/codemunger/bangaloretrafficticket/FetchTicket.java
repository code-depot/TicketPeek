package com.codemunger.bangaloretrafficticket;

import android.net.Uri;
import android.os.AsyncTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.net.Uri.Builder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by android on 2/22/16.
 */
public class FetchTicket {

   private static String TAG =  "FetchTicket";

   public static  List<Ticket> fetchTicketList(String query)   throws IOException {

       String plate = query;
       List<Ticket> ticketList = null;
       String url =  Uri.parse("http://198.74.57.119:5000/").buildUpon().appendPath("service").
               appendPath("traffic.wsgi").appendQueryParameter("plate", plate).build().toString();

       HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
       try {

           InputStream in =  connection.getInputStream();
           if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
               throw  new IOException(connection.getResponseMessage() +
                       ": with " +
                       url);
           }
           ByteArrayOutputStream bufferOutput = new ByteArrayOutputStream();
           byte [] buffer = new byte[1024];
           int bytesRead = 0;
           while((bytesRead = in.read(buffer)) > 0) {
               bufferOutput.write(buffer,0,bytesRead);

           }
           bufferOutput.close();

           ticketList = getTicketList(bufferOutput.toString());
       }
       catch (Exception e) {
           Log.d(TAG,"Exception:" + e.getMessage());
       }
       finally {
           connection.disconnect();
       }
       return ticketList;
    }


    public static List<Ticket> getTicketList(String json) throws JSONException {

        ArrayList<Ticket> ticketList  = new ArrayList<>();
        JSONArray ticketArray = new JSONArray(json);
        for(int i = 0; i < ticketArray.length(); ++i) {
            Ticket ticket = new Ticket();
            JSONObject ticketObject = ticketArray.getJSONObject(i);
            ticket.setAmount(ticketObject.getString("amount"));
            ticket.setViolationType(ticketObject.getString("violation-type"));
            ticket.setNotice(ticketObject.getString("notice-number"));
            ticket.setDate(ticketObject.getString("date-time"));
            ticketList.add(ticket);
        }
        return ticketList;
    }
}
