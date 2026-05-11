package com.parkiyo.parkiyo.controller;

import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.service.PaymentService;
import com.parkiyo.parkiyo.service.UserService;
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

import com.parkiyo.parkiyo.model.Reservation;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final WalletService walletService;
    private final UserService userService;

    // ─── USER ────────────────────────────────────────────────────────────────

    // GET /payments/pending/{id}
    @GetMapping("/payments/pending/{id}")
    @PreAuthorize("isAuthenticated()")
    public String pendingPayment(@PathVariable Long id,
                                 Authentication auth,
                                 Model model) {
        var payment = paymentService.getPendingPayment(id, auth.getName());
        String email = auth.getName();
        BigDecimal walletBalance = walletService.getBalance(email);
        model.addAttribute("pendingPayment", payment);
        model.addAttribute("walletBalance", walletBalance);
        model.addAttribute("walletAfterPay", walletBalance.subtract(payment.getAmount()));

        Reservation res = payment.getReservation();
        if (res != null) {
            long diffMin = Duration.between(res.getStartTime(), res.getEndTime()).toMinutes();
            long billHours = diffMin > 0 ? (long) Math.ceil(diffMin / 60.0) : 1L;
            model.addAttribute("reservationDurationLabel", billHours == 1 ? "1 hour" : billHours + " hours");
            var slot = res.getSlot();
            if (slot != null) {
                String z = slot.getZone() != null && !slot.getZone().isBlank() ? slot.getZone() : "—";
                String floor = slot.getFloor();
                String zoneLine = "Zone " + z
                        + (floor != null && !floor.isBlank() ? " · " + floor : "");
                model.addAttribute("reservationZoneLine", zoneLine);
                model.addAttribute("slotHourlyRate", slot.getHourlyRate() != null ? slot.getHourlyRate() : BigDecimal.ZERO);
            } else {
                model.addAttribute("reservationZoneLine", "—");
                model.addAttribute("slotHourlyRate", BigDecimal.ZERO);
            }
            var v = res.getVehicle();
            if (v != null && res.getUser() != null) {
                String mm = Stream.of(v.getMake(), v.getModel())
                        .filter(s -> s != null && !s.isBlank())
                        .map(String::trim)
                        .collect(Collectors.joining(" "));
                String sub = res.getUser().getFullName() + (mm.isEmpty() ? "" : " · " + mm);
                model.addAttribute("reservationVehicleSubtitle", sub);
            } else {
                model.addAttribute("reservationVehicleSubtitle", "—");
            }
        }

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
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            Authentication auth,
            Model model) {
        String email = auth.getName();
        LocalDate fromDate = null;
        LocalDate toDate = null;
        try {
            if (from != null && !from.isBlank()) {
                fromDate = LocalDate.parse(from);
            }
            if (to != null && !to.isBlank()) {
                toDate = LocalDate.parse(to);
            }
        } catch (Exception ignored) {
            fromDate = null;
            toDate = null;
        }
        int pageSize = 6;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Payment> paymentPage = paymentService.getUserPaymentHistoryFiltered(
                email, q, status, method, fromDate, toDate, pageable);

        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        model.addAttribute("totalPayments", paymentPage.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalSpent", paymentService.getUserTotalSpent(email));
        model.addAttribute("spentThisMonth", paymentService.getUserSpentInCurrentMonth(email));
        model.addAttribute("thisMonthLabel", paymentService.getCurrentMonthDisplayLabel());
        model.addAttribute("allTimeTxnCount", paymentService.countAllUserPayments(email));
        model.addAttribute("avgPerTxn", paymentService.getUserAverageSuccessfulPayment(email));
        model.addAttribute("monthlyBars", paymentService.getLastSixMonthlySpendBars(email));
        model.addAttribute("search", q != null ? q : "");
        model.addAttribute("statusFilter", status != null ? status : "ALL");
        model.addAttribute("methodFilter", method != null ? method : "ALL");
        model.addAttribute("dateFrom", fromDate);
        model.addAttribute("dateTo", toDate);
        model.addAttribute("currentUser", userService.getUserByEmail(email));

        long total = paymentPage.getTotalElements();
        int displayFrom = total == 0 ? 0 : page * pageSize + 1;
        int displayTo = total == 0 ? 0 : page * pageSize + paymentPage.getNumberOfElements();
        model.addAttribute("displayFrom", displayFrom);
        model.addAttribute("displayTo", displayTo);

        int tp = (int) paymentPage.getTotalPages();
        List<Integer> pageNumbers = new ArrayList<>();
        if (tp > 0) {
            int window = 5;
            int start = Math.max(0, page - window / 2);
            int end = Math.min(tp - 1, start + window - 1);
            start = Math.max(0, end - window + 1);
            for (int i = start; i <= end; i++) {
                pageNumbers.add(i);
            }
        }
        model.addAttribute("pageNumbers", pageNumbers);

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