package com.swissarmyutility.Parser;

import com.swissarmyutility.Constant.Constants;
import com.swissarmyutility.dataModel.WordsmythDictionaryData;
import com.swissarmyutility.dataModel.Part;
import com.swissarmyutility.dataModel.Pronunciation;
import com.swissarmyutility.dataModel.Sense;
import com.swissarmyutility.dataModel.Spelling;
import com.swissarmyutility.dataModel.WePhrase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by piyush.sharma on 7/16/2014.
 */
public class DictionaryDataParser {

    WordsmythDictionaryData dictionaryData;

    public WordsmythDictionaryData parseDictionaryData(String xmlDictionaryData)
    {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlDictionaryData));
            int eventType = xmlPullParser.getEventType();

            ArrayList<Spelling> spellingList = null;
            ArrayList<Pronunciation> pronunciationList = null;
            ArrayList<Part> partList = null;
            ArrayList<String> featuresWordHistoryList = null;
            ArrayList<WePhrase> wePhraseList = null;
            Spelling spelling = null;
            Pronunciation pronunciation = null;
            StringBuilder stressBuilder = null;
            Part part = null;
            String featureWordHistory = "";
            WePhrase wePhrase = null;
            //Part fields
            ArrayList<String> nameList = null;
            ArrayList<String> relatedWordList = null;
            ArrayList<Sense> senseList = null;
            Sense sense = null;
            //Sense fields
            boolean isSpanishExample = false;
            ArrayList<String> spanishTranslationList = null;
            ArrayList<String> spanishExampleList = null;
            ArrayList<String> exampleList = null;
            ArrayList<String> imageList = null;
            ArrayList<String> synonymList = null;
            ArrayList<String> similarWordList = null;
            // Weword field
            ArrayList<String> weWordList = null;
            String text = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xmlPullParser.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG:

                        if (tagName.equalsIgnoreCase(Constants.ENTRY))
                            dictionaryData = new WordsmythDictionaryData();

                        if (tagName.equalsIgnoreCase(Constants.SPELLINGS))
                            spellingList = new ArrayList<Spelling>();

                        if (tagName.equalsIgnoreCase(Constants.SPELLING))
                            spelling = new Spelling();

                        if (tagName.equalsIgnoreCase(Constants.PRONUNCIATIONS))
                            pronunciationList = new ArrayList<Pronunciation>();

                        if (tagName.equalsIgnoreCase(Constants.PRONUNCIATION)) {
                            pronunciation = new Pronunciation();
                            stressBuilder = new StringBuilder();
                        }

                        if (tagName.equalsIgnoreCase(Constants.PARTS))
                            partList = new ArrayList<Part>();

                        if (tagName.equalsIgnoreCase(Constants.PART)) {
                            part = new Part();
                            nameList = new ArrayList<String>();
                            relatedWordList = new ArrayList<String>();
                            senseList = new ArrayList<Sense>();

                        }

                        if (tagName.equalsIgnoreCase(Constants.SENSES)) {
                            senseList = new ArrayList<Sense>();
                        }
                        if (tagName.equalsIgnoreCase(Constants.SENSE)) {
                            sense = new Sense();
                            exampleList = new ArrayList<String>();
                            imageList = new ArrayList<String>();
                            synonymList = new ArrayList<String>();
                            similarWordList = new ArrayList<String>();
                        }

                        if (tagName.equalsIgnoreCase(Constants.SPANISH)) {
                            isSpanishExample = true;
                            spanishTranslationList = new ArrayList<String>();
                            spanishExampleList = new ArrayList<String>();

                        }

                        if (tagName.equalsIgnoreCase(Constants.FEATURES))
                            featuresWordHistoryList = new ArrayList<String>();

                        if (tagName.equalsIgnoreCase(Constants.WORD_HISTORY))
                            featureWordHistory = new String();


                        if (tagName.equalsIgnoreCase(Constants.WORD_EXPLORER))
                            wePhraseList = new ArrayList<WePhrase>();


                        if (tagName.equalsIgnoreCase(Constants.WE_PHRASE)) {
                            wePhrase = new WePhrase();
                            weWordList = new ArrayList<String>();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (tagName.equalsIgnoreCase(Constants.HEAD_WORD))
                            dictionaryData.setHeadWord(text);

                        if (tagName.equalsIgnoreCase(Constants.SPELL)) {
                            if (spelling != null)
                                spelling.setSpell(text);
                        }

                        if (tagName.equalsIgnoreCase(Constants.SYLLABLE)) {
                            if (spelling != null)
                                spelling.setSyllable(text);
                        }

                        if (tagName.equalsIgnoreCase(Constants.SPELLING)) {
                            if (spelling != null && spellingList != null)
                                spellingList.add(spelling);
                        }

                        if (tagName.equalsIgnoreCase(Constants.SPELLINGS)) {
                            if (spellingList != null)
                                dictionaryData.setSpellingList(spellingList);
                        }


                        if (tagName.equalsIgnoreCase(Constants.STRESS)) {
                            if (stressBuilder != null)
                                stressBuilder.append(text);
                        }

                        if (tagName.equalsIgnoreCase(Constants.SOUND)) {
                            if (pronunciation != null)
                                pronunciation.setSound(text);
                        }

                        if (tagName.equalsIgnoreCase(Constants.PRONUNCIATION)) {
                            if (pronunciation != null) {
                                if (stressBuilder != null)
                                    pronunciation.setStress(stressBuilder.toString());
                                if (pronunciationList != null) {
                                    pronunciationList.add(pronunciation);
                                }
                            }
                        }

                        if (tagName.equalsIgnoreCase(Constants.PRONUNCIATIONS)) {
                            if (pronunciationList != null)
                                dictionaryData.setPronunciationList(pronunciationList);
                        }

                        if (tagName.equalsIgnoreCase(Constants.NAME)) {
                            nameList.add(text);
                        }
                        if (tagName.equalsIgnoreCase(Constants.NAMES)) {
                            if (part != null)
                                part.setNameList(nameList);
                        }

                        if (tagName.equalsIgnoreCase(Constants.RELATED_WORD)) {
                            relatedWordList.add(text);
                        }
                        if (tagName.equalsIgnoreCase(Constants.RELATED_WORDS)) {
                            if (part != null)
                                part.setRelatedWordList(relatedWordList);
                        }

                        if (tagName.equalsIgnoreCase(Constants.DEFINITION)) {
                            if (sense != null)
                                sense.setDefinition(text);
                        }

                        if (tagName.equalsIgnoreCase(Constants.TRANSLATION)) {
                            if (spanishTranslationList != null)
                                spanishTranslationList.add(text);
                        }

                        if (tagName.equalsIgnoreCase(Constants.TRANSLATIONS)) {
                            if (sense != null && spanishTranslationList != null) {
                                sense.setSpanishTranslationList(spanishTranslationList);
                            }
                        }

                        if (tagName.equalsIgnoreCase(Constants.EXAMPLE)) {
                            if (isSpanishExample) {
                                if (spanishExampleList != null)
                                    spanishExampleList.add(text);
                            } else {
                                if (exampleList != null)
                                    exampleList.add(text);
                            }
                        }

                        if (tagName.equalsIgnoreCase(Constants.EXAMPLES)) {
                            if (sense != null) {
                                if (isSpanishExample) {
                                    if (spanishExampleList != null)
                                        sense.setSpanishExampleList(spanishExampleList);
                                } else {
                                    if (exampleList != null)
                                        sense.setExampleList(exampleList);
                                }
                            }

                        }



                if (tagName.equalsIgnoreCase(Constants.SPANISH)) {
                    isSpanishExample = false;
                }

                if (tagName.equalsIgnoreCase(Constants.IMAGE)) {
                    imageList.add(text);
                }

                if (tagName.equalsIgnoreCase(Constants.IMAGES)) {
                    if (sense != null && imageList != null) {
                        sense.setImageList(imageList);
                    }
                  }

                 if(tagName.equalsIgnoreCase(Constants.SYNONYM))
                 {
                     if(synonymList != null)
                         synonymList.add(text);
                 }
                if(tagName.equalsIgnoreCase(Constants.SYNONYMS))
                {
                    if(sense != null && synonymList != null)
                        sense.setSynonymList(synonymList);
                }

                if(tagName.equalsIgnoreCase(Constants.SIMILAR_WORD))
                {
                    if(similarWordList != null)
                        similarWordList.add(text);
                }
                if(tagName.equalsIgnoreCase(Constants.SIMILAR_WORDS))
                {
                    if(sense != null && similarWordList != null)
                        sense.setSimilarWordList(similarWordList);
                }

                if(tagName.equalsIgnoreCase(Constants.SENSE))
                {
                    if(sense != null && senseList != null )
                        senseList.add(sense);
                }

                if(tagName.equalsIgnoreCase(Constants.SENSES))
                {
                    if( senseList != null )
                        part.setSenseList(senseList);
                }

                if(tagName.equalsIgnoreCase(Constants.PART))
                {
                    if( part != null )
                       partList.add(part);

                }

                if(tagName.equalsIgnoreCase(Constants.PARTS))
                {
                    if( partList != null )
                     dictionaryData.setPartList(partList);
                }

                if(tagName.equalsIgnoreCase(Constants.WORD_HISTORY))
                {
                    if( featuresWordHistoryList != null )
                        featuresWordHistoryList.add(text);
                }

                if(tagName.equalsIgnoreCase(Constants.FEATURES))
                {
                    if( featuresWordHistoryList != null )
                       dictionaryData.setFeaturesWordHistoryList(featuresWordHistoryList);
                }

                if(tagName.equalsIgnoreCase(Constants.WORD_HISTORY))
                {
                    if( featuresWordHistoryList != null )
                        featuresWordHistoryList.add(text);
                }


                if(tagName.equalsIgnoreCase(Constants.TITLE))
                {
                    if( wePhrase != null )
                        wePhrase.setTitle(text);
                }

                if(tagName.equalsIgnoreCase(Constants.WE_WORD))
                {
                    if( weWordList != null )
                        weWordList.add(text);
                }
                if(tagName.equalsIgnoreCase(Constants.WE_WORDS))
                {
                    if(wePhrase != null && weWordList != null )
                        wePhrase.setWeWordList(weWordList);
                }

                if(tagName.equalsIgnoreCase(Constants.WE_PHRASE))
                {
                    if(wePhrase != null && wePhraseList != null )
                        wePhraseList.add(wePhrase);
                }

                if(tagName.equalsIgnoreCase(Constants.WORD_EXPLORER))
                {
                    if( wePhraseList != null )
                        dictionaryData.setWePhraseList(wePhraseList);

                }

                break;

                }
                eventType = xmlPullParser.next();
            }

        }
        catch (XmlPullParserException e)
        {

        }
        catch (IOException e) {
		}
        
        return dictionaryData;
    }

}
