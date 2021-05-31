package com.demo.decision;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApplcicationStatus {
	APPROVED("APPROVED"), DECLINED("DECLINED");

	@Getter
	private String value;
}
