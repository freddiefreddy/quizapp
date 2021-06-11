package com.todocode.quizv3.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;

//import androidx.core.content.ContextCompat;

//public class Utilities {
//
//    public static  boolean isSameDomain(String url, String url1){
//        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
//    }
//
//    private static String getRootDomainUrl(String url) {
//        String [] domainValues = url.split("/")[2].split("\\.");
//        int length = domainValues.length;
//        int recall = domainValues[0].equals("www") ? 1 : 0;
//        if(length - recall == 2){
//            return domainValues[length - 2] + "." + domainValues[length - 1];
//        }
//        else{
//            if(domainValues[length - 1].length()== 2){
//                return domainValues[length -3] + "." + domainValues[length-2] + "." + domainValues[length-1];
//            }
//            else{
//                return  domainValues[length - 2] + "." + domainValues[length - 1];
//            }
//        }
//    }
//    public static  void  tintMenuIcon(Context context, MenuItem item, int color){
//        Drawable drawable = item.getIcon();
//        if (drawable != null){
//            drawable.mutate();
//            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
//        };
//    }
//
//    public  static void bookmarkUrl(Context context, String url){
//        SharedPreferences pref = context.getSharedPreferences("QuizV4", 0); //0 represents private mode
//        SharedPreferences.Editor editor = pref.edit();
//
//        //url already bookmarked you can unbookmark it
//        if(pref.getBoolean(url, false)){
//            editor.putBoolean(url, false);
//        }
//        else{
//            editor.putBoolean(url, true);
//        }
//        editor.commit();
//    }
//
//    public static boolean isBookmarked(Context context, String url){
//        SharedPreferences pref = context.getSharedPreferences("QuizV4",0);
//        return pref.getBoolean(url, false);
//    }


    public class Utilities {

        public static boolean isSameDomain(String url, String url1) {
            return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
        }

        private static String getRootDomainUrl(String url) {
            String[] domainKeys = url.split("/")[2].split("\\.");
            int length = domainKeys.length;
            int dummy = domainKeys[0].equals("www") ? 1 : 0;
            if (length - dummy == 2)
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            else {
                if (domainKeys[length - 1].length() == 2) {
                    return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
                } else {
                    return domainKeys[length - 2] + "." + domainKeys[length - 1];
                }
            }
        }

        public static void tintMenuIcon(Context context, MenuItem item, int color) {
            Drawable drawable = item.getIcon();
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawable's with this id will have a color
                // filter applied to it.
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
            }
        }

        public static void bookmarkUrl(Context context, String url) {
            SharedPreferences pref = context.getSharedPreferences("QuizV4", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();

            // if url is already bookmarked, unbookmark it
            if (pref.getBoolean(url, false)) {
                editor.putBoolean(url, false);
            } else {
                editor.putBoolean(url, true);
            }

            editor.commit();
        }

        public static boolean isBookmarked(Context context, String url) {
            SharedPreferences pref = context.getSharedPreferences("QuizV4", 0);
            return pref.getBoolean(url, false);
        }
    }
