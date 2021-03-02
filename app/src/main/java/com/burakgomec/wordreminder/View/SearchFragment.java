package com.burakgomec.wordreminder.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.burakgomec.wordreminder.Connection;
import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.Model.TranslatedWord;
import com.burakgomec.wordreminder.R;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    EditText editText;
    String editString,langCode,langCode2,language1TextBuffer;
    ImageView changeLanguage,imageViewStar,clearEditText;
    TextView textViewResult, textViewLang1, textViewLang2;
    Button lang1,lang2;
    Boolean control,imageControl,saved;
    ArrayList<String> bufferArrayLanguage2,alertDialogLanguage;
    String[] regexLanguage2;
    Integer index;
    Database database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.editTextSearch);
        textViewResult = view.findViewById(R.id.textViewResult);
        changeLanguage = view.findViewById(R.id.changeLanguage);
        lang1 = view.findViewById(R.id.firstLanguage);
        lang2 = view.findViewById(R.id.secondLanguage);
        textViewLang1 = view.findViewById(R.id.textViewLang1);
        textViewLang2 = view.findViewById(R.id.textViewLang2);
        imageViewStar = view.findViewById(R.id.imageViewStar);
        clearEditText = view.findViewById(R.id.clearEditText);

        bufferArrayLanguage2 = new ArrayList<>();
        alertDialogLanguage = new ArrayList<>();

        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);

        control = false; imageControl = true;



        langCode = "tr"; langCode2 = "en";

        textViewResult.setMovementMethod(new ScrollingMovementMethod()); // scroll in text view

        database = new Database(view.getContext());
        database.getWords();

        setLanguageClickListeners();
        clearEditText();
        setChangeLanguage();

        imageViewStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textViewResult.getText().toString().equals("")){
                    if(imageControl){
                        imageViewStar.setImageResource(R.drawable.ic_baseline_star_rate_24);
                        database.saveWords(editText.getText().toString(),textViewResult.getText().toString());//insert db
                        imageControl = false;
                    }
                    else{
                        imageControl = true;
                        imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                        database.deleteWordWithString(textViewResult.getText().toString());
                    }
                }
            }
        });





        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                saved = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    editString = editText.getText().toString();
                    if((!editString.equals("")) && (!lang2.getText().equals("Bir dil seçiniz"))){
                        //Linear search in database. O(N) time complexity
                            for(TranslatedWord translatedWord : database.translatedWordArrayList){
                              if(editString.equals(translatedWord.getFirstWord())){
                                  imageViewStar.setImageResource(R.drawable.ic_baseline_star_rate_24);
                                  imageControl = false;
                                  textViewResult.setText(translatedWord.getSecondWord());
                                  saved = true;
                                   break; } }
                            if(!saved){
                                imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                                imageControl = true;
                                Connection.getInstance().getWord(editString,langCode,langCode2,getActivity(), textViewResult);
                            }
                    }
                    else if(lang2.getText().equals("Bir dil seçiniz")){
                        Toast.makeText(getContext(),"Çevirilecek dili seçiniz", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(getContext(),"Bir kelime giriniz", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setChangeLanguage(){
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!control && !lang2.getText().equals("Bir dil seçiniz")){
                    //View
                    language1TextBuffer = lang1.getText().toString();
                    lang1.setText(lang2.getText());
                    textViewLang1.setText(lang2.getText());
                    textViewLang2.setText(language1TextBuffer);
                    lang2.setText(language1TextBuffer);

                    //Ibm service
                    language1TextBuffer = langCode;
                    langCode = langCode2;
                    langCode2 = language1TextBuffer;

                    editString = textViewResult.getText().toString();
                    if(!editString.equals("")){
                        Connection.getInstance().getWord(editString,langCode,langCode2,getActivity(), textViewResult);
                        editText.setText(editString);
                    }

                }
            }
        });

    }


    private void clearEditText(){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    clearEditText.setVisibility(View.VISIBLE);
                }
                else{
                    clearEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        clearEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }



    private void setLanguageClickListeners(){

        lang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(control){ // clicked first language button
                    alertDialogLanguage.clear();
                    if(bufferArrayLanguage2.size() == 1){
                        index = Connection.getInstance().languagesCode.indexOf(bufferArrayLanguage2.get(0));
                        alertDialogLanguage.add(Connection.getInstance().languages.get(index));
                    }
                    else{
                        for (String s : regexLanguage2) {
                            index = Connection.getInstance().languagesCode.indexOf(s);
                            alertDialogLanguage.add(Connection.getInstance().languages.get(index));
                        }
                    }
                    control = false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Şu dile çevir:");
                builder.setItems(alertDialogLanguage.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lang2.setText(alertDialogLanguage.get(which));
                        textViewLang2.setText(alertDialogLanguage.get(which));
                        index = Connection.getInstance().languages.indexOf(alertDialogLanguage.get(which));
                        langCode2 = Connection.getInstance().languagesCode.get(index);
                    }
                });
                builder.setNegativeButton("Çıkış", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });


        lang1.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Şu dilden çevir:");
                builder.setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lang1.setText(Connection.getInstance().languages.get(which));
                        textViewLang1.setText(Connection.getInstance().languages.get(which));
                        lang2.setText("Bir dil seçiniz");
                        langCode = Connection.getInstance().languagesCode.get(which);
                        bufferArrayLanguage2.clear();
                        if(!(langCode.equals("en") || langCode.equals("de") || langCode.equals("es") || langCode.equals("it") || langCode.equals("fr"))){
                            bufferArrayLanguage2.add(Connection.getInstance().modals.get(langCode));
                        }
                        else{ //en-de-es-it-fr
                            regexLanguage2 = Objects.requireNonNull(Connection.getInstance().modals.get(langCode)).split(",");
                        }
                        control = true;
                    }
                });
                builder.setNegativeButton("Çıkış", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @Override
    public void onDestroy() {
        database.close();
        super.onDestroy();
    }
}