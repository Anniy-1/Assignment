import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class StudCrud {
    private String jdbcURL = "jdbc:mysql://localhost:3306/remotemysql.com";
    private String jdbcUsername = "1lAIHXc8Va";
    private String jdbcPassword = "TEMA3CTgGO";

    private static final String INSERT_STUDENT_SQL = "INSERT INTO STUDENT" + "  (student_name, student_dob, student_doj) VALUES " +
        " (?, ?, ?);";

    private static final String SELECT_STUDENT_BY_ID = "select student_no, student_name, student_dob, student_doj from STUDENT where Student_no =?";
    private static final String SELECT_ALL_STUDENT = "select * from STUDENT";
    private static final String DELETE_STUDENT_SQL = "delete from STUDENT where student_no = ?;";
    private static final String UPDATE_STUDENT_SQL = "update STUDENT set student_name= ?, student_dob =?, student_doj=? where student_no = ?;";

    public StudCrud() {}

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
           
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            
            e.printStackTrace();
        }
        return connection;
    }

    public void insertStudent(Student student) throws SQLException {
        System.out.println(INSERT_STUDENT_SQL);
        
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT_SQL)) {
            preparedStatement.setString(1, STUDENT.getStudent_name());
            preparedStatement.setDate(2, STUDENT.getStudent_dob());
            preparedStatement.setDate(3, STUDENT.getStudent_doj());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Student selectStudent(int id) {
        Student student = null;
        
        try (Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            
            ResultSet rs = preparedStatement.executeQuery();

            
            while (rs.next()) {
                String student_name = rs.getString("student_name");
                String student_dob = rs.getDate("student_dob");
                String student_doj = rs.getDate("student_doj");
                Student = new Student(student_no, student_name, student_dob, student_doj);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return student;
    }

    public List < Student > selectAllStudent() {
        
        List < Student > student = new ArrayList < > ();
        
        try (Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_STUDENT);) {
            System.out.println(preparedStatement);
            
            ResultSet rs = preparedStatement.executeQuery();

           
            while (rs.next()) {
                int student_no = rs.getInt("student_no");
                String student_name = rs.getString("student_name");
                String student_dob = rs.getDate("student_dob");
                String student_doj = rs.getDate("student_doj");
                student.add(new Student(student_no, student_name, student_dob, student_doj));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return student;
    }

    public boolean deleteStudent(int student_no) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_SQL);) {
            statement.setInt(1, student_no);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateStudent(Student student) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_SQL);) {
            statement.setString(1, student.getStudent_name());
            statement.setDate(2, student.getStudent_dob());
            statement.setDate(3, Student.getStudent_doj());
            statement.setInt(4, student.getStudent_no());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
