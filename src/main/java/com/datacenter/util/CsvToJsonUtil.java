package com.datacenter.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvToJsonUtil {

    /**
     * 根据文件路径读取CSV文件 返回String字符串
     *
     * @param filePath  CSV文件路径
     * @param separator CSV文件内容分隔符-默认是逗号分隔
     * @return String 字符串
     * @throws IOException IO异常
     */
//    public static String getJSONFromFile(String filePath, String separator) throws IOException {
//
//        byte[] bytes = null;
//
//        bytes = readFileByBytes(filePath);
//
//        String csv = new String(bytes, "UTF-8");
////        System.out.println(csv);
//        String jsons=getJSON(csv, separator);
//        JSONArray jsonArray = JSONArray.fromObject(jsons);
//        String str="ID,DateTime,Volume"+"\n";
//        for (int i = 0; i < jsonArray.length(); i++) {
//            str+=jsonArray.getJSONObject(i).getString("ID")
//            +','+jsonArray.getJSONObject(i).getString("DateTime")
//            +','+jsonArray.getJSONObject(i).getString("Volume")+"\n";
//        }
//        System.out.println(str);
//        return str;
//    }
//
//
//    /**
//     * 读取CSV文件内容
//     *
//     * @param content   文件
//     * @param separator 分隔符
//     * @return String 字符串
//     */
//    private static String getJSON(String content, String separator) {
//
//        StringBuilder sb = new StringBuilder("[\n");
//
//        String csv = content;
//        String[] csvValues = csv.split("\n");
//        String[] header = csvValues[0].split(separator);
//
//        for (int i = 1; i < csvValues.length; i++) {
//            sb.append("\t").append("{").append("\n");
//            String[] tmp = csvValues[i].split(separator);
//
//            for (int j = 0; j < tmp.length; j++) {
//
//                sb.append("\t").append("\t\"").append(header[j].replaceAll("\\s*", "").replace("\"", "")).append("\":\"")
//                        .append(tmp[j].replaceAll("\\s*", "").replace("\"", "")).append("\"");
//                if (j < tmp.length - 1) {
//                    sb.append(",\n");
//                } else {
//                    sb.append("\n");
//                }
//            }
//            if (i < csvValues.length - 1) {
//                sb.append("\t},\n");
//            } else {
//                sb.append("\t}\n");
//            }
//        }
//
//        sb.append("]");
//
//        return sb.toString();
//    }
//
//    /**
//     * 根据文件路径读取byte[] 数组
//     */
//    private static byte[] readFileByBytes(String filePath) throws IOException {
//        File file = new File(filePath);
//        if (!file.exists()) {
//            throw new FileNotFoundException(filePath);
//        } else {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
//            BufferedInputStream in = null;
//
//            try {
//                in = new BufferedInputStream(new FileInputStream(file));
//                short bufSize = 1024;
//                byte[] buffer = new byte[bufSize];
//                int len1;
//                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
//                    bos.write(buffer, 0, len1);
//                }
//                byte[] var7 = bos.toByteArray();
//                return var7;
//            } finally {
//                try {
//                    if (in != null) {
//                        in.close();
//                    }
//                } catch (IOException var14) {
//                    var14.printStackTrace();
//                }
//                bos.close();
//            }
//        }
//    }

//    public static void main(String[] args) {
//        CsvToJsonUtil.readFileByBytes("E:\\sample.csv");
//    }

    //读取文件
    public static String readFileByLines(String fileName) {
//        File file = new File(fileName);
//        BufferedReader reader = null;
//        String str="";
//        try {
//            reader = new BufferedReader(new FileReader(file));
//            String tempString = null;
//            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
//                str=str+tempString+'\n';
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e1) {
//                }
//            }
//        }
//        return str;
        File file = new File(fileName);
        BufferedReader reader = null;
        String str= null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString=null;
            while((tempString = reader.readLine())!=null){
                str+=tempString+'\n';
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return str;
    }

//    //读取csv文件中的数据
//    public static String readFileByChars(String fileName) {
//        File file = new File(fileName);
//        Reader reader = null;
//        String str="";
//        try {
//            // 一次读一个字符
//            reader = new InputStreamReader(new FileInputStream(file));
//            int tempchar;
//            while ((tempchar = reader.read()) != -1) {
//                str+=(char) tempchar;
//            }
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return str;
//    }
//    public static void main(String[] args) throws Exception {
//       // System.out.println(CsvToJsonUtil.readFileByChars("E:/sampleResult.csv"));
//        CsvToJsonUtil.readFileByChars("E:\\HSI2014.csv");
//    }
}
