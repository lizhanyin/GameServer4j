package org.mmo.bill.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class BillingController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/billing")
    public Billing getBilling(@RequestParam(value = "info", defaultValue = "账单") String info) {
        return new Billing(counter.incrementAndGet(), "您的账单信息：" + info);
    }
}
