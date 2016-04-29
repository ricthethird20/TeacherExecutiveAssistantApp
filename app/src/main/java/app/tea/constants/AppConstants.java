package app.tea.constants;

/**
 * Created by PB5N0145 on 3/4/2016.
 */
public class AppConstants {

    /**
     * For main fragments
     */
    public static final String STUDNAME = "student_name";
    public static final String STUDID = "student_id";
    public static final String SUBJCODE = "subj_code";
    public static final String SUBJSECTION = "subj_section";
    public static final String SUBJDESC = "subj_desc";
    public static final String SECTNAME = "section_name";
    public static final String SECTID = "section_id";
    public static final String SECTROOM = "section_room";
    public static final String HASHQUIZ = "hash_quiz";
    public static final String HASHQUIZID = "quiz_id";
    public static final String HASHQUIZNO = "hash_quiz_no";

    /**
     * For Database
     */
    public static final String DBNAME = "TeaDb.db";

    //For Section table
    public static final String SECT_TABLE = "tbl_sections";
    public static final String COL_SECT_NAME = "SECT_NAME";
    public static final String COL_SECT_ROOM = "ROOM";

    //For Student table
    public static final String STUD_TABLE = "tbl_students";
    public static final String COL_STUD_ID = "STUD_ID";
    public static final String COL_STUD_LNAME = "LAST_NAME";
    public static final String COL_STUD_FNAME = "FIRST_NAME";
    public static final String COL_STUD_MNAME = "MIDDLE_NAME";
    public static final String COL_STUD_YR = "YEAR";
    public static final String COL_STUD_SECTION = "SECTION";

    //For Subject table
    public static final String SUBJ_TABLE = "tbl_subjects";
    public static final String COL_SUBJ_CODE = "SUBJ_CODE";
    public static final String COL_SUBJ_NAME = "SUBJ_NAME";
    public static final String COL_SUBJ_SECTION = "SUBJ_SECTION";

    public static final String STUDSUBJ_TABLE = "tbl_studsubjects";

    //For Grades
    public static final String GRADES_TABLE  = "tbl_grades";
    public static final String COL_PART = "PART";
    public static final String CP = "CLASS_PART";

    //For PARTS
    public static final String PRELIM = "PRELIM";
    public static final String MIDTERM = "MIDTERM";
    public static final String SEMIFINAL = "SEMI-FINAL";
    public static final String COL_CLASS_STANDING = "CLASSSTANDING";

    //For Quiz table
    public static final String QUIZ_TABLE = "tbl_quiz";
    public static final String COL_QUIZ = "QUIZ";
    public static final String COL_QUIZNO = "QUIZ_NO";

    //For class standing table
    public static final String CLASS_STANDING_TABLE = "tbl_class_stading";
    public static final String COL_ATTENDANCE  = "ATTENDANCE";
    public static final String COL_BEHAVIOR = "BEHAVIOR";
    public static final String COL_HOMEWORK = "HOMEWORK";
    public static final String COL_SEATWORK = "SEATWORK";

    //For exam table
    public static final String EXAM_TABLE = "tbl_exam";
    public static final String COL_EXAM = "EXAM";

    //PercentageTable
    public static final String PERCENT_TABLE = "tbl_percentage";
    public static final String COL_SEMIFINAL = "SEMIFINAL";

    //Intent extras
    public static final String EXTRA_CLASS = "CLASS";
    public static final String EXTRA_ID = "CLASS_ID";

    //Fragment Key extras
    public static final String STUDENT_KEY = "student_id";
    public static final String SUBJECT_KEY = "subject_code";
    public static final String SECTION_KEY = "section_code";

}
