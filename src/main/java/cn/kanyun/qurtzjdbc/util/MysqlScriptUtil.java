package cn.kanyun.qurtzjdbc.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Mysql执行脚本
 * @author KANYUN
 */
public class MysqlScriptUtil {

    /**
     * 执行SQL脚本文件
     *
     * @param ip           MySQL数据库所在服务器地址IP
     * @param host         数据库端口号
     * @param userName     进入数据库所需要的用户名
     * @param password     进入数据库所需要的密码
     * @param databaseName 要导入的数据库名
     * @param filePath     SQL脚本文件目录
     * @param fileName     SQL脚本文件名称
     * @throws IOException
     */
    public static void runSqlFile(String ip, String host, String userName, String password, String databaseName, String filePath, String fileName) throws IOException {
        String cmdarray[] = {"mysql -h" + ip + " -P" + host + " -u" + userName + " -p" + password + " " + databaseName, "source " + filePath + fileName};
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
}