package com.swissarmyutility.Dictionary;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.swissarmyutility.Utility.NetworkConnection;
import com.swissarmyutility.dataModel.WordsmythDictionaryData;
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
    InputMethodManager mInputMethodManger;

    private Button mSearchButton;
    private EditText mSearchEditText;
    private TextView mHeadertTextview;
    private RelativeLayout mLoadDictionaryDataLayout;
    private LinearLayout mDictionaryDataLayout;
    private LinearLayout mPronunciationsLayout;
    private LinearLayout mPartsLayout;
    private WebServices mSingletonWebServicesInstance;
    private WordsmythDictionaryData mDictionaryData;
    private LayoutInflater mLayoutInflator;
    private LinearLayout mDataLayout;
    private RelativeLayout mDictioanryWordMeaningLayout;
    private TextView mWordMeaningNotFoundMessageTextview;
    private LinearLayout mFooterLayout;
    private Button mWordPronunciationSoundBtn;
    private ProgressBar mWordSoundStreamingProgressBar;
    private FetchDictionaryDataTask mFetchDictionaryDataTask;
    private ImageLoader mImageLoader;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_wordsmyth_dictionary,null);
        mImageLoader = new ImageLoader(getActivity());
        mInputMethodManger = (InputMethodManager)getActivity().getSystemService( Context.INPUT_METHOD_SERVICE);
        mLayoutInflator = getActivity().getLayoutInflater();
        mFooterLayout =  (LinearLayout)view.findViewById(R.id.footer_layout);
        mWordMeaningNotFoundMessageTextview = (TextView)view.findViewById(R.id.word_meaning_not_found_msg);
        mSearchEditText = (EditText)view.findViewById(R.id.search_edit_text);
        mSearchEditText.addTextChangedListener(mDictionaryWordChangeWatcher);
        mSearchButton  = (Button)view.findViewById(R.id.search_btn);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mSearchEditText.getText().toString().equalsIgnoreCase(""))
                {
                    if(NetworkConnection.isNetworkAvailable(getActivity())) {
                        mInputMethodManger.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);

                        if(mPronunciationsLayout.getChildCount() > 0)
                        {
                            mPronunciationsLayout.removeAllViews();
                        }


                        if(mPartsLayout.getChildCount() > 0)
                        {
                            mPartsLayout.removeAllViews();
                        }
                        mFetchDictionaryDataTask = new FetchDictionaryDataTask();
                        mFetchDictionaryDataTask.execute(mSearchEditText.getText().toString());
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

        mDictioanryWordMeaningLayout = (RelativeLayout)view.findViewById(R.id.dictioanry_word_meaning_layout);
        mDataLayout = (LinearLayout)view.findViewById(R.id.data_layout);
        mLoadDictionaryDataLayout = (RelativeLayout)view.findViewById(R.id.load_dictionary_data_layout);
        mDictionaryDataLayout = (LinearLayout)view.findViewById(R.id.dictionary_data_layout);
        mHeadertTextview = (TextView)view.findViewById(R.id.header);
        mPronunciationsLayout = (LinearLayout)view.findViewById(R.id.pronunciations_layout);
        mPartsLayout = (LinearLayout)view.findViewById(R.id.parts_layout);
        mSingletonWebServicesInstance = WebServices.getSingletonInstance();

        prepareMediaPlayer();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle(getString(R.string.wordsmyth_dictionary));
        super.onActivityCreated(savedInstanceState);
    }

   private TextWatcher mDictionaryWordChangeWatcher = new TextWatcher() {
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
            mDictionaryDataLayout.setVisibility(View.GONE);
            mDictioanryWordMeaningLayout.setVisibility(View.GONE);
            mLoadDictionaryDataLayout.setVisibility(View.GONE);
            mWordMeaningNotFoundMessageTextview.setVisibility(View.GONE);
            mFooterLayout.setVisibility(View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

   private class FetchDictionaryDataTask extends AsyncTask<String, Integer, WordsmythDictionaryData>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWordMeaningNotFoundMessageTextview.setVisibility(View.GONE);
            mFooterLayout.setVisibility(View.GONE);
            mDictionaryDataLayout.setVisibility(View.GONE);
            mDictioanryWordMeaningLayout.setVisibility(View.VISIBLE);
            mLoadDictionaryDataLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected WordsmythDictionaryData doInBackground(String... params) {
            return mSingletonWebServicesInstance.getDictionaryData(params[0]);
        }

        @Override
        protected void onPostExecute(WordsmythDictionaryData dictionaryData) {
            super.onPostExecute(dictionaryData);

            mDictionaryData = dictionaryData;

            if(mDictionaryData != null)
            {
                if(mDictionaryData.getHeadWord() != null)
                {
                    mHeadertTextview.setText(mDictionaryData.getHeadWord());
                }

                if(mDictionaryData.getPronunciationList() != null)
                {
                    ArrayList<Pronunciation> pronunciations =  mDictionaryData.getPronunciationList();
                    for(int i=0;i<pronunciations.size();i++) {
                        RelativeLayout pronunciationLayout = (RelativeLayout) mLayoutInflator.inflate(R.layout.pronunciation_layout,null);

                        if(pronunciations.get(i).getStress() != null) {
                            ((TextView) pronunciationLayout.findViewById(R.id.pronunciation_text_view)).setText(pronunciations.get(i).getStress());
                            final Button wordPronunciationSoundBtn = ((Button) pronunciationLayout.findViewById(R.id.pronunciation_sound_btn));
                            final ProgressBar wordSoundStreamingProgressBar = ((ProgressBar) pronunciationLayout.findViewById(R.id.streaming_sound_progressbar));
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
                            mPronunciationsLayout.addView(pronunciationLayout);
                        }

                    }

                }

                if(mDictionaryData.getPartList() != null)
                {
                    ArrayList<Part> partList =  mDictionaryData.getPartList();
                    for(int i=0;i<partList.size();i++) {

                        LinearLayout partLayout = (LinearLayout) mLayoutInflator.inflate(R.layout.part_layout,null);
                        LinearLayout nameLayout = (LinearLayout)partLayout.findViewById(R.id.part_titles_layout);
                        LinearLayout sensesLayout = (LinearLayout)partLayout.findViewById(R.id.senses_layout);

                        Part part = partList.get(i);
                        ArrayList<String> nameList = part.getNameList();
                        ArrayList<Sense> senseList = part.getSenseList();

                        if(nameList != null)
                        {
                            for(int j=0;j<nameList.size();j++) {

                                TextView name = (TextView) mLayoutInflator.inflate(R.layout.part_title_layout,null);
                                name.setText(nameList.get(j));
                                nameLayout.addView(name);
                            }

                        }


                        if(senseList != null)
                        {
                            for(int j=0;j<senseList.size();j++) {

                                LinearLayout senseLayout = (LinearLayout) mLayoutInflator.inflate(R.layout.sense_layout,null);
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
                                            RelativeLayout wordImageLayout = (RelativeLayout) mLayoutInflator.inflate(R.layout.word_image_layout, null);
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

                        mPartsLayout.addView(partLayout);
                    }

                }

                mLoadDictionaryDataLayout.setVisibility(View.GONE);
                mDictionaryDataLayout.setVisibility(View.VISIBLE);
                mFooterLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                mLoadDictionaryDataLayout.setVisibility(View.GONE);
                mWordMeaningNotFoundMessageTextview.setVisibility(View.VISIBLE);
            }
        }
    }



    private  void prepareMediaPlayer()
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

    private void playPronunciationSound() {
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

  private void releaseMediaPlayer()
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
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup back button key listener to release MediaPlayer
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    releaseMediaPlayer();
                }
                return false;
            }
        });
    }
}
