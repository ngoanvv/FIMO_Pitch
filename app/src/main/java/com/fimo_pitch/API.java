package com.fimo_pitch;

/**
 * Created by diep1 on 12/5/2016.
 */

public class API {
    public static String API = "http://118.70.72.13:3000/";
    public static String GetSystemPitch="system_pitch/getallsystempitch";
    public static String GetNews=API+"find_teams/getfindteams/1";
    public static String GetPrice=API+"pitch/getmanagementpitchforpitch/";
    public static String CreateNews=API+"find_teams/createNews";
    public static String InsertPitch=API+"pitch/insertPitch";
    public static String InsertSystemPitch=API+"system_pitch/insertsystempich";
    public static String GetAllPitchofSystem=API+"pitch/getallpitchsofsystem/";
    public static String UpdatePitch=API+"pitch/updatepitch/";
    public static String Login=API+"users/login";
    public static String SearcInDay=API+"system_pitch/searchPitch";
    public static String NewPrice=API+"system_pitch/insertmanagement";
    public static String UpdatePrice=API+"pitch/updatemanagementpitch/";
    public static String GetOrders=API+"pitch/getcalendarforday";
    public static String UpdateFCMToken =API+"users/updatetockentouser/";
    public static String BookPitch=API+"system_pitch/bookpitch";
    public static String GetTime=API+"pitch/getlistmanagementpitchforday";
    public static String UpdateOrder=API+"pitch/updateorder";
    public static String SignUp=API+"users/createNew";
    public static String GetSystemByLocation=API+"system_pitch/getsystempitchbylocation";
}
