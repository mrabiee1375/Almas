package com.example.almas.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StaticVars {
    public static String BaseUrl = "http://192.168.1.33:45455/";
    //public  static  String BaseUrl="http://192.168.43.103:45455/";

    public static boolean ChooseImageFlag;
    public static int ListItemsCount = 20;
    public static boolean IsAdmin;
    public  static  UserModel UserModel;
    public static HashMap<Integer, Integer> BillTypes = new HashMap<Integer, Integer>() {
        {
            put(0, 1);
            put(1, 2);
            put(2, 3);
            put(3, 4);
        }
    };
    public static HashMap<Integer, String> BillKeyValueTypes = new HashMap<Integer, String>() {
        {
            put(1, "برق");
            put(2, "آب");
            put(3, "گاز");
            put(4, "مالیات");
        }
    };
    public  static  ArrayList<String> BillTypesStrList=new ArrayList<String>()
    {
        {
            add("برق");
            add("آب");
            add("گاز");
            add("مالیات");
        }
    };
}
