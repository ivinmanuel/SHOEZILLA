package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.DailyEarning;
import com.ecommerce.library.dto.Monthlyearning;
import com.ecommerce.library.dto.OrderDto;
import com.ecommerce.library.dto.WeeklyEarnings;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.utils.MonthlyReportPdf;
import com.ecommerce.library.utils.PdfGenerator;
import com.ecommerce.library.utils.WeeklyPdfGenerator;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderDetailsController {
    private OrderService orderService;


    private OrderRepository orderRepository;


    @Autowired
    public OrderDetailsController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orderDetails/{pageNo}")
    public String showOrderDetails(@PathVariable("pageNo") int pageNo, Model model) {


        List<Long> orderDetailIds = orderService.findAllOrder().stream()
                .map(OrderDetails::getOrder)
                .map(Order::getId)
                .distinct()
                .collect(Collectors.toList());

        // Retrieve orders whose IDs are present in the order details
        List<Order> filteredOrders = orderService.findOrderByPageble(pageNo, 10)
                .stream()
                .filter(order -> orderDetailIds.contains(order.getId()))
                .collect(Collectors.toList());

        // Create a PageImpl from the filtered orders
        Page<Order> orders = new PageImpl<>(filteredOrders);

        OrderDto orderDto = new OrderDto();
        model.addAttribute("report", orderDto);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", orders.getTotalPages());
        return "orderDetails";


    }

    @GetMapping("/orderDetailsInfo")
    public String showOrderDetaliInfo(@RequestParam("orderId") Long orderId, Model model) {
        List<OrderDetails> orderDetails = orderService.findByOrderId(orderId);
        Order order = orderService.findById(orderId);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("order", order);
        model.addAttribute("order1", order);
        return "orderDetail-info";
    }

    @PostMapping("/updateStatus")
    public String showUpdateOrderStaus(@ModelAttribute("order1") Order order) {
        orderService.updateOrderStatus(order);
        return "redirect:/orderDetails/0";
    }


    @GetMapping("/orderStatus/{pageNo}")
    public String showOrderStatus(@PathVariable("pageNo")int pageNo,
                                  @ModelAttribute("report")OrderDto orderDto1,Model model){
        String orderStatus=orderDto1.getOrderStatus();
        Page<Order> orders = orderService.findOrderByOrderStatusPagable(pageNo,orderStatus);
        //List<Order>orders=orderService.findAll();
        OrderDto orderDto=new OrderDto();
        model.addAttribute("report",orderDto);


        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", orders.getTotalPages());
        return "orderDetails";
    }

    @GetMapping("/removeOrder")
    public String removeOrder(@RequestParam("orderId") Long orderId, Model model) {
        try {
            orderService.deleteOrderDetailsById(orderId);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return "redirect:/orderDetails/0";
    }

    @GetMapping("/pdfReport")
    public void generatePdf(@ModelAttribute("report")OrderDto orderDto, HttpServletResponse response, Principal principal) throws DocumentException, IOException {


        String value=orderDto.getPdfReport();
        if(value.equals("daily")){
            String email = principal.getName();
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            System.out.println(currentDateTime);
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            List<DailyEarning> list=orderService.dailyReport(year,month);


            PdfGenerator pdfGenerator=new PdfGenerator();
            pdfGenerator.setOrders(list);
            pdfGenerator.generate(response);

        }
        if(value.equals("weekly")){
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            System.out.println(currentDateTime);
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;


            List<WeeklyEarnings> list=orderService.findWeeklyEarnings(2024);


            WeeklyPdfGenerator pdfGenerator=new WeeklyPdfGenerator();
            pdfGenerator.setOrders(list);
            pdfGenerator.generate(response);
        }
        if(value.equals("monthly")){
            String email = principal.getName();
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            System.out.println(currentDateTime);
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);

            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);



            List<Monthlyearning> monthlyearnings = orderService.getMonthlyReport(year);

            MonthlyReportPdf monthlyReportPdf = new MonthlyReportPdf();
            monthlyReportPdf.setOrders(monthlyearnings);
            monthlyReportPdf.generate(response);
        }




    }


}