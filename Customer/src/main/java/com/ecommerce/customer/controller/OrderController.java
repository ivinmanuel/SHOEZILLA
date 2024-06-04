package com.ecommerce.customer.controller;

import com.ecommerce.customer.config.CustomerDetails;
import com.ecommerce.library.dto.DailyEarning;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import com.ecommerce.library.service.WalletService;
import com.ecommerce.library.utils.PdfGenerator;
import com.ecommerce.library.utils.PdfGeneratorAllInvoice;
import com.lowagie.text.DocumentException;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    AddressService addressService;
    ShoppingCartService shopCartService;

    WalletService walletService;
    OrderRepository orderRepository;


    @Autowired
    public OrderController(OrderService orderService, AddressService addressService,
                           ShoppingCartService shopCartService,WalletService walletService,OrderRepository orderRepository) {
        this.orderService = orderService;
        this.addressService = addressService;
        this.shopCartService = shopCartService;
        this.walletService = walletService;
        this.orderRepository = orderRepository;
    }


    @GetMapping("/orderConfirm")
    public String showOrderConfirm(){
        return "order-confirm";
    }

    @GetMapping("/order")
    public String showOrder(@RequestParam("pageNo")int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Page<Order> orders = orderService.findOrderByCustomerPagable(pageNo,username);
        model.addAttribute("orders", orders);
       model.addAttribute("currentPage", pageNo);
       model.addAttribute("totalPage", orders.getTotalPages());

        return "orders";
    }

    @GetMapping("/orderDetails")
    public String showOrderDetails(@RequestParam("orderId") Long id, Model model) {
        List<OrderDetails> orderDetails = orderService.findByOrderId(id);
        model.addAttribute("orderDetails", orderDetails);
        return "order-details";
    }


    @PostMapping("/createPayment")
    @ResponseBody
    public String showOnlinePayment(Principal principal, Authentication authentication,
                                    @RequestBody Map<String, Object> data) throws RazorpayException {
        CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();
        long id = customerDetails.getCustomer_id();
        String username = principal.getName();
        String paymentMethod = data.get("paymentMethod").toString();
        Long address_id = Long.parseLong(data.get("addressId").toString());
        Double amount = Double.valueOf(data.get("amount").toString());

        if (!orderService.isCodAllowed(amount) && paymentMethod.equalsIgnoreCase("cash_on_delivery")) {
            JSONObject option = new JSONObject();
            option.put("status", "COD not allowed for orders above Rs 1000");
            return option.toString();
        }
        if (paymentMethod.equals("wallet")) {
            Wallet wallet = walletService.findByCustomer(id);
            if (wallet.getBalance() < amount) {
                JSONObject option = new JSONObject();
                option.put("status", "noWallet");
                return option.toString();
            }
        }


            ShoppingCart shoppingCart = new ShoppingCart();
        Order order = orderService.saveOrder(shoppingCart, username, address_id, paymentMethod, amount);

        if (paymentMethod.equals("online_payment")) {
            RazorpayClient client = new RazorpayClient("rzp_test_0KTaWunlL4sKzR", "m8xtLRI9e6sRDuH7vmMvHaGo");
            JSONObject object = new JSONObject();
            object.put("amount", amount * 100);
            object.put("currency", "INR");
            object.put("receipt", "receipt#1");
            com.razorpay.Order razorpayOrder = client.orders.create(object);

            // Update payment status to Pending
            order.setPaymentStatus("Success");
            Order newOrder = orderRepository.save(order); //

            JSONObject response = new JSONObject(razorpayOrder.toString());
            response.put("status", "created");
            response.put("newOrderId", newOrder.getId().toString());

            return response.toString();
        } else if (paymentMethod.equals("wallet")) {
            Wallet wallet = walletService.findByCustomer(id);
            orderService.saveOrder(shoppingCart, username, address_id, paymentMethod, amount);
                walletService.debit(wallet, amount);
                order.setPaymentStatus("Success");
                orderRepository.save(order);

                JSONObject option = new JSONObject();
                option.put("status", "wallet");
                return option.toString();

        } else {
            order.setPaymentStatus("Success");
            orderRepository.save(order);

            JSONObject option = new JSONObject();
            option.put("status", "cash");
            return option.toString();
        }
    }


    @PostMapping("/verify-payment")
    @ResponseBody
    public String showVerifyPayment(@RequestBody Map<String, Object> data) {
        String paymentStatus = data.get("status").toString();
        var orderId = data.get("order_id").toString();

        Order order = orderService.findById(Long.parseLong(orderId));

        if (paymentStatus.equalsIgnoreCase("success")) {
            order.setPaymentStatus("Success");
        } else {
            order.setPaymentStatus("Payment Pending");
        }

        orderRepository.save(order);

        return "{\"status\":\"done\"}";
    }



    @GetMapping("/orderListPdf1")
    public void generatePdf(HttpServletResponse response, Principal principal) throws DocumentException, IOException {
        String email = principal.getName();
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Order> orders = orderService.findOrderByCustomer(email);

        PdfGeneratorAllInvoice pdfGenerator = new PdfGeneratorAllInvoice();
        pdfGenerator.setOrders(orders);
        pdfGenerator.generate(response);
    }





}
