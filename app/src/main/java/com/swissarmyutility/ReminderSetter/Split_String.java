package com.swissarmyutility.ReminderSetter;

/**
 * Created by kapil.gupta on 28-07-2014.
 */
public class Split_String {
    //* this method splitting the String
    public String[] split(String s, char suffix) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == suffix) {
                count++;
            }

        }
        String[] temp = new String[count + 1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = "";
        }
        count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == suffix) {
                count++;
            } else {
                if (count < temp.length) {
                    temp[count] += s.charAt(i);
                }
            }
        }
        return temp;
    }
}
