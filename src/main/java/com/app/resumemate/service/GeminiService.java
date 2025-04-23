package com.app.resumemate.service;

import com.app.resumemate.model.ResumeRequest;

public interface GeminiService {
	
	String analyzeResume(ResumeRequest request);

}
