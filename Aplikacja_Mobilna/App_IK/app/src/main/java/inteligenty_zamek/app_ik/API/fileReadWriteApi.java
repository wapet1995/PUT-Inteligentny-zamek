package inteligenty_zamek.app_ik.API;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Damian on 26.10.2017.
 */

public final class fileReadWriteApi {



    public static void writeToFile(String data, Context context, String filepath) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filepath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {

        }
    }

    public static void writeToFile(String data, Context context, String filepath,Boolean flag) {
        try {


            File dir = new File (filepath);
            dir.mkdirs();
            File file = new File(dir, "klucz");
            FileOutputStream f = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(f);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {

        }
    }

    // funkcja odpowiedzialna za odczytywanie z pliku
    public static String readFromFile( String filepath, Context context) {

        String ret = "";

        try {
            InputStream inputStream =  context.openFileInput(filepath);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            return "NULL";
        } catch (IOException e) {
        }

        return ret;
    }


    public static String readFromFile( String filepath, Context context,Boolean a){
        String ret = "";

        try {

            File dir = new File (filepath);


            InputStream inputStream =  new FileInputStream(dir);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            return "NULL";
        } catch (IOException e) {
        }

        return ret;
    }

}
