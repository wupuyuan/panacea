import java.util.regex.Pattern;


public class Main {

    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z0-9]");

    public static void main(String[] args) {
        String sql = "id, create_author, create_time, update_author, update_time, deleted, version, remarks, type_name, type_code, parent_id, type_lvl, sort, allow_upload";
        printInsert(sql);
        System.out.println("=============================");
        printUpdate(sql);
    }

    private static void printInsert(String sql) {
        String[] ss = sql.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            sb.append("#{").append(camel(s.trim())).append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
    }

    private static void printUpdate(String sql) {
        String[] ss = sql.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {

            String column = s;
            String field = camel(s).trim();

            sb.append("\"<if test='").append(field).append("!=null'>\"+\n");
            sb.append("    \"").append(column).append("=#{").append(field).append("},\"+\n").append("\"</if>\"+\n");
        }
        sb.append("\"where id = #{id}\"");
        System.out.println(sb.toString());
    }


    private static final char UNDER_LINE = '_';

    public static String camel(String name) {
        if (null == name || name.length() == 0) {
            return null;
        }

        int length = name.length();
        StringBuilder sb = new StringBuilder(length);
        boolean underLineNextChar = false;

        for (int i = 0; i < length; ++i) {
            char c = name.charAt(i);
            if (c == UNDER_LINE) {
                underLineNextChar = true;
            } else if (underLineNextChar) {
                sb.append(Character.toUpperCase(c));
                underLineNextChar = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
