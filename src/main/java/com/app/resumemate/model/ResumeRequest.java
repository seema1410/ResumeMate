package com.app.resumemate.model;

public class ResumeRequest {
	
	private String resumeText;
    private String jobDescription;

    public ResumeRequest(String resumeText, String jobDescription) {
        this.resumeText = resumeText;
        this.jobDescription = jobDescription;
    }

    public String getResumeText() {
        return resumeText;
    }

    public String getJobDescription() {
        return jobDescription;
    }

}
