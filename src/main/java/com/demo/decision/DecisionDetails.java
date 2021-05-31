package com.demo.decision;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecisionDetails {

	private String applicationId;
	private ApplcicationStatus applicationStatus;
	private Long approvedAmount;

}