package com.burakgomec.wordreminder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.TooManyRequestsException;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Connection { //Singleton Design Pattern

    IamAuthenticator authenticator;
    LanguageTranslator languageTranslator;
    TranslateOptions translateOptions;
    String translatedWord;

    public HashMap<String,String> modals;
    public ArrayList<String> languages,languagesCode;

    private static Connection connection;


    public static Connection getInstance() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    private Connection() {
	//https://cloud.ibm.com/apidocs/language-translator?code=java
        authenticator = new IamAuthenticator("Your Api Key");
        languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl("Service URL");
        modals = new HashMap<>();
        languages = new ArrayList<>();
        languagesCode = new ArrayList<>();
    }


    public void getWord(String word, String lang1, String lang2, FragmentActivity activity, TextView textView,View view) {
        try {
            translateOptions = new TranslateOptions.Builder()
                    .addText(word)
                    .modelId(lang1 +"-"+ lang2)
                    .build();
            languageTranslator.translate(translateOptions).enqueue(new ServiceCallback<TranslationResult>() {
                @Override
                public void onResponse(Response<TranslationResult> response) {
                    translatedWord = response.getResult().getTranslations().get(0).getTranslation();
                    if(translatedWord.length() == 0){
                        translatedWord = activity.getString(R.string.noResultsFound);
                        activity.runOnUiThread(() -> Snackbar.make(view,translatedWord, Snackbar.LENGTH_SHORT).show());
                    }
                    else{
                        activity.runOnUiThread(() -> textView.setText(translatedWord));
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    translatedWord = activity.getString(R.string.modelNotFound);
                    activity.runOnUiThread(() -> Snackbar.make(view,translatedWord, Snackbar.LENGTH_SHORT).show());
                }
            });
        }
        catch (TooManyRequestsException e){
            activity.runOnUiThread(() -> Snackbar.make(view,R.string.tooManyRequestException, Snackbar.LENGTH_SHORT).show());
        }
        catch(RuntimeException e){
            activity.runOnUiThread(() -> Snackbar.make(view,R.string.checkYourInternetConnection, Snackbar.LENGTH_SHORT).show());
        }
        catch (NoClassDefFoundError e){
            activity.runOnUiThread(() ->Snackbar.make(view, R.string.noClassException,Snackbar.LENGTH_SHORT).show());
        }
        catch (Exception e) {
            activity.runOnUiThread(() -> Snackbar.make(view,R.string.generalException, Snackbar.LENGTH_SHORT).show());
        }
    }


    public void fillLangModals(Activity activity){
        modals.put("en","tr,de,fr,fr-CA,it,pt,zh-TW,hr,hu,cnr,id,zh,ja,nl,fi,ga,gu,pl,da,lt,lv,uk,ur,ml," +
                "ms,mt,el,es,et,nb,vi,ne,ro,ru,bg,bn,bs,sk,sl,sr,ko,sv,ta,cs,te,th,he,hi,cy"); //Ibm translation language modals
        modals.put("he","en"); modals.put("hi","en"); modals.put("pt","en"); modals.put("zh-TW","en"); modals.put("hr","en");
        modals.put("hu","en"); modals.put("cnr","en"); modals.put("id","en"); modals.put("zh","en"); modals.put("ja","en");
        modals.put("nl","en"); modals.put("fi","en"); modals.put("ga","en"); modals.put("gu","en"); modals.put("pl","en");
        modals.put("tr","en"); modals.put("da","en"); modals.put("lt","en"); modals.put("lv","en"); modals.put("uk","en");
        modals.put("fr-CA","en"); modals.put("ur","en");modals.put("ml","en"); modals.put("ms","en"); modals.put("mt","en"); modals.put("el","en"); modals.put("et","en"); modals.put("nb","en");
        modals.put("vi","en"); modals.put("ne","en"); modals.put("ro","en"); modals.put("ru","en"); modals.put("bg","en"); modals.put("bn","en");
        modals.put("bs","en"); modals.put("sk","en"); modals.put("sl","en"); modals.put("sr","en"); modals.put("ko","en");
        modals.put("sv","en"); modals.put("ta","en"); modals.put("cs","en"); modals.put("te","en"); modals.put("th","en"); modals.put("cy","en");
        modals.put("ca","es"); modals.put("eu","es"); modals.put("de","fr,it,en"); modals.put("es","fr,eu,en,ca"); modals.put("it","en,de"); modals.put("fr","en,de,es");
        languages.addAll(Arrays.asList(activity.getResources().getStringArray(R.array.languages)));
        languagesCode.addAll(Arrays.asList(activity.getResources().getStringArray(R.array.languagesCode)));
    }

}


