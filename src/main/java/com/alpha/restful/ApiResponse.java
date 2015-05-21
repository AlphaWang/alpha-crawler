package com.alpha.restful;

import lombok.Data;

/**
 * Created by Alpha on May/20/15.
 */
@Data
public class ApiResponse<T> {
	private String rCode;
	private String rMessage;
	private T rData;
}
