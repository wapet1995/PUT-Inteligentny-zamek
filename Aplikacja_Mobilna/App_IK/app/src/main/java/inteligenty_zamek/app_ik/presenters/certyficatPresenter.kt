package inteligenty_zamek.app_ik.presenters

import android.content.Intent
import android.util.Log
import inteligenty_zamek.app_ik.API.HTTPRequestAPI
import inteligenty_zamek.app_ik.API.fileReadWriteApi
import inteligenty_zamek.app_ik.Views.GenerationCertyficatActivity
import inteligenty_zamek.app_ik.Views.ManagmentCertyficationUserActivity
import inteligenty_zamek.app_ik.Views.Managment_certyficationActivity
import inteligenty_zamek.app_ik.Views.certyficatActivity
import inteligenty_zamek.app_ik.models.certyficatModel
import inteligenty_zamek.app_ik.models.GlobalContainer
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

/**
 * Created by Damian on 09.12.2017.
 */
class certyficatPresenter(val view: certyficatActivity)
{
    val model: certyficatModel
    init{
        val bundle = view.intent.extras
        if (bundle != null) {
            model= certyficatModel(view, bundle.getInt("position"))
        }
        else
        {
            model= certyficatModel(view, -1)
        }
    }
    fun isCertyficat():Boolean
    {
        if (model.certyficat!=null)
        {return true}
        return false
    }
    fun getCertyficatText():String
    {
        return model.getCertyficatText()
    }
    fun getCertyficatExpiryDateText():String
    {
    return "Czas wygasniecia: \n" + model.certyficat!!.to.replace("T","  ")
    }
    fun getCertyficatInfoText():String
    {
        return  "Klucz przypisany do użytkownika: \n" + model.certyficat!!.name + " " + model.certyficat!!.surname
    }
    private fun deleteCertyficatFromFile()
    {
        try {
            val readcertyficat = fileReadWriteApi
                    .readFromFile(model.login, view)
            val arrJson = JSONArray(readcertyficat)
            for (i in 0 until arrJson.length()) {
                val jsonObj = arrJson.getJSONObject(i)
                val k = jsonObj.getString("LOCK_KEY")
                if (k == model.certyficat!!.lok_key) {
                    arrJson.remove(i)
                    break
                }
            }
            GlobalContainer.getUser(view).addCertyficatList(arrJson)
            fileReadWriteApi.writeToFile(arrJson.toString(), view, model.login)

        } catch (e: Exception) { }
    }
    fun deleteCertyficat() {
        if (model.isLogin) {
            val toSend: HashMap<String, String> = HashMap()
            toSend.put("login", model.login)
            toSend.put("token", model.token)
            if(model.fromAdminPanel)
            {
                toSend.put("user_id",model.certyficat!!.id_user)
            }
            else
            {
                toSend.put("certificate_id",model.certyficat!!.idKey)
            }
            try {

              if(model.fromAdminPanel)
              {
                  HTTPRequestAPI(this, "http://" + model.ipaddres + ":8080/api/admin/deactivation_user_certificate/", "deleteResult", toSend).execute()
              }
                else
              {
                  HTTPRequestAPI(this, "http://" + model.ipaddres + ":8080/api/deactivation/", "deleteResult", toSend).execute()
              }
            } catch (e: Exception) { }


        } else {
            deleteCertyficatFromFile()
            val myIntent = Intent(view, Managment_certyficationActivity::class.java)
            view.startActivity(myIntent)
        }
    }

  fun deleteResult(response:String)
  {
      Log.i("yyyy",response)
      try {
          if ( JSONObject(response).getString("status") == "ok")
          {
              if(model.fromAdminPanel)
              {
                  val myIntent = Intent(view, ManagmentCertyficationUserActivity::class.java)
                  view.startActivity(myIntent)
                  view.finish()
              }
              else {
                  deleteCertyficatFromFile()
                  val myIntent = Intent(view, Managment_certyficationActivity::class.java)
                  view.startActivity(myIntent)
              }
          }
          else
          {
              view.showMessage("Nie powiodło się usunięcie certyfikatu")
          }
      } catch (e: Exception) { }
  }

    fun extendCertyficat()
    {
        if(model.fromAdminPanel|| model.isAdmin)
        {
            GlobalContainer.obj=model.certyficat
            val myIntent = Intent(view, GenerationCertyficatActivity::class.java)
            myIntent.putExtra("haveCertificate",true)
            view.startActivity(myIntent)
            view.finish()
        }
        else {
            val toSend: HashMap<String, String> = HashMap()
            toSend.put("login", model.login)
            toSend.put("token", model.token)
            toSend.put("lock_id", model.certyficat!!.id_lock)
            try {
                HTTPRequestAPI(this, "http://" + model.ipaddres + ":8080/api/request_new_certificate/", "extendResult", toSend).execute()
            } catch (e: Exception) {

            }
        }
    }

    fun extendResult(response:String) {
        try {

            if (JSONObject(response).getString("status") == "ok") {
                view.showMessage("wysłano prośbę o przedłużenie certyfikatu")
            } else {
                view.showMessage("Nie powiodło się przedłużenie certyfikatu")
            }
        } catch (e: Exception) { }
    }



}


