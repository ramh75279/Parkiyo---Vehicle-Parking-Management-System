package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.service.PaymentService;
import com.parkiyo.parkiyo.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // GET /payments/pending/{id}
    @GetMapping("/payments/pending/{id}")
    @PreAuthorize("isAuthenticated()")
    public String pendingPayment(@PathVariable Long id,
                                 Authentication auth,
                                 Model model) {
        model.addAttribute("pendingPayment", paymentService.getPendingPayment(id, auth.getName()));
        model.addAttribute("walletBalance", walletService.getBalance(auth.getName()));
        return "payments/pendingpayment";
    }

    // POST /payments/processing
    @PostMapping("/payments/processing")
    @PreAuthorize("isAuthenticated()")
    public String processPayment(@RequestParam Long paymentId,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        try {
            paymentService.initiatePayment(paymentId, auth.getName());
            return "redirect:/payments/processing/" + paymentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/pending/" + paymentId;
        }
    }

    // GET /payments/processing/{id}
    @GetMapping("/payments/processing/{id}")
    @PreAuthorize("isAuthenticated()")
    public String paymentProcessingPage(@PathVariable Long id,
                                        Authentication auth,
                                        Model model) {
        model.addAttribute("payment", paymentService.getPaymentById(id, auth.getName()));
        return "payments/paymentprocessing";
    }

    // GET /payment/success
    @GetMapping("/payment/success")
    @PreAuthorize("isAuthenticated()")
    public String paymentSuccess(@RequestParam(required = false) Long paymentId,
                                 Authentication auth,
                                 Model model) {
        model.addAttribute("payment", paymentService.getLatestSuccessfulPayment(auth.getName()));
        return "payments/paymentsuccess";
    }

    // GET /payments/history  (user)
    @GetMapping("/payments/history")
    @PreAuthorize("isAuthenticated()")
    public String userPaymentHistory(
            @RequestParam(defaultValue = "0") int page,
            Authentication auth,
            Model model) {
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Payment> paymentPage = paymentService.getUserPaymentHistoryPaginated(auth.getName(), pageable);

        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        model.addAttribute("totalPayments", paymentPage.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalSpent", paymentService.getUserTotalSpent(auth.getName()));
        return "payments/paymenthistory-user";
    }

    // GET /receipts  (list)
    @GetMapping("/receipts")
    @PreAuthorize("isAuthenticated()")
    public String receipts(Authentication auth,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("receipt", paymentService.getLatestReceipt(auth.getName()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/history";
        }
        return "payments/receipt";
    }

    // GET /receipt  (single receipt)
    @GetMapping("/receipt")
    @PreAuthorize("isAuthenticated()")
    public String receipt(@RequestParam(required = false) Long paymentId,
                          Authentication auth,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("receipt", paymentService.getReceipt(paymentId, auth.getName()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payments/history";
        }
        return "payments/receipt";
    }

    @GetMapping("/user/receipt")
    @PreAuthorize("isAuthenticated()")
    public String userReceiptShortcut(@RequestParam(required = false) Long paymentId,
                                      Authentication auth,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        return receipt(paymentId, auth, model, redirectAttributes);
    }

    // GET /receipts/{id}/pdf
    @GetMapping("/receipts/{id}/pdf")
    @PreAuthorize("isAuthenticated()")
    public String downloadReceiptPdf(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "PDF generated and downloaded successfully.");
        return "redirect:/receipt?paymentId=" + id;
    }

    // GET /receipts/{id}/email
    @GetMapping("/receipts/{id}/email")
    @PreAuthorize("isAuthenticated()")
    public String emailReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Receipt emailed to your registered email address.");
        return "redirect:/receipt?paymentId=" + id;
    }

    // ─── ADMIN ───────────────────────────────────────────────────────────────

    // GET /admin/payments  (admin - all payments)
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

    // GET /admin/payments/history
    @GetMapping("/admin/payments/history")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPaymentHistory(Model model) {
        model.addAttribute("payments", paymentService.getAllPaymentHistory());
        return "payments/paymenthistory";
    }

    // GET /admin/receipts/{id}
    @GetMapping("/admin/receipts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminReceipt(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("receipt", paymentService.getAdminReceipt(id));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/payments";
        }
        return "payments/receipt-admin";
    }

    // POST /admin/payments/{id}/refund
    @PostMapping("/admin/payments/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public String refundPayment(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            paymentService.refundPayment(id);
            redirectAttributes.addFlashAttribute("success", "Payment refunded.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/payments";
    }

    // GET /admin/receipts/{id}/pdf
    @GetMapping("/admin/receipts/{id}/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDownloadReceiptPdf(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "PDF generated and downloaded successfully.");
        return "redirect:/admin/receipts/" + id;
    }

    // GET /admin/receipts/{id}/email
    @GetMapping("/admin/receipts/{id}/email")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEmailReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Receipt emailed to customer.");
        return "redirect:/admin/receipts/" + id;
    }
}