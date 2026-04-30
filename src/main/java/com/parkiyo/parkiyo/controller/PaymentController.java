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

    // ─── USER ────────────────────────────────────────────────────────────────

    @GetMapping("/payments/pending/{id}")
    @PreAuthorize("isAuthenticated()")
    public String pendingPayment(@PathVariable Long id,
                                 Authentication auth,
                                 Model model) {
        try {
            model.addAttribute("pendingPayment", paymentService.getPaymentById(id, auth.getName()));
            model.addAttribute("walletBalance", walletService.getBalance(auth.getName()));
            return "payments/pendingpayment";
        } catch (ResourceNotFoundException | PaymentException e) {
            // You can add error handling here if needed
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
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
            return "redirect:/payments/pending/" + paymentId;
        }
    }

    @GetMapping("/payments/processing/{id}")
    @PreAuthorize("isAuthenticated()")
    public String paymentProcessingPage(@PathVariable Long id,
                                        Authentication auth,
                                        Model model) {
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
            model.addAttribute("payment", paymentService.getLatestReceipt(auth.getName())); // Using receipt as success view
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

    @GetMapping("/user/receipt")
    @PreAuthorize("isAuthenticated()")
    public String userReceiptShortcut(@RequestParam(required = false) Long paymentId,
                                      Authentication auth,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        return receipt(paymentId, auth, model, redirectAttributes);
    }

    // Dummy PDF and Email endpoints (for now)
    @GetMapping("/receipts/{id}/pdf")
    @PreAuthorize("isAuthenticated()")
    public String downloadReceiptPdf(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "PDF generated and downloaded successfully.");
        return "redirect:/receipt?paymentId=" + id;
    }

    @GetMapping("/receipts/{id}/email")
    @PreAuthorize("isAuthenticated()")
    public String emailReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Receipt emailed to your registered email address.");
        return "redirect:/receipt?paymentId=" + id;
    }

    // ─── ADMIN ───────────────────────────────────────────────────────────────

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

    @GetMapping("/admin/payments/history")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPaymentHistory(Model model) {
        model.addAttribute("payments", paymentService.getAllPaymentHistory()); // Keep if you still have this method
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
            return "redirect:/admin/payments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/payments";
        }
    }

    @GetMapping("/admin/receipts/{id}/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDownloadReceiptPdf(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "PDF generated and downloaded successfully.");
        return "redirect:/admin/receipts/" + id;
    }

    @GetMapping("/admin/receipts/{id}/email")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEmailReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Receipt emailed to customer.");
        return "redirect:/admin/receipts/" + id;
    }
}