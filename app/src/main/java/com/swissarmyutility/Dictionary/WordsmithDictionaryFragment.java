package com.swissarmyutility.Dictionary;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.swissarmyutility.R;
import com.swissarmyutility.Networking.WebServices;
import com.swissarmyutility.Utility.ImageLoader;
import com.swissarmyutility.Utility.Utility;
import com.swissarmyutility.dataModel.DictionaryData;
import com.swissarmyutility.dataModel.Part;
import com.swissarmyutility.dataModel.Pronunciation;
import com.swissarmyutility.dataModel.Sense;
import com.swissarmyutility.globalnavigation.AppFragment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/22/2014.
 */
public class WordsmithDictionaryFragment extends AppFragment {
    MediaPlayer mMediaPlayer;
    InputMethodManager inputMethodManger ;

    Button mSearchButton;
    EditText mSearchText;
    TextView header;
    RelativeLayout load_dictionary_data_layout;
    LinearLayout dictionary_data_layout;
    LinearLayout pronunciationsLayout;
    LinearLayout partsLayout;
    WebServices mSingletonWebServicesInstance;
    DictionaryData mDictionaryData;
    LayoutInflater inflator;
    LinearLayout data_layout;
    RelativeLayout dictioanry_word_meaning_layout;
    TextView word_meaning_not_found_msg;
    LinearLayout footer_layout;
    Button mWordPronunciationSoundBtn;
    ProgressBar mWordSoundStreamingProgressBar;
    FetchDictionaryDataTask mFetchDictionaryDataTask;
    ImageLoader mImageLoader;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_wordsmyth_dictionary,null);
        mImageLoader = new ImageLoader(getActivity());
        inputMethodManger = (InputMethodManager)getActivity().getSystemService( Context.INPUT_METHOD_SERVICE);
        inflator = getActivity().getLayoutInflater();
        footer_layout =  (LinearLayout)view.findViewById(R.id.footer_layout);
        word_meaning_not_found_msg = (TextView)view.findViewById(R.id.word_meaning_not_found_msg);
        mSearchText  = (EditText)view.findViewById(R.id.search_edit_text);
        mSearchText.addTextChangedListener(wathcher);
        mSearchButton  = (Button)view.findViewById(R.id.search_btn);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mSearchText.getText().toString().equalsIgnoreCase(""))
                {
                    if(Utility.isNetworkAvailable(getActivity())) {
                        inputMethodManger.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);

                        if(pronunciationsLayout.getChildCount() > 0)
                        {
                            pronunciationsLayout.removeAllViews();
                        }


                        if(partsLayout.getChildCount() > 0)
                        {
                            partsLayout.removeAllViews();
                        }
                        mFetchDictionaryDataTask = new FetchDictionaryDataTask();
                        mFetchDictionaryDataTask.execute(mSearchText.getText().toString());
                    }
                    else
                    {
                        Toast.makeText(getActivity(), getString(R.string.network_unavailable_msg), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), getString(R.string.empty_word_msg), Toast.LENGTH_SHORT).show();

                }

            }
        });

        dictioanry_word_meaning_layout = (RelativeLayout)view.findViewById(R.id.dictioanry_word_meaning_layout);
        data_layout = (LinearLayout)view.findViewById(R.id.data_layout);
        load_dictionary_data_layout = (RelativeLayout)view.findViewById(R.id.load_dictionary_data_layout);
        dictionary_data_layout = (LinearLayout)view.findViewById(R.id.dictionary_data_layout);
        header = (TextView)view.findViewById(R.id.header);
        pronunciationsLayout = (LinearLayout)view.findViewById(R.id.pronunciations_layout);
        partsLayout = (LinearLayout)view.findViewById(R.id.parts_layout);
        mSingletonWebServicesInstance = WebServices.getSingletonInstance();

        prepareMediaPlayer();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Wordsmyth Dictionary");
        super.onActivityCreated(savedInstanceState);
    }

    TextWatcher wathcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(mFetchDictionaryDataTask != null && !mFetchDictionaryDataTask.isCancelled())
            {
                mFetchDictionaryDataTask.cancel(true);
            }
            mWordPronunciationSoundBtn = null;
            mWordSoundStreamingProgressBar = null;
            dictionary_data_layout.setVisibility(View.GONE);
            dictioanry_word_meaning_layout.setVisibility(View.GONE);
            load_dictionary_data_layout.setVisibility(View.GONE);
            word_meaning_not_found_msg.setVisibility(View.GONE);
            footer_layout.setVisibility(View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    class FetchDictionaryDataTask extends AsyncTask<String, Integer, DictionaryData>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            word_meaning_not_found_msg.setVisibility(View.GONE);
            footer_layout.setVisibility(View.GONE);
            dictionary_data_layout.setVisibility(View.GONE);
            dictioanry_word_meaning_layout.setVisibility(View.VISIBLE);
            load_dictionary_data_layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected DictionaryData doInBackground(String... params) {
            return mSingletonWebServicesInstance.getDictionaryData(params[0]);
        }

        @Override
        protected void onPostExecute(DictionaryData dictionaryData) {
            super.onPostExecute(dictionaryData);

            int tag =100;

            mDictionaryData = dictionaryData;

            if(mDictionaryData != null)
            {
                if(mDictionaryData.getHeadWord() != null)
                {
                    header.setText(mDictionaryData.getHeadWord());
                }

                if(mDictionaryData.getPronunciationList() != null)
                {
                    ArrayList<Pronunciation> pronunciations =  mDictionaryData.getPronunciationList();
                    for(int i=0;i<pronunciations.size();i++) {
                        RelativeLayout pronunciationLayout = (RelativeLayout)inflator.inflate(R.layout.pronunciation_layout,null);

                        if(pronunciations.get(i).getStress() != null) {
                            ((TextView) pronunciationLayout.findViewById(R.id.pronunciation_text_view)).setText(pronunciations.get(i).getStress());
                            final Button wordPronunciationSoundBtn = ((Button) pronunciationLayout.findViewById(R.id.pronunciation_sound_btn));
                            final ProgressBar wordSoundStreamingProgressBar = ((ProgressBar) pronunciationLayout.findViewById(R.id.streaming_sound_progressbar));
                            wordPronunciationSoundBtn.setTag(tag++);
                            wordPronunciationSoundBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mWordSoundStreamingProgressBar = wordSoundStreamingProgressBar;
                                    mWordPronunciationSoundBtn = wordPronunciationSoundBtn;
                                    if (mWordSoundStreamingProgressBar != null)
                                        mWordSoundStreamingProgressBar.setVisibility(View.VISIBLE);

                                    if (mWordPronunciationSoundBtn != null)
                                        mWordPronunciationSoundBtn.setVisibility(View.GONE);
                                    playPronunciationSound();
                                }


                            });
                            pronunciationsLayout.addView(pronunciationLayout);
                        }

                    }

                }

                if(mDictionaryData.getPartList() != null)
                {

                    ArrayList<Part> partList =  mDictionaryData.getPartList();

                    for(int i=0;i<partList.size();i++) {


                        LinearLayout partLayout = (LinearLayout)inflator.inflate(R.layout.part_layout,null);
                        LinearLayout nameLayout = (LinearLayout)partLayout.findViewById(R.id.part_titles_layout);
                        LinearLayout sensesLayout = (LinearLayout)partLayout.findViewById(R.id.senses_layout);
/*                        TextView definition = (TextView)partLayout.findViewById(R.id.definition_text_view);
                        TextView synonyms = (TextView)partLayout.findViewById(R.id.synonym_texts_text_view);
                        TextView spanish_translation_text_view = (TextView)partLayout.findViewById(R.id.spanish_translation_text_view);*/

                        Part part = partList.get(i);
                        ArrayList<String> nameList = part.getNameList();
                        ArrayList<Sense> senseList = part.getSenseList();

                        if(nameList != null)
                        {
                            for(int j=0;j<nameList.size();j++) {

                                TextView name = (TextView)inflator.inflate(R.layout.part_title_layout,null);
                                name.setText(nameList.get(j));
                                nameLayout.addView(name);
                            }

                        }


                        if(senseList != null)
                        {
                            for(int j=0;j<senseList.size();j++) {

                                LinearLayout senseLayout = (LinearLayout)inflator.inflate(R.layout.sense_layout,null);
                                TextView definitionNoTextView = (TextView)senseLayout.findViewById(R.id.definition_no_text_view);

                                TextView definition = (TextView)senseLayout.findViewById(R.id.definition_text_view);
                                TextView synonyms = (TextView)senseLayout.findViewById(R.id.synonym_texts_text_view);
                                TextView spanish_translation_text_view = (TextView)senseLayout.findViewById(R.id.spanish_translation_text_view);
                                LinearLayout wordExampleImageListLayout = (LinearLayout)senseLayout.findViewById(R.id.images_layout);

                                if(senseList.get(j).getDefinition() != null) {
                                    definitionNoTextView.setText((j + 1) + "");
                                    definition.setText(senseList.get(j).getDefinition());
                                }
                                else
                                {
                                    definitionNoTextView.setVisibility(View.GONE);
                                    definition.setVisibility(View.GONE);
                                }

                                if(senseList.get(j).getSynonymList() != null)
                                {
                                    StringBuilder synonym = new StringBuilder();
                                    ArrayList<String> synonymList = senseList.get(j).getSynonymList();
                                    for(int k = 0 ;k<synonymList.size();k++)
                                    {
                                        if(k!= 0)
                                            synonym.append(","+synonymList.get(k));
                                        else
                                            synonym.append(synonymList.get(k));
                                    }

                                    synonyms.setText(synonym.toString());
                                }
                                else
                                {
                                    ((TextView)senseLayout.findViewById(R.id.synonym_title_textview)).setVisibility(View.GONE);
                                }


                                if(senseList.get(j).getSpanishTranslationList()!= null)
                                {
                                    StringBuilder spanishTranslateBuilder = new StringBuilder();
                                    ArrayList<String> spanishTranslateList = senseList.get(j).getSpanishTranslationList();
                                    for(int k = 0 ;k<spanishTranslateList.size();k++)
                                    {
                                        if(k!= 0)
                                            spanishTranslateBuilder.append(","+spanishTranslateList.get(k));
                                        else
                                            spanishTranslateBuilder.append(spanishTranslateList.get(k));
                                    }

                                    spanish_translation_text_view.setText(spanishTranslateBuilder.toString());
                                }
                                else
                                {
                                    ((TextView)senseLayout.findViewById(R.id.spanish_translation_title_textview)).setVisibility(View.GONE);
                                }

                                if(senseList.get(j).getImageList()!= null)
                                {
                                    ArrayList<String> imageList = senseList.get(j).getImageList();

                                    for(int k=0;k<imageList.size();k++)
                                    {

                                        String imageName = imageList.get(i);
                                        if(!imageName.endsWith(".gif")) {
                                            RelativeLayout wordImageLayout = (RelativeLayout) inflator.inflate(R.layout.word_image_layout, null);
                                            ImageView wordImage = (ImageView) wordImageLayout.findViewById(R.id.word_image_view);
                                            mImageLoader.displayImage(wordImage, WebServices.IMAGE_URL + imageName);
                                            wordExampleImageListLayout.addView(wordImageLayout);
                                        }
                                    }
                                }
                                else
                                {
                                    wordExampleImageListLayout.setVisibility(View.GONE);
                                }


                                sensesLayout.addView(senseLayout);
                            }

                        }

                        partsLayout.addView(partLayout);
                    }

                }

                load_dictionary_data_layout.setVisibility(View.GONE);
                dictionary_data_layout.setVisibility(View.VISIBLE);
                footer_layout.setVisibility(View.VISIBLE);
            }
            else
            {
                load_dictionary_data_layout.setVisibility(View.GONE);
                word_meaning_not_found_msg.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), getString(R.string.not_found_word_meaning), Toast.LENGTH_SHORT).show();
            }

        }
    }



    void prepareMediaPlayer()
    {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer arg0)
            {
                if(mWordSoundStreamingProgressBar != null)
                    mWordSoundStreamingProgressBar.setVisibility(View.GONE);

                if(mWordPronunciationSoundBtn != null)
                    mWordPronunciationSoundBtn.setVisibility(View.VISIBLE);

                mMediaPlayer.start();

            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                try {

                    if(mWordSoundStreamingProgressBar != null)
                        mWordSoundStreamingProgressBar.setVisibility(View.VISIBLE);

                    if(mWordPronunciationSoundBtn != null)
                        mWordPronunciationSoundBtn.setVisibility(View.GONE);

                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(WebServices.MUSIC_URL);
                    mMediaPlayer.prepareAsync();
                }
                catch (IOException e)
                {

                }
                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.reset();
            }
        });
    }

    void playPronunciationSound() {
        try
        {

            if( (mMediaPlayer.isPlaying() == true) || (mMediaPlayer.isLooping() == true) )
            {
                mMediaPlayer.stop();
            }

            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(WebServices.MUSIC_URL);
            mMediaPlayer.prepareAsync();
        }
        catch (IOException e)
        {

        }
    }

    void releaseMediaPlayer()
    {
        try
        {
            if(mMediaPlayer != null)
            {
                mMediaPlayer.reset();
                mMediaPlayer.release();
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }
    
}
