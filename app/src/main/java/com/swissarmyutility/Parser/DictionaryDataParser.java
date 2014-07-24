package com.swissarmyutility.Parser;

import com.swissarmyutility.dataModel.DictionaryData;
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

    DictionaryData dictionaryData;

    public DictionaryData parseDictionaryData(String xmlDictionaryData)
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

                        if (tagName.equalsIgnoreCase("entry"))
                            dictionaryData = new DictionaryData();

                        if (tagName.equalsIgnoreCase("spellings"))
                            spellingList = new ArrayList<Spelling>();

                        if (tagName.equalsIgnoreCase("spelling"))
                            spelling = new Spelling();

                        if (tagName.equalsIgnoreCase("pronunciations"))
                            pronunciationList = new ArrayList<Pronunciation>();

                        if (tagName.equalsIgnoreCase("pronunciation")) {
                            pronunciation = new Pronunciation();
                            stressBuilder = new StringBuilder();
                        }

                        if (tagName.equalsIgnoreCase("parts"))
                            partList = new ArrayList<Part>();

                        if (tagName.equalsIgnoreCase("part")) {
                            part = new Part();
                            nameList = new ArrayList<String>();
                            relatedWordList = new ArrayList<String>();
                            senseList = new ArrayList<Sense>();

                        }

                        if (tagName.equalsIgnoreCase("senses")) {
                            senseList = new ArrayList<Sense>();
                        }
                        if (tagName.equalsIgnoreCase("sense")) {
                            sense = new Sense();
                            exampleList = new ArrayList<String>();
                            imageList = new ArrayList<String>();
                            synonymList = new ArrayList<String>();
                            similarWordList = new ArrayList<String>();
                        }

                        if (tagName.equalsIgnoreCase("spanish")) {
                            isSpanishExample = true;
                            spanishTranslationList = new ArrayList<String>();
                            spanishExampleList = new ArrayList<String>();

                        }

                        if (tagName.equalsIgnoreCase("features"))
                            featuresWordHistoryList = new ArrayList<String>();

                        if (tagName.equalsIgnoreCase("wordhistory"))
                            featureWordHistory = new String();


                        if (tagName.equalsIgnoreCase("wordexplorer"))
                            wePhraseList = new ArrayList<WePhrase>();


                        if (tagName.equalsIgnoreCase("wephrase")) {
                            wePhrase = new WePhrase();
                            weWordList = new ArrayList<String>();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (tagName.equalsIgnoreCase("headword"))
                            dictionaryData.setHeadWord(text);

                        if (tagName.equalsIgnoreCase("spell")) {
                            if (spelling != null)
                                spelling.setSpell(text);
                        }

                        if (tagName.equalsIgnoreCase("syllable")) {
                            if (spelling != null)
                                spelling.setSyllable(text);
                        }

                        if (tagName.equalsIgnoreCase("spelling")) {
                            if (spelling != null && spellingList != null)
                                spellingList.add(spelling);
                        }

                        if (tagName.equalsIgnoreCase("spellings")) {
                            if (spellingList != null)
                                dictionaryData.setSpellingList(spellingList);
                        }


                        if (tagName.equalsIgnoreCase("stress")) {
                            if (stressBuilder != null)
                                stressBuilder.append(text);
                        }
                  /*      if (tagName.equalsIgnoreCase("stress")) {
                            if (stressBuilder != null)
                                stressBuilder.append(text);
                        }*/
                        if (tagName.equalsIgnoreCase("sound")) {
                            if (pronunciation != null)
                                pronunciation.setSound(text);
                        }

                        if (tagName.equalsIgnoreCase("pronunciation")) {
                            if (pronunciation != null) {
                                if (stressBuilder != null)
                                    pronunciation.setStress(stressBuilder.toString());
                                if (pronunciationList != null) {
                                    pronunciationList.add(pronunciation);
                                }
                                // dictionaryData.setPronunciationList(pronunciation);
                            }
                        }

                        if (tagName.equalsIgnoreCase("pronunciations")) {
                            if (pronunciationList != null)
                                dictionaryData.setPronunciationList(pronunciationList);
                        }

                        if (tagName.equalsIgnoreCase("name")) {
                            nameList.add(text);
                        }
                        if (tagName.equalsIgnoreCase("names")) {
                            if (part != null)
                                part.setNameList(nameList);
                        }

                        if (tagName.equalsIgnoreCase("relatedword")) {
                            relatedWordList.add(text);
                        }
                        if (tagName.equalsIgnoreCase("relatedwords")) {
                            if (part != null)
                                part.setRelatedWordList(relatedWordList);
                        }

                        if (tagName.equalsIgnoreCase("definition")) {
                            if (sense != null)
                                sense.setDefinition(text);
                        }

                        if (tagName.equalsIgnoreCase("translation")) {
                            if (spanishTranslationList != null)
                                spanishTranslationList.add(text);
                        }

                        if (tagName.equalsIgnoreCase("translations")) {
                            if (sense != null && spanishTranslationList != null) {
                                sense.setSpanishTranslationList(spanishTranslationList);
                            }
                        }

                        if (tagName.equalsIgnoreCase("example")) {
                            if (isSpanishExample) {
                                if (spanishExampleList != null)
                                    spanishExampleList.add(text);
                            } else {
                                if (exampleList != null)
                                    exampleList.add(text);
                            }
                        }

                        if (tagName.equalsIgnoreCase("examples")) {
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

                

                if (tagName.equalsIgnoreCase("spanish")) {
                    isSpanishExample = false;
                }

                if (tagName.equalsIgnoreCase("image")) {
                    imageList.add(text);
                }

                if (tagName.equalsIgnoreCase("images")) {
                    if (sense != null && imageList != null) {
                        sense.setImageList(imageList);
                    }
                  }

                 if(tagName.equalsIgnoreCase("synonym"))
                 {
                     if(synonymList != null)
                         synonymList.add(text);
                 }
                if(tagName.equalsIgnoreCase("synonyms"))
                {
                    if(sense != null && synonymList != null)
                        sense.setSynonymList(synonymList);
                }

                if(tagName.equalsIgnoreCase("similarword"))
                {
                    if(similarWordList != null)
                        similarWordList.add(text);
                }
                if(tagName.equalsIgnoreCase("similarwords"))
                {
                    if(sense != null && similarWordList != null)
                        sense.setSimilarWordList(similarWordList);
                }

                if(tagName.equalsIgnoreCase("sense"))
                {
                    if(sense != null && senseList != null )
                        senseList.add(sense);
                }

                if(tagName.equalsIgnoreCase("senses"))
                {
                    if( senseList != null )
                        part.setSenseList(senseList);
                }

                if(tagName.equalsIgnoreCase("part"))
                {
                    if( part != null )
                       partList.add(part);

                }

                if(tagName.equalsIgnoreCase("parts"))
                {
                    if( partList != null )
                     dictionaryData.setPartList(partList);
                }

                if(tagName.equalsIgnoreCase("wordhistory"))
                {
                    if( featuresWordHistoryList != null )
                        featuresWordHistoryList.add(text);
                }

                if(tagName.equalsIgnoreCase("features"))
                {
                    if( featuresWordHistoryList != null )
                       dictionaryData.setFeaturesWordHistoryList(featuresWordHistoryList);
                }

                if(tagName.equalsIgnoreCase("wordhistory"))
                {
                    if( featuresWordHistoryList != null )
                        featuresWordHistoryList.add(text);
                }


                if(tagName.equalsIgnoreCase("title"))
                {
                    if( wePhrase != null )
                        wePhrase.setTitle(text);
                }

                if(tagName.equalsIgnoreCase("weword"))
                {
                    if( weWordList != null )
                        weWordList.add(text);
                }
                if(tagName.equalsIgnoreCase("wewords"))
                {
                    if(wePhrase != null && weWordList != null )
                        wePhrase.setWeWordList(weWordList);
                }

                if(tagName.equalsIgnoreCase("wephrase"))
                {
                    if(wePhrase != null && wePhraseList != null )
                        wePhraseList.add(wePhrase);
                }

                if(tagName.equalsIgnoreCase("wordexplorer"))
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
			// TODO: handle exception
		}
        
        return dictionaryData;
    }

}
