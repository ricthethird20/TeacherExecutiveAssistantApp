package app.tea.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.tea.com.R;
import app.tea.constants.AppConstants;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class AppDb extends SQLiteOpenHelper {
    private Context mContext;
    private Cursor mCursor;
    private String mTables[];
    private String mColumns[];
    private String mWhereClause;
    private String mWhereArgs[];
    private static SQLiteDatabase db;
    private static String mSql;
    public AppDb(Context context) {
        super(context, AppConstants.DBNAME, null, 1);
        this.mContext = context;
    }
    public void open() {
        if (null == db || !db.isOpen()) {
            db = mContext.openOrCreateDatabase(AppConstants.DBNAME,
                    Context.MODE_PRIVATE, null);
        }
    }

    public void close(){
        db.close();
    }

    public void createTable() {
        try {
            String[] table_array = mContext.getResources().getStringArray(
                    R.array.db_table_name);
            for (int i = 0; i < table_array.length; i++) {
                int tables = mContext.getResources().getIdentifier(
                        table_array[i], "array", mContext.getPackageName());
                String[] col_array = mContext.getResources().getStringArray(
                        tables);

                mSql = "CREATE TABLE IF NOT EXISTS " + table_array[i] + "(";
                for (int j = 0; j < col_array.length; j++) {
                    mSql = mSql + col_array[j] + ",";
                }
                mSql = mSql.substring(0, mSql.length() - 1) + ")";
                db.execSQL(mSql);
                Log.d("TEA", "Table " + table_array[i] + " created!");
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
        }
        //updatePercentage();
    }

    public ArrayList<HashMap<String, String>> getStudents(){
        ArrayList<HashMap<String, String>> studentList
                = new ArrayList<HashMap<String, String>>();
        mColumns = new String[]{AppConstants.COL_STUD_ID,AppConstants.COL_STUD_FNAME
                ,AppConstants.COL_STUD_MNAME,AppConstants.COL_STUD_LNAME};
        mCursor = db.query(AppConstants.STUD_TABLE, mColumns
                ,null, null, null, null, AppConstants.COL_STUD_LNAME +" ASC");
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.STUDID,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_ID)));
                hashMap.put(AppConstants.STUDNAME, mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_LNAME)) +", "+mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_FNAME)) +" "+mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_MNAME)));
                studentList.add(hashMap);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return studentList;
    }

    public ArrayList<HashMap<String, String>> getSections(){
        ArrayList<HashMap<String, String>> sectionList
                = new ArrayList<HashMap<String, String>>();
        mColumns = new String[]{AppConstants.COL_SECT_NAME,AppConstants.COL_SECT_ROOM};
        mCursor = db.query(AppConstants.SECT_TABLE, mColumns
                ,null, null, null, null, null);
        Log.d("TEA", mCursor.getCount() + "");
        if(mCursor.getCount() > 0){
           mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.SECTNAME,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SECT_NAME)));
                hashMap.put(AppConstants.SECTROOM,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SECT_ROOM)));
                sectionList.add(hashMap);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return sectionList;
    }

    public ArrayList<HashMap<String,String>> getQuiz(String studID, String subjCode, String part){

        ArrayList<HashMap<String,String>> quiz = new ArrayList<HashMap<String,String>>();
        mSql = "SELECT rowid,"+AppConstants.COL_QUIZ+","+AppConstants.COL_QUIZNO+" FROM "+AppConstants.QUIZ_TABLE+" " +
                "WHERE "+AppConstants.COL_STUD_ID +" = '"+studID+"' AND "
                +AppConstants.COL_PART +" = '"+part+"' AND "
                +AppConstants.COL_SUBJ_CODE+" = '"+subjCode+"' ORDER BY "+AppConstants.COL_QUIZNO;
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.HASHQUIZID,mCursor
                        .getString(mCursor.getColumnIndex("rowid")));

                Log.d("TEA", "Query quiz id -> " + mCursor
                        .getInt(mCursor.getColumnIndex("rowid")));

                hashMap.put(AppConstants.HASHQUIZ, String.valueOf(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_QUIZ))));

                Log.d("TEA", "Query quiz grade -> " + mCursor
                        .getInt(mCursor.getColumnIndex(AppConstants.COL_QUIZ)));

                Log.d("TEA", "Query Quiz No-> " + mCursor
                        .getInt(mCursor.getColumnIndex(AppConstants.COL_QUIZNO)));

                hashMap.put(AppConstants.HASHQUIZNO, mCursor.getString(mCursor
                        .getColumnIndex(AppConstants.COL_QUIZNO)));
                quiz.add(hashMap);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return quiz;
    }

    public void insertSection(String sectName,String sectRoom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.COL_SECT_NAME, sectName);
        values.put(AppConstants.COL_SECT_ROOM, sectRoom);
        db.insertOrThrow(AppConstants.SECT_TABLE, null, values);
        db.close();
    }

    public List<String> spinnerSectionList(){
        List<String> spinnerArray =  new ArrayList<String>();
        mColumns = new String[]{AppConstants.COL_SECT_NAME};
        mCursor = db.query(AppConstants.SECT_TABLE, mColumns
                ,null, null, null, null, null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                spinnerArray.add(mCursor.getString(mCursor
                        .getColumnIndex(AppConstants.COL_SECT_NAME)));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return spinnerArray;
    }

    public void insertStudent(String studId,String lName,
                              String fName, String mName, int year, String section){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AppConstants.STUD_TABLE,AppConstants.COL_STUD_ID + "= '" + studId + "'",null);
        ContentValues values = new ContentValues();
        values.put(AppConstants.COL_STUD_ID, studId);
        values.put(AppConstants.COL_STUD_FNAME, fName);
        values.put(AppConstants.COL_STUD_LNAME, lName);
        values.put(AppConstants.COL_STUD_MNAME, mName);
        values.put(AppConstants.COL_STUD_YR, year);
        values.put(AppConstants.COL_STUD_SECTION, section);
        db.insertOrThrow(AppConstants.STUD_TABLE, null, values);
        db.close();
    }

    public void deleteRecord(String table, String col, String args){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, col + "= '" + args + "'", null);
        db.close();
    }

    public void insertSubjects(String subjCode, String subjDesc, String section){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.COL_SUBJ_CODE, subjCode);
        values.put(AppConstants.COL_SUBJ_NAME, subjDesc);
        values.put(AppConstants.COL_SUBJ_SECTION, section);
        db.insertOrThrow(AppConstants.SUBJ_TABLE, null, values);
        db.close();
    }

    public void insertStudentSubjects(String studId,String subjCode,String section){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.COL_STUD_ID, studId);
        values.put(AppConstants.COL_SUBJ_CODE, subjCode);
        values.put(AppConstants.COL_SUBJ_SECTION, section);
        db.insertOrThrow(AppConstants.STUDSUBJ_TABLE, null, values);
        db.close();
    }

    public boolean checkStudSubject(String studId,String subjCode){

        mSql = "SELECT "+AppConstants.COL_SUBJ_CODE+" FROM "+AppConstants.STUDSUBJ_TABLE+" " +
                "WHERE "+AppConstants.COL_STUD_ID +" = '"+studId+"' AND "
                +AppConstants.COL_SUBJ_CODE +" = '"+subjCode+"'";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            return true;
        }
        mCursor.close();
        return false;
    }

    public void insertStudentGrades(String studId,String subjCode,String subRecord
            , String record, String table,String rating,String quizSeatNo){

        if(!table.equals(AppConstants.QUIZ_TABLE)){
            mSql = "SELECT * FROM "+table+" " +
                    "WHERE "+AppConstants.COL_STUD_ID +" = '"+studId+"' " +
                    "AND "+AppConstants.COL_SUBJ_CODE +" = '"+subjCode+"' AND "+
                            AppConstants.COL_PART+" = '" + record +"'";
            mCursor = db.rawQuery(mSql, null);
            if(mCursor.getCount() > 0){
                mCursor.close();
                ContentValues args = new ContentValues();
                args.put(subRecord, rating);
                db.update(table, args
                        , AppConstants.COL_STUD_ID + "='" + studId + "' AND "+ AppConstants.COL_SUBJ_CODE +" = '" +subjCode+ "'" +
                        "AND "+AppConstants.COL_PART+" = '"+ record +"'", null);
            }else{
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(AppConstants.COL_STUD_ID, studId);
                values.put(AppConstants.COL_SUBJ_CODE, subjCode);
                values.put(AppConstants.COL_PART,record);
                values.put(subRecord, rating);
                db.insertOrThrow(table, null, values);
                db.close();
            }
        }else{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + table + " " +
                    "WHERE " + AppConstants.COL_STUD_ID + " = '" + studId + "' " +
                    "AND " + AppConstants.COL_SUBJ_CODE + " = '" + subjCode + "' AND " +
                    AppConstants.COL_PART + " = '" + record + "' AND " + AppConstants.COL_QUIZNO
                    + " = '" + quizSeatNo + "'");

            ContentValues values = new ContentValues();
            values.put(AppConstants.COL_STUD_ID, studId);
            values.put(AppConstants.COL_SUBJ_CODE, subjCode);
            values.put(AppConstants.COL_PART,record);
            values.put(AppConstants.COL_QUIZNO,quizSeatNo);
            values.put(subRecord, rating);

            Log.d("TEA", "saving quiz -> " + rating);

            db.insertOrThrow(table, null, values);
            db.close();
        }

    }



    public void insertGradePercent(int val, String col){
        mSql = "SELECT * FROM "+AppConstants.PERCENT_TABLE+"";

        mCursor = db.rawQuery(mSql, null);
        if(mCursor.getCount() > 0){
            mCursor.close();
            ContentValues args = new ContentValues();
            args.put(col, val);
            db.update(AppConstants.PERCENT_TABLE, args
                    ,null, null);
        }else{

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(col, val);
            db.insertOrThrow(AppConstants.PERCENT_TABLE, null, values);
            db.close();
        }

    }

    private void updatePercentage(){

        Log.d("TEA","Update PErcentage");

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.PRELIM+" = '0' WHERE "+AppConstants.PRELIM+" is null ";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.MIDTERM+" = '0' WHERE "+AppConstants.MIDTERM+" is null or "+AppConstants.MIDTERM+" = ''";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_SEMIFINAL+" = '0' WHERE "+AppConstants.COL_SEMIFINAL+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_CLASS_STANDING+" = '0' WHERE "+AppConstants.COL_CLASS_STANDING+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_QUIZ+" = '0' WHERE "+AppConstants.COL_QUIZ+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_EXAM+" = '0' WHERE "+AppConstants.COL_EXAM+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_ATTENDANCE+" = '0' WHERE "+AppConstants.COL_ATTENDANCE+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_BEHAVIOR+" = '0' WHERE "+AppConstants.COL_BEHAVIOR+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_SEATWORK+" = '0' WHERE "+AppConstants.COL_SEATWORK+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();

        mSql = "UPDATE "+AppConstants.PERCENT_TABLE+" SET "+AppConstants.COL_HOMEWORK+" = '0' WHERE "+AppConstants.COL_HOMEWORK+" is null";
        mCursor = db.rawQuery(mSql, null);
        mCursor.close();
    }


    public ArrayList<Integer> getPercentage(){
        ArrayList<Integer> percent = new ArrayList<Integer>();
        mSql = "SELECT *  FROM "+AppConstants.PERCENT_TABLE+"";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.PRELIM)) != null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.PRELIM))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.MIDTERM))!=null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.MIDTERM))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_SEMIFINAL)) != null)
                 percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_SEMIFINAL))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_CLASS_STANDING))!=null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_CLASS_STANDING))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_QUIZ))!=null)
               percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_QUIZ))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_EXAM)) != null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_EXAM))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_ATTENDANCE)) != null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_ATTENDANCE))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_BEHAVIOR))!=null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_BEHAVIOR))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_SEATWORK)) != null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_SEATWORK))));
            else
                percent.add(0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_HOMEWORK)) != null)
                percent.add(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_HOMEWORK))));
            else
                percent.add(0);

        }else{
            for(int i = 0;i<10;i++){
                percent.add(0);
            }
        }
        mCursor.close();
        return percent;
    }


    public ArrayList<String> getStudIdBySection(String section){
        ArrayList<String> studId = new ArrayList<String>();

        mSql = "SELECT DISTINCT "+AppConstants.COL_STUD_ID+" FROM "+AppConstants.STUD_TABLE+" " +
                "WHERE "+AppConstants.COL_STUD_SECTION +" = '"+section+"'";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                studId.add(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_STUD_ID)));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return studId;

    }
    public ArrayList<String> getStudInfo(String studId){
        ArrayList<String> studInfos = new ArrayList<String>();
        mSql = "SELECT * FROM "+AppConstants.STUD_TABLE+" " +
                "WHERE "+AppConstants.COL_STUD_ID +" = '"+studId+"'";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            studInfos.add(mCursor.getString(mCursor
                    .getColumnIndex(AppConstants.COL_STUD_LNAME)));
            studInfos.add(mCursor.getString(mCursor
                    .getColumnIndex(AppConstants.COL_STUD_FNAME)));
            studInfos.add(mCursor.getString(mCursor
                    .getColumnIndex(AppConstants.COL_STUD_MNAME)));
            studInfos.add(mCursor.getString(mCursor
                    .getColumnIndex(AppConstants.COL_STUD_YR)));
            studInfos.add(mCursor.getString(mCursor
                    .getColumnIndex(AppConstants.COL_STUD_SECTION)));
        }
        mCursor.close();
        return studInfos;
    }

    public String getStudInfo(String studId, String find){
        String info = null;
        mSql = "SELECT "+find+" FROM "+AppConstants.STUD_TABLE+" " +
                "WHERE "+AppConstants.COL_STUD_ID +" = '"+studId+"'";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
                info = mCursor.getString(mCursor.getColumnIndex(find));
        }
        mCursor.close();
        return info;
    }

    public double getQuizGradeUsingStudId(String studId,String part,String subjCode){
        double quiz =0;
        int count = 0;
        mSql = "SELECT "+AppConstants.COL_QUIZ+" FROM "+AppConstants.QUIZ_TABLE+" " +
                "WHERE "+AppConstants.COL_PART +" = '"+part+"' AND "+AppConstants.COL_SUBJ_CODE+" = '"+subjCode+"' AND "+
                AppConstants.COL_STUD_ID+" = '"+studId+"'";

        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){

                if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_QUIZ)) != null){
                    quiz += Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_QUIZ)));
                }
                count++;
                mCursor.moveToNext();
            }
        }else{
            mCursor.close();
            return 0;
        }
        mCursor.close();
        return quiz / count;
    }

    public ArrayList<Double> getClassStandingUsingStudId(String studId,String part, String subjCode){
        ArrayList<Double> class_standing = new ArrayList<Double>();

        mSql = "SELECT "+AppConstants.COL_ATTENDANCE+","+AppConstants.COL_BEHAVIOR+","+
                AppConstants.COL_SEATWORK+","+AppConstants.COL_HOMEWORK+" FROM "+AppConstants.CLASS_STANDING_TABLE+" " +
                "WHERE "+AppConstants.COL_PART +" = '"+part+"' AND "+AppConstants.COL_SUBJ_CODE+" = '"+subjCode+"' AND "+
                AppConstants.COL_STUD_ID+" = '"+studId+"'";

        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_ATTENDANCE)) != null){
                class_standing.add(Double.parseDouble(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_ATTENDANCE))));
            }else
                class_standing.add(0.0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_BEHAVIOR)) != null){
               class_standing.add(Double.parseDouble(mCursor
                       .getString(mCursor.getColumnIndex(AppConstants.COL_BEHAVIOR))));
            }else
                class_standing.add(0.0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_SEATWORK)) != null){
               class_standing.add(Double.parseDouble(mCursor
                       .getString(mCursor.getColumnIndex(AppConstants.COL_SEATWORK))));
            }else
                class_standing.add(0.0);

            if(mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_HOMEWORK)) != null){
                class_standing.add(Double.parseDouble(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_HOMEWORK))));
            }else
                class_standing.add(0.0);
        }else{
            class_standing.add(0.0);
            class_standing.add(0.0);
            class_standing.add(0.0);
            class_standing.add(0.0);
        }
        mCursor.close();
        return class_standing;
    }

    public double getExam(String studId,String part, String subjCode){
        double exam = 0;
        mSql = "SELECT "+AppConstants.COL_EXAM+" FROM "+AppConstants.EXAM_TABLE+" " +
                "WHERE "+AppConstants.COL_PART +" = '"+part+"' AND "+AppConstants.COL_SUBJ_CODE+" = '"+subjCode+"' AND "+
                AppConstants.COL_STUD_ID+" = '"+studId+"'";

        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            if (mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_EXAM)) != null) {
                exam = Double.parseDouble(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_EXAM)));
            }else
                exam = 0;
        }
        return exam;
    }


    public int getPercentage(String col){
        int perc = 0;
        mSql = "SELECT "+col+"  FROM "+AppConstants.PERCENT_TABLE+"";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            if (mCursor.getString(mCursor.getColumnIndex(col)) != null)
                perc = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(col)));
            else
                perc = 0;
        }
        return perc;
    }

    public String getStudentGrades(String studId,String subjCode
            ,String record,String subRecord,String table){
        String grade = "";
        mSql = "SELECT "+subRecord+" FROM "+table+" " +
                "WHERE "+AppConstants.COL_STUD_ID +" = '"+studId+"' " +
                "AND "+AppConstants.COL_SUBJ_CODE +" = '"+subjCode+"' AND "+
                AppConstants.COL_PART+"= '"+ record +"'";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            grade = mCursor.getString(mCursor.getColumnIndex(subRecord));
        }
        mCursor.close();
        return grade;
    }

    public ArrayList<HashMap<String, String>> getSubjects(){
        ArrayList<HashMap<String, String>> subjList
                = new ArrayList<HashMap<String, String>>();
        mColumns = new String[]{AppConstants.COL_SUBJ_NAME,AppConstants.COL_SUBJ_CODE};
        mCursor = db.query(true,
                AppConstants.SUBJ_TABLE,
                mColumns,
                null,
                null,
                AppConstants.COL_SUBJ_CODE,
                null,
                null,
                null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.SUBJCODE,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_CODE)));
                hashMap.put(AppConstants.SUBJDESC,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_NAME)));
                subjList.add(hashMap);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return subjList;
    }

    public ArrayList<HashMap<String, String>> getStudSubjects(String studId){
        ArrayList<HashMap<String, String>> studSubjList
                = new ArrayList<HashMap<String, String>>();
        mColumns = new String[]{AppConstants.COL_SUBJ_CODE
                ,AppConstants.COL_SUBJ_SECTION};
        mWhereClause = AppConstants.COL_STUD_ID +"=?";
        mWhereArgs = new String[]{studId};
        mCursor = db.query(AppConstants.STUDSUBJ_TABLE
                ,mColumns
                ,mWhereClause
                ,mWhereArgs
                ,null
                ,null
                ,null);
        Log.d("TEA","Get stud subjects : "+mCursor.getCount());
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.SUBJCODE,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_CODE)));
                hashMap.put(AppConstants.SUBJSECTION, mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_SECTION)));
                studSubjList.add(hashMap);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return studSubjList;
    }

    public ArrayList<HashMap<String, String>> getStudentsUsingSubjects(String subjCode){
        ArrayList<HashMap<String, String>> students
                = new ArrayList<HashMap<String, String>>();

       mSql = "SELECT a."+AppConstants.COL_STUD_ID +",b."+AppConstants.COL_STUD_LNAME +"" +
               ",b."+AppConstants.COL_STUD_MNAME +",b."+AppConstants.COL_STUD_FNAME +" FROM "+AppConstants.STUDSUBJ_TABLE+" a"+
               " INNER JOIN "+AppConstants.STUD_TABLE+" b ON a."+AppConstants.COL_STUD_ID +" = b."+AppConstants.COL_STUD_ID +
               " WHERE a."+AppConstants.COL_SUBJ_CODE +" = '"+subjCode+"' ORDER BY b."+AppConstants.COL_STUD_LNAME+"";

        //Log.d("TEA","Get stud subjects : "+mCursor.getCount());
        mCursor = db.rawQuery(mSql, null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){

                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(AppConstants.STUDID,mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_ID)));
                hashMap.put(AppConstants.STUDNAME, mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_LNAME)) +", "+mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_MNAME)) +" "+mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_STUD_FNAME)));
                students.add(hashMap);
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return students;
    }



    public List<String> getSubjectsOnly(){
        List<String> list_subj = new ArrayList<String>();
        mColumns = new String[]{AppConstants.COL_SUBJ_CODE};
        mCursor = db.query(true,
                AppConstants.SUBJ_TABLE,
                mColumns,
                null,
                null,
                AppConstants.COL_SUBJ_CODE,
                null,
                null,
                null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                list_subj.add(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_CODE)));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return list_subj;
    }

    public List<String> getSectionsUsingSubj(String subjCode){
        List<String> list_sects = new ArrayList<String>();
        mColumns = new String[]{AppConstants.COL_SUBJ_SECTION};
        mWhereClause = AppConstants.COL_SUBJ_CODE +"=?";
        mWhereArgs = new String[]{subjCode};
        mCursor = db.query(true ,
                AppConstants.SUBJ_TABLE,
                mColumns,
                mWhereClause,
                mWhereArgs,
                AppConstants.COL_SUBJ_SECTION,
                null,
                null,
                null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                list_sects.add(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_SECTION)));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return list_sects;
    }

    public ArrayList<String> getSectionsUsingSubj2(String subjCode){
        ArrayList<String> list_sects = new ArrayList<String>();
        mColumns = new String[]{AppConstants.COL_SUBJ_SECTION};
        mWhereClause = AppConstants.COL_SUBJ_CODE +"=?";
        mWhereArgs = new String[]{subjCode};
        mCursor = db.query(true ,
                AppConstants.SUBJ_TABLE,
                mColumns,
                mWhereClause,
                mWhereArgs,
                AppConstants.COL_SUBJ_SECTION,
                null,
                null,
                null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()){
                list_sects.add(mCursor
                        .getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_SECTION)));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return list_sects;
    }

    public String getSubjectDesc(String subjectCode){
        String info = "";
        mSql = "SELECT * FROM "+AppConstants.SUBJ_TABLE+" " +
                "WHERE "+AppConstants.COL_SUBJ_CODE +" = '"+subjectCode+"'";
        mCursor = db.rawQuery(mSql,null);
        if(mCursor.getCount() > 0){
            mCursor.moveToFirst();
               info =  mCursor.getString(mCursor.getColumnIndex(AppConstants.COL_SUBJ_NAME));
        }
        mCursor.close();
        return info;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
