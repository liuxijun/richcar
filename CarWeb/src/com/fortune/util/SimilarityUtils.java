package com.fortune.util;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ����· on 2014/12/22.
 * �ִ����ƶȼ����㷨���㷨��ϸ���ͼ�����URL
 * http://www.catalysoft.com/articles/StrikeAMatch.html
 * ����˵ͨ�����ִ��ָ�Ϊ2���֣��ַ���������˳��ƥ�������ִ��ɹ��Ķ��� n��2*n/unionCount(�����ִ������ܺ�)Ϊ���ƶ�
 */
public class SimilarityUtils {
    public static double getSimilarity(String s1, String s2){
        if( s1 == null || s2 == null) return 0.0;

        ArrayList list1 = getWordsLetterPairs(s1);
        ArrayList list2 =  getWordsLetterPairs(s2);
        int unionCount = list1.size() + list2.size();
        int intersection = 0;
        for( Object o1 : list1){
            for( Object o2 : list2){
                if( o1.equals(o2)) intersection++;
            }
        }

        return (double)intersection*2/unionCount;
    }

    public static ArrayList getWordsLetterPairs(String s){
        if(s == null) return null;

        ArrayList pairs = new ArrayList();

        // ȥ�ո񣬶��� ����
        String str = s.replace(",", " ").
                replace(",", " ").
                replace("��", " ").
                replace("'", " ").
                replace("\"", " ").replace("��", " ").replace("��", " ");
        String[] words = str.split("\\s+");

        for(String word : words){
            pairs.addAll(getWordLetterPairs(word));
        }

        return pairs;
    }

    public static ArrayList getWordLetterPairs(String word){
        int numPairs = word.length()-1;
        ArrayList list = new ArrayList();
        for (int i=0; i<numPairs; i++) {
            list.add(word.substring(i, i+2));
        }
        return list;
    }
}
