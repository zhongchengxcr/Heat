package com.zc.heat;

import com.google.common.collect.Maps;
import kafka.api.OffsetRequest;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 说明 . <br>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2018/03/03 下午1:36
 * <p>
 * Company: xxx
 * <p>
 *
 * @author zhongcheng_m@yeah.net
 * @version 1.0.0
 */
public class HeatTopo {

    private final static String ZK_HOST = "192.168.0.105:2181";

    private final static String TOPIC_NAME = "heat_storm";

    private final static String ZK_PATH = "/heat_storm";

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();
        BrokerHosts hosts = new ZkHosts(ZK_HOST);


        SpoutConfig spoutConfig = new SpoutConfig(hosts, TOPIC_NAME, ZK_PATH, UUID.randomUUID().toString());
        //spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        //设置偏移量


        spoutConfig.startOffsetTime = OffsetRequest.LatestTime();
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        String spoutId = KafkaSpout.class.getSimpleName();
        builder.setSpout(spoutId, kafkaSpout);
        String logProcessBoltName = LogProcessBolt.class.getSimpleName();
        builder.setBolt(logProcessBoltName, new LogProcessBolt()).shuffleGrouping(spoutId);


        Map<String, Object> jdbcConf = Maps.newHashMap();
        jdbcConf.put("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        jdbcConf.put("dataSource.url", "jdbc:mysql://localhost/demo");
        jdbcConf.put("dataSource.user", "root");
        jdbcConf.put("dataSource.password", "123456");
        ConnectionProvider connectionProvider = new HikariCPConnectionProvider(jdbcConf);
        String tableName = "bb";
        JdbcMapper simpleJdbcMapper = new SimpleJdbcMapper(tableName, connectionProvider);

        JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, simpleJdbcMapper)
                .withTableName(tableName)
                .withQueryTimeoutSecs(30);

        builder.setBolt(JdbcInsertBolt.class.getSimpleName(), userPersistanceBolt).shuffleGrouping(logProcessBoltName);

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology(HeatTopo.class.getSimpleName(), new Config(), builder.createTopology());


    }
}
