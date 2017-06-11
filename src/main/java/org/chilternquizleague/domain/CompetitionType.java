package org.chilternquizleague.domain;

public enum CompetitionType {

	LEAGUE("League") ,
	BEER("Beer Leg") ,
	INDIVIDUAL("Individual Quiz") , 
	CUP("Knockout Cup") ,
	PLATE("Plate") ,
	BUZZER("Buzzer Quiz");

	private final String description;

	private CompetitionType(String description) {
		this.description = description;
	}


	public String description() {
		return description;
	}

}
