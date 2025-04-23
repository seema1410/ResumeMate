package com.app.resumemate.controller;

import com.app.resumemate.service.GeminiService;
import com.app.resumemate.model.ResumeRequest;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ResumeController {

	@Autowired
	private GeminiService geminiService;

	// First page Resume upload page
	@GetMapping("/upload")
	public String showUploadForm() {
		return "uploadForm"; // Upload page will be shown
	}

	@PostMapping("/submit")
	public String handleSubmit(@RequestParam("resume") MultipartFile resume,
			@RequestParam("jobDescription") String jobDescription, Model model) {
		String resumeText = "";

		try {
			Tika tika = new Tika();
			resumeText = tika.parseToString(resume.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Failed to process the resume.");
			return "errorPage";
		}

		ResumeRequest request = new ResumeRequest(resumeText, jobDescription);

		// Gemini response 
		String fullHtmlResponse = geminiService.analyzeResume(request);

		// Extract sections from divs using helper method
		String scoreSection = extractBetween(fullHtmlResponse, "score");
		String matchedSection = extractBetween(fullHtmlResponse, "matched");
		String missingSection = extractBetween(fullHtmlResponse, "missing");
		String suggestionSection = extractBetween(fullHtmlResponse, "suggestions");

		// Send each section to model
		model.addAttribute("score", scoreSection);
		model.addAttribute("matched", matchedSection);
		model.addAttribute("missing", missingSection);
		model.addAttribute("suggestions", suggestionSection);

		return "resultPage";
	}

	private String extractBetween(String html, String divId) {
		String startTag = "<div id='" + divId + "'>";
		String endTag = "</div>";
		int start = html.indexOf(startTag);
		int end = html.indexOf(endTag, start);
		if (start != -1 && end != -1) {
			return html.substring(start + startTag.length(), end).trim();
		}
		return "Not Available";
	}

}
