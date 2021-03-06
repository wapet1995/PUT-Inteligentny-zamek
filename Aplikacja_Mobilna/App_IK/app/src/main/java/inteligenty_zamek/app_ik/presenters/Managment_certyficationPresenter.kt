package inteligenty_zamek.app_ik.presenters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import inteligenty_zamek.app_ik.API.HTTPRequestAPI
import inteligenty_zamek.app_ik.API.fileReadWriteApi
import inteligenty_zamek.app_ik.Views.CertificationaskActivity
import inteligenty_zamek.app_ik.Views.Managment_certyficationActivity
import inteligenty_zamek.app_ik.models.Managment_certyficationModel
import inteligenty_zamek.app_ik.models.GlobalContainer
import inteligenty_zamek.app_ik.Views.userCertyfikationListActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/**
 * Created by Damian on 28.11.2017.
 */
class Managment_certyficationPresenter( val view: Managment_certyficationActivity) {
    var model: Managment_certyficationModel=Managment_certyficationModel(view)



    fun goToCertifiationList()
    {
        val myIntent = Intent(view, userCertyfikationListActivity::class.java)
        view.startActivity(myIntent)
    }

    fun goToCertificationAsk()
    {
        val myIntent = Intent(view, CertificationaskActivity::class.java)
        view.startActivity(myIntent)
    }




    fun readfromfile(requestCode:Int,resultCode:Int,data: Intent)
    {
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            model.selectedfile = data.data
            model.arrJson = null
            try {
                val jObj = JSONObject(fileReadWriteApi.readFromFile(model.selectedfile!!.toString(),view))
                val readcertyficat = fileReadWriteApi.readFromFile(GlobalContainer.getUser(view).login,view)
                model.arrJson = JSONArray(readcertyficat)
                model.arrJson!!.put(jObj)

            } catch (e: Exception) {
            }
            fileReadWriteApi.writeToFile(model.arrJson!!.toString(), view, GlobalContainer.getUser(view).login)
        }

    }

    fun isLogin():Boolean
    {
        return model.islogin
    }

    fun setAdapter(): ArrayAdapter<String>
    {
        return model.adapter
    }

    fun getCertyficat()
    {
        val toSend: HashMap<String, String> = HashMap()
        toSend.put("login", model.login)
        toSend.put("token", model.token)
        try {
            Log.i("HHHH","http://" + model.ipaddres + ":8080/api/download/all_certifacate/");

            HTTPRequestAPI(this, "http://" + model.ipaddres + ":8080/api/download/all_certifacate/", "downloadResult", toSend).execute()
        } catch (e: Exception) { }

    }

    fun downloadResult(response:String)
    {
        try {
            val jObj: JSONObject = JSONObject(response)
            val arrJson = jObj.getJSONArray("data")

            fileReadWriteApi.writeToFile(arrJson.toString(), view, model.login)
            GlobalContainer.getUser(view).addCertyficatList(arrJson)

            view.showMessage("dodano certyfikaty")

        } catch (e: JSONException) {
            view.showMessage("brak certyfikatow")
            GlobalContainer.getUser(view).certyficateList=null
            fileReadWriteApi.writeToFile("", view, model.login)
    }

}

}

