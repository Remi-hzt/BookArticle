package com.example.book_novel.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
@Configuration
public class GridFSConfig{
    private static Properties properties;
    private static InputStream stream = null;
    private static String host1;
    private static String port;
    private static String dbname;
    private static String username;
    private static String password;
    private static String source;
    //1.创建一个静态代码块，用于初始化工具类中的静态变量，该静态代码块在类加载过程中的初始化阶段执行，并且只执行一次
    static {
        //判断properties集合对象是否为空，为空则创建一个集合对象
        if (properties == null) {
            properties = new Properties();
        }
		/*
		由于我们调用load方法，而load方法底层抛出了一个IOException异常，此异常为编译时期异常
		所以，我们调用load方法时，需要处理底层抛过来的异常
         */
        try {
            //创建一个InputStream字节输入流对象，用于接收mongodb.properties配置文件中的配置参数
            stream = GridFSConfig.class.getClassLoader().getResourceAsStream
                    ("mongodb.properties");
            //properties集合对象调用load()方法，将配置参数加载到properties集合中
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据mongodb.properties配置文件中的key，获取value值
        host1 = properties.getProperty("host1");
        port = properties.getProperty("port");
        dbname = properties.getProperty("dbname");
        source = properties.getProperty("source");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }
    //2.定义一个getMongoClient()方法，用于获取MongoDB副本集的连接对象
    public static MongoClient getMongoClient() {
        //指定用户名、用户认证书库、密码进行身份验证
        MongoCredential credential = MongoCredential
                .createCredential(username, source, password.toCharArray());
        //连接mongodb副本集
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(
                                        Arrays.asList(
                                                new ServerAddress(host1, 40000),
                                                new ServerAddress(host1, 40001),
                                                new ServerAddress(host1, 40002)
                                        )))
                        .credential(credential)
                        .build());
        return mongoClient;
    }
    @Bean
    //3.定义一个gridFSBucket方法，用于实现连接指定的MongoDB GridFS中的数据库
    public GridFSBucket gridFSBucket() {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbname);
        //获取GridFS中数据库连接
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase);
        return gridFSBucket;
    }

}