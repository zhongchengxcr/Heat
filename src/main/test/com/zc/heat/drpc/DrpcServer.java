package com.zc.heat.drpc;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 说明 . <br>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2018/03/01 20:59
 * <p>
 * Company: 百趣
 * <p>
 *
 * @author zhongcheng_m@yeah.net
 * @version 1.0.0
 */
public class DrpcServer {

    public static void main(String[] args) {

        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("user");
        builder.addBolt(new UserBolt(), 2);

        LocalDRPC localDRPC = new LocalDRPC();
        LocalCluster localCluster = new LocalCluster();

        localCluster.submitTopology("drpc-user", new Config(), builder.createLocalTopology(localDRPC));


        String response = localDRPC.execute("user", "zhongc");

        System.out.println("============================================ " + response);
        localCluster.shutdown();
        localDRPC.shutdown();


//        DRPCClient client = new DRPCClient("drpc-host", 3772);
//        String result = client.execute("reach", "http://twitter.com");


    }


    static class UserBolt extends BaseRichBolt {


        private OutputCollector outputCollector;

        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            this.outputCollector = collector;
        }

        public void execute(Tuple input) {

            Object requestId = input.getValue(0);
            String name = input.getString(1);

            System.out.println("============Receive requestId:" + requestId);
            System.out.println("============Receive name:" + name);

            outputCollector.emit(new Values(requestId, "add user success,name:" + name));


        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "response"));
        }
    }
}
