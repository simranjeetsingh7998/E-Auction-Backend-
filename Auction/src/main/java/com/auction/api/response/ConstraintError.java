package com.auction.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ConstraintError {

	private String field;

	private String message;

}
