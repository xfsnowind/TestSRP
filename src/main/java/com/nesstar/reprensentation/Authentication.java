package com.nesstar.reprensentation;

import java.math.BigInteger;

public class Authentication {
	private BigInteger A;
	private BigInteger salt;
	private BigInteger B;
	
	
	public BigInteger getB() {
		return B;
	}
	public void setB(BigInteger b) {
		B = b;
	}
	private String S;
	
	
	public BigInteger getA() {
		return A;
	}
	public void setA(BigInteger a) {
		A = a;
	}
	public BigInteger getSalt() {
		return salt;
	}
	public void setSalt(BigInteger salt) {
		this.salt = salt;
	}
	public String getS() {
		return S;
	}
	public void setS(String s) {
		S = s;
	}
}
