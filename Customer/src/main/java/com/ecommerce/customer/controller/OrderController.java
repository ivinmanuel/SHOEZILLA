package com.ecommerce.customer.controller;

import com.ecommerce.customer.config.CustomerDetails;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import com.ecommerce.library.service.WalletService;
import com.ecommerce.library.utils.PdfGenerator;
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


    @Autowired
    public OrderController(OrderService orderService, AddressService addressService,
                           ShoppingCartService shopCartService,WalletService walletService) {
        this.orderService = orderService;
        this.addressService = addressService;
        this.shopCartService = shopCartService;
        this.walletService = walletService;
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
    public String showOnlinePayment(Principal principal,Authentication authentication,
                                    @RequestBody Map<String, Object> data) throws RazorpayException{

        CustomerDetails customerDetails= (CustomerDetails) authentication.getPrincipal();
//        System.out.println(customerDetails.toString());
        long id=customerDetails.getCustomer_id();
        String username=principal.getName();
        String paymentMethod = data.get("paymentMethod").toString();
        Long address_id=Long.parseLong(data.get("addressId").toString());
        Double amount= Double.valueOf(data.get("amount").toString());

        System.out.println(amount);

        if(paymentMethod.equals("online_payment")) {
            ShoppingCart shoppingCart = new ShoppingCart();
            orderService.saveOrder(shoppingCart, username, address_id,paymentMethod,amount);
            RazorpayClient client = new RazorpayClient("rzp_test_0KTaWunlL4sKzR", "m8xtLRI9e6sRDuH7vmMvHaGo");
            org.json.JSONObject object = new org.json.JSONObject();
            object.put("amount", amount * 100);
            object.put("currency", "INR");
            object.put("receipt", "receipt#1");
            com.razorpay.Order order = client.orders.create(object);
            System.out.println(order);
            System.out.println(paymentMethod);
            System.out.println(address_id);
            return order.toString();

        }
        if(paymentMethod.equals("wallet")){
            Wallet wallet=walletService.findByCustomer(id);
            if(wallet.getBalance()<amount){
                org.json.JSONObject option=new org.json.JSONObject();
                option.put("status","noWallet");
                return option.toString();
            }
            else{
                ShoppingCart shopingCart = new ShoppingCart();
                orderService.saveOrder(shopingCart, username, address_id,paymentMethod,amount);
                walletService.debit(wallet,amount);
                org.json.JSONObject option=new org.json.JSONObject();
                option.put("status","wallet");
                return option.toString();
            }

        }
        else{
            ShoppingCart shopingCart = new ShoppingCart();
            orderService.saveOrder(shopingCart, username, address_id,paymentMethod,amount);

            org.json.JSONObject option=new JSONObject();
            option.put("status","cash");
            return option.toString();
        }



    }

    @PostMapping("/verify-payment")
    @ResponseBody
    public String showVerifyPayment(@RequestBody Map<String,Object> data){

        return "done";
    }
    @GetMapping("/orderListPdf1")
    public void generatePdf(HttpServletResponse response, Principal principal) throws DocumentException, IOException {

        String email=principal.getName();
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

       // List<Order> list=orderService.findOrderByCustomer(email);

        PdfGenerator pdfGenerator=new PdfGenerator();
//       // pdfGenerator.setOrders(list);
        pdfGenerator.generate(response);
    }




}
