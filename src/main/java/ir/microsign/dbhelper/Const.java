package ir.microsign.dbhelper;

import android.content.Context;

/**
 * Created by Mohammad on 6/14/14.
 */
public class Const {
    public static class Database {
//        public final static String DatabaseName = getDatabaseName();
//
        public static String getDatabaseName(Context context) {
            String pkg =context.getPackageName();
            String db = pkg.substring(pkg.lastIndexOf(".") + 1);
            return db;
        }
    }

  public enum fieldType{
      INTEGER_PRIMARY_KEY_AUTOINCREMENT_NOT_NULL,
        INT,
        STRING,
        LONG,
        SHORT,
      BOOLEAN,
      INT_UNIQUE,
      STRING_UNIQUE,
      SHORT_UNIQUE,
      BOOLEAN_UNIQUE,
      LONG_UNIQUE
    }
//    public class Type {
//        public final static String STRING = "text", INT = "integer", PRIMERYKEYAUTO = "integer primary key autoincrement";
//
//    }

//    public class Separator {
//        public final static String COMMA = ",", COMDOT = ";", SPACE = " ", OPR = "(", CPR = ")", EQ = "=", SQ = "\'", DQ = "\"";
//
//    }

    public class Command {
        public final static String CreateTable = "CREATE TABLE ", DropTable = "DROP TABLE IF EXISTS ", AND = " AND ", OR = " OR ";
    }
}
