package com.zc.heat;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Vector;

/**
 * 说明 . <br>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2018/03/03 下午2:30
 * <p>
 * Company: xxx
 * <p>
 *
 * @author zhongcheng_m@yeah.net
 * @version 1.0.0
 */
public class LogProcessBolt extends BaseRichBolt {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private OutputCollector outputCollector;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    public void execute(Tuple tuple) {

        try {
            byte[] bytes = tuple.getBinaryByField("bytes");

            String value = new String(bytes);

            logger.info(">>>>>>>>>>> revice message :{}", value);
            this.outputCollector.emit(new Values(value, value));
            this.outputCollector.ack(tuple);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("qwq", "aa"));
    }
}
