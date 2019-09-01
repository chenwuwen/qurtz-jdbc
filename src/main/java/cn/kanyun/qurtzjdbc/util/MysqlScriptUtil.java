package cn.kanyun.qurtzjdbc.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Mysql执行脚本
 *
 * @author KANYUN
 */
public class MysqlScriptUtil {

    private static Connection conn = null;
    private static Statement stmt = null;

    /**
     * 执行SQL脚本文件
     *
     * @param ip           MySQL数据库所在服务器地址IP
     * @param port         数据库端口号
     * @param userName     进入数据库所需要的用户名
     * @param password     进入数据库所需要的密码
     * @param databaseName 要导入的数据库名
     * @param filePath     SQL脚本文件目录
     * @param fileName     SQL脚本文件名称
     * @throws IOException
     */
    public static void runSqlFile(String ip, String port, String userName, String password, String databaseName, String filePath, String fileName) throws IOException {
        String cmdarray[] = {"mysql -h" + ip + " -P" + port + " -u" + userName + " -p" + password + " " + databaseName, "source " + filePath + fileName};
        Runtime runtime = Runtime.getRuntime();
        OutputStream os = null;
        OutputStreamWriter writer = null;
        try {
            //cmd之后执行数组的第一个条件进入数据库
            Process process = runtime.exec("cmd /c " + cmdarray[0]);
            //执行了第一条命令以后已经登录到mysql了
            os = process.getOutputStream();
            writer = new OutputStreamWriter(os);
            //向图形界面输出第二第三条命令。中间 \r\n  作用是用来换行的
            writer.write(cmdarray[1]);
            writer.flush();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 创建数据库
     *
     * @param driverName   驱动名
     * @param url          连接Url 带端口 jdbc方式
     * @param userName     用户名
     * @param password     密码
     * @param databaseName 待创建的数据库名称
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void createDatabase(String driverName, String url, String userName, String password, String databaseName) throws ClassNotFoundException, SQLException {


        //STEP 2: Register JDBC driver
        Class.forName(driverName);

        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(url, userName, password);

        //STEP 4: Execute a query
        System.out.println("Creating database...");
        stmt = conn.createStatement();

//        创建数据库命令,并设置字符集和排序规则
        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName + " default charset utf8mb4 COLLATE utf8mb4_general_ci;";
        stmt.executeUpdate(sql);
        System.out.println("Database created successfully...");
    }

    /**
     * 执行数据库脚本
     *
     * @param driverName 驱动名
     * @param url        连接Url 带端口 jdbc方式 此url携带数据名
     * @param userName   用户名
     * @param password   密码
     * @param command    脚本命令
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void handlerDatabaseScript(String driverName, String url, String userName, String password, String command) throws ClassNotFoundException, SQLException {

        //STEP 2: Register JDBC driver
        Class.forName(driverName);

        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(url, userName, password);

        System.out.println("get statement...");
        stmt = conn.createStatement();

        //STEP 4: Execute a query
        stmt.executeUpdate(command);
        System.out.println("Database execute successfully...");
    }


    public static void main(String[] args) {
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/";
        String userName, password, databaseName;
        userName = "root";
        password = "root";
        databaseName = "test2";
        try {
            createDatabase(driverName, url, userName, password, databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}