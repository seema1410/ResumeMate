package com.app.resumemate.model;

public class MatchResult {
    private String matchScore;
    private String matchedSkills;
    private String missingSkills;
    private String advice;
	public String getMatchScore() {
		return matchScore;
	}
	public void setMatchScore(String matchScore) {
		this.matchScore = matchScore;
	}
	public String getMatchedSkills() {
		return matchedSkills;
	}
	public void setMatchedSkills(String matchedSkills) {
		this.matchedSkills = matchedSkills;
	}
	public String getMissingSkills() {
		return missingSkills;
	}
	public void setMissingSkills(String missingSkills) {
		this.missingSkills = missingSkills;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}

   
}
