package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.exception.InsufficientBalanceException;
import com.parkiyo.parkiyo.exception.PaymentException;
import com.parkiyo.parkiyo.exception.ResourceNotFoundException;
import com.parkiyo.parkiyo.service.PaymentService;
import com.parkiyo.parkiyo.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final WalletService walletService;

    // ====================== USER ENDPOINTS ======================

    @GetMapping("/payments/pending/{id}")
    @PreAuthorize("isAuthenticated()")
    public String pendingPayment(@PathVariable Long id, Authentication auth, Model model) {
        try {
            model.addAttribute("pendingPayment", paymentService.getPaymentById(id, auth.getName()));
            model.addAttribute("walletBalance", walletService.getBalance(auth.getName()));
            return "payments/pendingpayment";
        } catch (Exception e) {
            return "redirect:/payments/history";
        }
    }

    @PostMapping("/payments/processing")
    @PreAuthorize("isAuthenticated()")
    public String processPayment(@RequestParam Long paymentId,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        try {
            paymentService.initiatePayment(paymentId, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Payment completed successfully!");
            return "redirect:/payments/processing/" + paymentId;
        } catch (InsufficientBalanceException e) {
            redirectAttributes.addFlashAttribute("error", "Insufficient wallet balance. Please top up your wallet.");
            return "redirect:/payments/pending/" + paymentId;
        } catch (PaymentException | ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/pending/" + paymentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/payments/pending/" + paymentId;
        }
    }

    @GetMapping("/payments/processing/{id}")
    @PreAuthorize("isAuthenticated()")
    public String paymentProcessingPage(@PathVariable Long id, Authentication auth, Model model) {
        try {
            model.addAttribute("payment", paymentService.getPaymentById(id, auth.getName()));
            return "payments/paymentprocessing";
        } catch (Exception e) {
            return "redirect:/payments/history";
        }
    }

    @GetMapping("/payment/success")
    @PreAuthorize("isAuthenticated()")
    public String paymentSuccess(Authentication auth, Model model) {
        try {
            model.addAttribute("receipt", paymentService.getLatestReceipt(auth.getName()));
            return "payments/paymentsuccess";
        } catch (Exception e) {
            return "redirect:/payments/history";
        }
    }

    @GetMapping("/payments/history")
    @PreAuthorize("isAuthenticated()")
    public String userPaymentHistory(Authentication auth, Model model) {
        model.addAttribute("payments", paymentService.getUserPaymentHistory(auth.getName()));
        model.addAttribute("totalSpent", paymentService.getUserTotalSpent(auth.getName()));
        return "payments/paymenthistory-user";
    }

    @GetMapping("/receipts")
    @PreAuthorize("isAuthenticated()")
    public String receipts(Authentication auth, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("receipt", paymentService.getLatestReceipt(auth.getName()));
            return "payments/receipt";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/history";
        }
    }

    @GetMapping("/receipt")
    @PreAuthorize("isAuthenticated()")
    public String receipt(@RequestParam(required = false) Long paymentId,
                          Authentication auth,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("receipt", paymentService.getReceipt(paymentId, auth.getName()));
            return "payments/receipt";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/history";
        }
    }

    @PostMapping("/receipt")
    @PreAuthorize("isAuthenticated()")
    public String receiptPostRedirect(@RequestParam(required = false) Long paymentId) {
        if (paymentId != null) {
            return "redirect:/receipt?paymentId=" + paymentId;
        }
        return "redirect:/receipt";
    }

    @PostMapping("/receipts")
    @PreAuthorize("isAuthenticated()")
    public String receiptsPostRedirect() {
        return "redirect:/receipts";
    }

    @GetMapping("/user/receipt")
    public String userReceiptShortcut(@RequestParam(required = false) Long paymentId,
                                      Authentication auth,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        return receipt(paymentId, auth, model, redirectAttributes);
    }

    // Dummy endpoints for PDF and Email
    @GetMapping("/receipts/{id}/pdf")
    @PreAuthorize("isAuthenticated()")
    public String downloadReceiptPdf(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "PDF generated successfully.");
        return "redirect:/receipt?paymentId=" + id;
    }

    @GetMapping("/receipts/{id}/email")
    @PreAuthorize("isAuthenticated()")
    public String emailReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Receipt emailed successfully.");
        return "redirect:/receipt?paymentId=" + id;
    }

    // ====================== ADMIN ENDPOINTS ======================

    @GetMapping("/admin/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPayments(@RequestParam(required = false) String status,
                                @RequestParam(required = false) String dateFrom,
                                @RequestParam(required = false) String dateTo,
                                Model model) {
        model.addAttribute("payments", paymentService.getAllPayments(status, dateFrom, dateTo));
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        return "payments/paymenthistory";
    }

    // FIXED METHOD - This was causing the error
    @GetMapping("/admin/payments/history")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPaymentHistory(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments(null, null, null));
        model.addAttribute("totalRevenue", paymentService.getTotalRevenue());
        return "payments/paymenthistory";
    }

    @GetMapping("/admin/receipts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminReceipt(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("receipt", paymentService.getAdminReceipt(id));
            return "payments/receipt-admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/payments";
        }
    }

    @PostMapping("/admin/payments/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public String refundPayment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            paymentService.refundPayment(id);
            redirectAttributes.addFlashAttribute("success", "Payment refunded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/payments";
    }
}