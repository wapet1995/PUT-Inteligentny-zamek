package inteligenty_zamek.app_ik.API;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import inteligenty_zamek.app_ik.Navigation.BaseActivity;
import inteligenty_zamek.app_ik.presenters.LoginPresenter;
import inteligenty_zamek.app_ik.presenters.Managment_certyficationPresenter;
import inteligenty_zamek.app_ik.presenters.RegisterPresenter;
import inteligenty_zamek.app_ik.sampledata.CertificationaskPresenter;


public class HTTPRequestAPI extends
        AsyncTask<Void, Void, String> {

    String urlString;
    HashMap DataToSend;
    int apiNumber;
    Object presenter;

    public HTTPRequestAPI(Object presenter,String url,int apiNumber,HashMap DataToSend) {

        this.urlString=url;
        this.DataToSend=DataToSend;
        this.apiNumber=apiNumber;
        this.presenter=presenter;
    }


    @Override
    protected String doInBackground(Void... params) {

        String response = "";
        try {
            response = performPostCall(urlString, DataToSend);
        } catch (Exception e) {
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {


        //API LOGIN  wyjatkowo przechodzi do kolejnego widoku
        switch(apiNumber)
        {
            case 0:
                BaseActivity ba=BaseActivity.class.cast(presenter);
                ba.logoutresponse(response);
                break;
            case 1:
                LoginPresenter lp= LoginPresenter.class.cast(presenter);
                lp.loginResult(response);
                break;
            case 2:
                RegisterPresenter rp=RegisterPresenter.class.cast(presenter);
                rp.registerResult(response);
                break;
            case 3:
                Managment_certyficationPresenter mp=Managment_certyficationPresenter.class.cast(presenter);
                mp.downloadResult(response);
                break;
            case 4:
                CertificationaskPresenter x=CertificationaskPresenter.class.cast(presenter);
                //x.downloadResult(response);
                break;
        }




    }

    public String performPostCall(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.i("BBB",String.valueOf(responseCode));
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
