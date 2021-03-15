package com.burakgomec.wordreminder.View;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.burakgomec.wordreminder.Connection;
import com.burakgomec.wordreminder.Model.DatabaseController;
import com.burakgomec.wordreminder.Model.Database;
import com.burakgomec.wordreminder.Model.TranslatedWord;
import com.burakgomec.wordreminder.R;
import com.burakgomec.wordreminder.databinding.FragmentSearchBinding;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import java.util.Objects;


public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    String editString,langCode,langCode2,language1TextBuffer,bufferRecordedWord;
    Boolean saved;
    ArrayList<String> bufferArrayLanguage2,alertDialogLanguage;
    String[] regexLanguage2;
    Integer index;
    Database database;
    SharedPreferences sharedPreferences;
    ClipboardManager clipboardManager;
    ClipData clipData;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setFirstViewFromSharedPref(view);
        bufferArrayLanguage2 = new ArrayList<>(); alertDialogLanguage = new ArrayList<>();

        setSecondLanguageCodes();

        binding.editTextSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        binding.editTextSearch.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.textViewResult.setMovementMethod(new ScrollingMovementMethod()); // scroll in text view

        database = new Database(view.getContext());
        clipboardManager = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        binding.imageViewStar.setOnClickListener(v -> recordWordsToDb());
        binding.changeLanguage.setOnClickListener(v -> changeLanguages());

        binding.editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            binding.editTextSearch.clearFocus();
            saved = false;
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                searchWord();
                return true;
            }
            return false;
        });

        binding.textViewResult.setOnLongClickListener(v -> {
            clipData = ClipData.newPlainText("text",binding.textViewResult.getText());
            clipboardManager.setPrimaryClip(clipData);
            Snackbar.make(v, R.string.copied, Snackbar.LENGTH_SHORT).show();
            return true;
        });

        binding.secondLanguageButton.setOnClickListener(this::secondLanguageButtonClickListener);

        binding.firstLanguageButton.setOnClickListener(this::firstLanguageButtonClickListener);

        binding.editTextSearch.setOnFocusChangeListener((v, hasFocus) -> clearEditText(hasFocus));

        binding.clearEditText.setOnClickListener(v -> clearEditText());
    }

    private void clearEditText(){
        binding.editTextSearch.setText("");
        binding.textViewResult.setText("");
        binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
        binding.imageViewStar.setTag("null");
    }


    private void setFirstViewFromSharedPref(View view){
        sharedPreferences =  view.getContext().getSharedPreferences("languages",Context.MODE_PRIVATE);
        binding.textViewLang1.setText(sharedPreferences.getString("Language1", getString(R.string.turkish)));
        binding.textViewLang2.setText(sharedPreferences.getString("Language2",getString(R.string.english)));
        binding.firstLanguageButton.setText(sharedPreferences.getString("Language1",getString(R.string.turkish)));
        binding.secondLanguageButton.setText(sharedPreferences.getString("Language2",getString(R.string.english)));
        langCode = sharedPreferences.getString("langCode1","tr");
        langCode2 = sharedPreferences.getString("langCode2","en");
    }



    private void checkWordFromDb(String editString){
        //Linear search in array list. O(N) time complexity
        for(TranslatedWord translatedWord : DatabaseController.getInstance().getTranslatedWordArrayList()){
            if(editString.equals(translatedWord.getFirstWord())){
                binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_rate_24);
                binding.imageViewStar.setTag("!null");
                binding.textViewResult.setText(translatedWord.getSecondWord());
                saved = true;
                break; } }
    }



    private void searchWord(){
        editString = binding.editTextSearch.getText().toString();
        if((!editString.equals("")) && (!binding.secondLanguageButton.getText().equals(getString(R.string.choose_a_language)))){
            checkWordFromDb(editString);
            if(!saved){
                binding.imageViewStar.setTag("null");
                binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                Connection.getInstance().getWord(editString,langCode,langCode2,getActivity(), binding.textViewResult,getView());
            }
        }
        else if(binding.secondLanguageButton.getText().equals(getString(R.string.choose_a_language))){
            Toast.makeText(getContext(), R.string.chooseTheLanguageToTranslate, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getContext(), R.string.enterWord, Toast.LENGTH_SHORT).show();
        }
    }

    private void changeLanguages(){
        if(!binding.secondLanguageButton.getText().equals(getString(R.string.choose_a_language))){
            //View
            language1TextBuffer = binding.firstLanguageButton.getText().toString();
            binding.firstLanguageButton.setText(binding.secondLanguageButton.getText());
            binding.textViewLang1.setText(binding.secondLanguageButton.getText());
            binding.textViewLang2.setText(language1TextBuffer);
            binding.secondLanguageButton.setText(language1TextBuffer);

            //Ibm service
            language1TextBuffer = langCode;
            langCode = langCode2;
            langCode2 = language1TextBuffer;
            editString = binding.textViewResult.getText().toString();

            if(!editString.trim().equals("")){
               if(binding.imageViewStar.getTag().equals("!null")){
                   binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                   binding.imageViewStar.setTag("null");
                   bufferRecordedWord = binding.editTextSearch.getText().toString();
               }
               Connection.getInstance().getWord(editString,langCode,langCode2,getActivity(), binding.textViewResult,getView());
               binding.editTextSearch.setText(editString);
               if(editString.equals(bufferRecordedWord)) {
                   binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_rate_24);
                   binding.imageViewStar.setTag("!null");
                   bufferRecordedWord = editString;
               }
            }
            setSecondLanguageCodes();
        }
    }




    private void recordWordsToDb(){
        if(!binding.textViewResult.getText().toString().equals("")){
            if(binding.imageViewStar.getTag().equals("null")){
                binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_rate_24);
                binding.imageViewStar.setTag("!null");
                database.saveWords(binding.editTextSearch.getText().toString(),binding.textViewResult.getText().toString());
            }
            else{
                binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                binding.imageViewStar.setTag("null");
                database.deleteWordWithString(binding.textViewResult.getText().toString());
            }
        }
    }

    private void clearEditText(Boolean hasFocus){
       if(hasFocus){
           binding.clearEditText.setVisibility(View.VISIBLE);
       }
       else{
           binding.clearEditText.setVisibility(View.INVISIBLE);
       }
    }

    private void firstLanguageButtonClickListener(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(getString(R.string.choose_a_language));
        builder.setItems(R.array.languages, (dialog, which) -> {
            binding.firstLanguageButton.setText(Connection.getInstance().languages.get(which));
            binding.textViewLang1.setText(Connection.getInstance().languages.get(which));
            binding.secondLanguageButton.setText(getString(R.string.choose_a_language));
            langCode = Connection.getInstance().languagesCode.get(which);
            setSecondLanguageCodes();
            binding.textViewResult.setText("");
            binding.imageViewStar.setTag("null");
            binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
            binding.textViewLang2.setText(getString(R.string.choose_a_language));
        });
        builder.setNegativeButton(R.string.exit, (dialog, which) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void secondLanguageButtonClickListener(View v){
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
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.translateTo);
            builder.setItems(alertDialogLanguage.toArray(new String[0]), (dialog, which) -> {
                binding.secondLanguageButton.setText(alertDialogLanguage.get(which));
                binding.textViewLang2.setText(alertDialogLanguage.get(which));
                index = Connection.getInstance().languages.indexOf(alertDialogLanguage.get(which));
                langCode2 = Connection.getInstance().languagesCode.get(index);
                editString = binding.editTextSearch.getText().toString();
                if(!editString.trim().equals("")){
                    Connection.getInstance().getWord(editString,langCode,langCode2,getActivity(), binding.textViewResult,getView());
                }
                if(binding.imageViewStar.getTag().equals("!null")){
                    binding.imageViewStar.setImageResource(R.drawable.ic_baseline_star_border_24);
                    binding.imageViewStar.setTag("null");
                }
            });
            builder.setNegativeButton(getString(R.string.exit), (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
    }

    private void setSecondLanguageCodes(){
        bufferArrayLanguage2.clear();
        if(!(langCode.equals("en") || langCode.equals("de") || langCode.equals("es") || langCode.equals("it") || langCode.equals("fr"))){
            bufferArrayLanguage2.add(Connection.getInstance().modals.get(langCode));
        }
        else{ //en-de-es-it-fr
            regexLanguage2 = Objects.requireNonNull(Connection.getInstance().modals.get(langCode)).split(",");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!binding.secondLanguageButton.getText().toString().equals(getString(R.string.choose_a_language))){
            editor.putString("langCode1",langCode);
            editor.putString("langCode2",langCode2);
            editor.putString("Language1",binding.textViewLang1.getText().toString());
            editor.putString("Language2",binding.textViewLang2.getText().toString());
        }
        else{
            editor.putString("langCode1","tr");
            editor.putString("langCode2","en");
            editor.putString("Language1",getString(R.string.turkish));
            editor.putString("Language2",getString(R.string.english));
        }
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
        binding = null;
    }

}