package com.demo.decision;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DecisionConsumer {

	private final Logger logger = LoggerFactory.getLogger(DecisionConsumer.class);
	private static final String CONSUMER_TOPIC = "CreditTopic";
	private static final String PRODUCER_TOPIC = "DecisionTopic";
	private Long minSalary = new Long(1000);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@KafkaListener(topics = CONSUMER_TOPIC, groupId = "decisionConsumer")
	public void consume(ConsumerRecord<String, String> record) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JSONObject json = (JSONObject) parser.parse(record.value());
		
		String applicationId = record.key();
		Long salary = (Long) json.get("salary");
		Boolean isInCollection = (Boolean) json.get("inCollection");
		logger.info(String.format("Reading application %s from %s : \n%s", applicationId,CONSUMER_TOPIC,json));
		
		DecisionDetails decisionDetails = calculateDecision(applicationId, salary, isInCollection);

		ProducerRecord<String, String> decisionRecord = new ProducerRecord<String, String>(PRODUCER_TOPIC,
				applicationId, mapper.writeValueAsString(decisionDetails));
		logger.info(String.format("Decision for application %s is %s",applicationId,decisionDetails.getApplicationStatus().toString()));
		kafkaTemplate.send(decisionRecord);

	}

	private DecisionDetails calculateDecision(String applicationId, Long salary, Boolean isInCollection) {
		if(salary > minSalary && !isInCollection) {
			return new DecisionDetails(applicationId, ApplcicationStatus.APPROVED, new Long(Long.sum(salary, new Long(15000))));
		}
		else
			return new DecisionDetails(applicationId, ApplcicationStatus.DECLINED,new Long(0));
	}


}