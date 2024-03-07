package org.epm.bs4.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class WebErrorController implements ErrorController {
	@GetMapping
	public String getErrorPage(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());
			String text = "";
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				text = "Page not found";
			} else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
				text = "Access unauthorized";
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				text = "Access forbidden";
			} else {
				text = "Something went wrong";
			}
			model.addAttribute("errorText", text);
			model.addAttribute("errorCode", statusCode);
		} else {
			model.addAttribute("errorText", "An unknown error has occurred");
			model.addAttribute("errorCode", "Unknown");
		}
		return "error";
	}
}
