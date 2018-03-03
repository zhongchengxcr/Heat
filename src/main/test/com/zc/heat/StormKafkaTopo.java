package com.zc.heat;

import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.UUID;

/**
 * 说明 . <br>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2018/03/02 下午6:46
 * <p>
 * Company: xxx
 * <p>
 *
 * @author zhongcheng_m@yeah.net
 * @version 1.0.0
 */
public class StormKafkaTopo {

    private final static String ZK_HOST = "";

    private final static String TOPIC_NAME = "";

    private final static String ZK_PATH = "";


    public static void main(String[] args) {
        BrokerHosts hosts = new ZkHosts(ZK_HOST);
        SpoutConfig spoutConfig = new SpoutConfig(hosts, TOPIC_NAME, ZK_PATH, UUID.randomUUID().toString());
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout(KafkaSpout.class.getSimpleName(), kafkaSpout);
    }
}
