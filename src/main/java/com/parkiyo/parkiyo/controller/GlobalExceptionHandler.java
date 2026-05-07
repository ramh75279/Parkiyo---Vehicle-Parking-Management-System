package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.exception.BadRequestException;
import com.parkiyo.parkiyo.exception.ResourceAlreadyExistsException;
import com.parkiyo.parkiyo.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Resource not found — e.g. user ID doesn't exist
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    // Bad input — e.g. invalid role, wrong password
    @ExceptionHandler(BadRequestException.class)
    public String handleBadRequest(BadRequestException ex, Model model) {
        log.warn("Bad request: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Bad Request");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/400";
    }

    // Duplicate — e.g. email already exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public String handleAlreadyExists(ResourceAlreadyExistsException ex, Model model) {
        log.warn("Resource already exists: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Already Exists");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/400";
    }

    // Catch-all for anything unexpected
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        return "error/500";
    }
}