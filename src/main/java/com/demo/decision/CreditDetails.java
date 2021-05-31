package com.demo.decision;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditDetails {

	private String applicationId;
	private String address;
	private int salary;
	private boolean isInCollection;

}