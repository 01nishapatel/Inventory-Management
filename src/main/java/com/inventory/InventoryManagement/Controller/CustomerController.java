package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Models.*;
import com.inventory.InventoryManagement.Orderserviceimpl;
import com.inventory.InventoryManagement.Repositary.*;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import groovy.util.logging.Slf4j;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
public class CustomerController {

    int custid;
    int orderid;

    @Autowired
    private CustomerRepo cr;

    @Autowired
    private ProductRepo pr;

    @Autowired
    private Orderserviceimpl orderservice;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private  CompanyDetailsRepository companyDetailsRepository;
    @Autowired
    private OrderDetailRepo odr;

    @Autowired
    private ProductTransactionRepository productTransactionRepository;

    private void loadProductAttributeValues(Model model) {

        model.addAttribute("categories", cr.findAll());
        model.addAttribute("products",pr.findAll());
    }
    @RequestMapping( value = "/addcustomer", method = {RequestMethod.GET})
    public String showcustomerform(@ModelAttribute("customer") Customer customer, Model model) {
        // loadProductAttributeValues(model);
        model.addAttribute("customer",new Customer());
        return "Customer.html";
    }

    @RequestMapping(value = "/savecustomer",method={RequestMethod.POST,RequestMethod.GET})
    public String saveCustomer(Model model, @Valid @ModelAttribute("customer")Customer customer, Errors errors, RedirectAttributes redirAttrs) {
        if (errors.hasErrors()) {
            //log.error("Supplier Form Validation due to : " + errors.toString());
            redirAttrs.addFlashAttribute("error",errors.toString());
            return "Customer.html";
        }
        Customer custdata= cr.findByEmail(customer.getEmail());
        Customer custdata2=cr.findByPhonenumber(customer.getPhonenumber());
        if(custdata2!=null)
        {
            model.addAttribute("phnoerror","Customer with this contact number already exists !!!");
        }
        else if(custdata!=null)
        {
            model.addAttribute("emailerror","Customer with this email id already exists !!!");
        }
        else {
            cr.save(customer);
            redirAttrs.addFlashAttribute("success","Customer Added Successfully");
            return "redirect:/getallcustomer";
        }
        return "Customer.html";

    }

    @RequestMapping(value="/getallcustomer",method = GET)
    public String getAllCustomers(Model model)
    {
        model.addAttribute("customers",cr.findAll());
        return "customerlist.html";
    }

    @RequestMapping(value = "/updatecustomer/{id}",method = {RequestMethod.GET,RequestMethod.POST})
    public String getcustomerbyid(@PathVariable("id") int customerid, Model model)
    {
        Optional<Customer> p=cr.findById(customerid);

        model.addAttribute("updcust", p.get());
        // model.addAttribute("updproduct",new ProductMaster());


        return "updatecustomer.html";
    }

    @RequestMapping("/updcustomer/{id}")
    public String updaetCustomer(@PathVariable("id") int customerid, Customer c, BindingResult result , Model model)
    {
        if(result.hasErrors())
        {
            c.setCustomerID(customerid);
            return "updatecustomer.html";
        }
        c.CustomerID=customerid;
        cr.save(c);
        model.addAttribute("customers",cr.findAll());
        return "redirect:/getallcustomer";
    }

    @GetMapping("/deleteCustomer/{id}")
    public String deletCustomer(@PathVariable("id") int id, Model model) {
        Customer cust = cr.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<Order> orders=cust.getOrders();
        List<ProductTransaction> productTransactions=cust.getProductTransactions();
        productTransactionRepository.deleteAll(productTransactions);
        orderRepo.deleteAll(orders);
        cr.delete(cust);
        return "redirect:/getallcustomer";
    }

    @GetMapping("/newinvoice/{id}")
    public String DisplayHomePage(@PathVariable("id") int id,@ModelAttribute("o") Order o,@ModelAttribute("od") OrderDetail od , Model model){
        loadProductAttributeValues(model);
        Optional<Customer> c=cr.findById(id);
        Customer cust=c.get();
        custid=cust.getCustomerID();
        model.addAttribute("custname",cust.getCustomerName());
        model.addAttribute("address",cust.getCustomerAddress());
        model.addAttribute("email",cust.getEmail());
        model.addAttribute("phno",cust.getPhonenumber());

//        Order ordercart=c.get().getOrders();
//        if(ordercart==null)
//        {
//            model.addAttribute("check","No items added");
//        }
        return "NewInvoice.html";
    }

    @RequestMapping(value = "/saveorder",method = {RequestMethod.GET,RequestMethod.POST})
    public String addItemToCart(Model model, Order o1, OrderDetail od1,@ModelAttribute("od") OrderDetail od){

        loadProductAttributeValues(model);
        Optional<Customer> c =  cr.findById(custid);
        Customer cust = c.get();
        model.addAttribute("custname",cust.getCustomerName());
        model.addAttribute("address",cust.getCustomerAddress());
        model.addAttribute("email",cust.getEmail());
        model.addAttribute("phno",cust.getPhonenumber());
       Order o=new Order();
        if(od1.getProducts() == null)
        {
            Optional<Order> order = orderRepo.findById(orderid);
            Order neworder = order.get();
            model.addAttribute("shoopingcart", neworder.getOrderDetails());
            model.addAttribute("orderidd", neworder.getOrderID());
            return "newinvoice";
        }
        Optional<Product> product=pr.findById(od1.getProducts().ProductID);
        Product pro=product.get();
        if(od1.getQuantity() == 0)
        {
            model.addAttribute("errormsg2","Invalid Quantity");
        }
//        if(pro.getProductQuantity() < 5)
//        {
//            model.addAttribute("errormsg","Your selected product has less than 5 quantity...");
//        }
        if(od1.getQuantity() > pro.getProductQuantity())
        {
            model.addAttribute("erromsg1","Only " + pro.getProductQuantity() + " " + pro.getProductName() + " Available !!");
        }
        if(pro.getProductQuantity()==0)
        {
            model.addAttribute("errormsg3","Your selected product has out of stock !!");
        }
        if(od1.getQuantity() <= pro.getProductQuantity() && od1.getQuantity()!=0 && pro.getProductQuantity() > 0)
        {
            try {
                if(orderid==0) {
                    Order order = orderservice.addItem(pro, od1.getQuantity(), cust, orderid);
                    order.setOrderID(order.getOrderID());
                    double totalpriceoforder=order.getTotalPrice();
                    double totalunitprice=0.0;
                    for (OrderDetail item : order.getOrderDetails()) {

                        totalunitprice += item.getUnitPrice();
                    }
                    double finaltax=totalpriceoforder - totalunitprice;
                    order.setTotalTax((float) finaltax);
                    orderRepo.save(order);
                    orderid = order.getOrderID();
                }
                else
                {
                    Order order = orderservice.addItem(pro, od1.getQuantity(), cust, orderid);
                    order.setOrderID(order.getOrderID());
                    double totalpriceoforder=order.getTotalPrice();
                    double totalunitprice=0.0;
                    for (OrderDetail item : order.getOrderDetails()) {

                        totalunitprice += item.getUnitPrice();
                    }
                    double finaltax=totalpriceoforder - totalunitprice;
                    order.setTotalTax((float) finaltax);
                    orderRepo.save(order);
                }
            }catch (Exception e)
            {
                e.getCause();
            }

            //log.info(order.toString());

        }
        if(orderid > 0) {
            Optional<Order> order = orderRepo.findById(orderid);
            Order order1 = order.get();
            model.addAttribute("shoopingcart", order1.getOrderDetails());
            model.addAttribute("orderidd", order1.getOrderID());
        }
        return "newinvoice";
    }

    @GetMapping("/invoice/{id}")
    public String DisplayInvoice(@PathVariable("id") int id, Model model,Authentication authentication){
        orderid=0;


        User user=userRepository.finddByUserName(authentication.getName());
        CompanyDetails cm=user.getCompanies();
        Optional<Order> o =  orderRepo.findById(id);
        Order order = o.get();
        String tax=order.getTotalTax() + "%";
        float subtotal=0;
        for (OrderDetail item : o.get().getOrderDetails()) {

            subtotal += item.getUnitPrice();
        }

        String da=order.getOrderDate();
        String da1=da.substring(0,10);

        model.addAttribute("comapnyname",cm.getCompanyName());
        model.addAttribute("companyadd",cm.getCompanyAddress());
        model.addAttribute("comapnaynumber",cm.getPhoneNo());
        model.addAttribute("companyemail",cm.getCompanyEmail());


        model.addAttribute("customername",order.getCustomers().getCustomerName());
        model.addAttribute("address",order.getCustomers().getCustomerAddress());
        model.addAttribute("phno",order.getCustomers().getPhonenumber());
        model.addAttribute("email",order.getCustomers().getEmail());
        model.addAttribute("invoiceid",order.getOrderID());
        model.addAttribute("orderdata",order.getOrderDetails());
        model.addAttribute("total",order.getTotalPrice());
        model.addAttribute("totaltax",order.getTotalTax());
        model.addAttribute("subtotal",subtotal);
        model.addAttribute("date",da1);
        return "Invoice.html";
    }

    @GetMapping("/removebyorderdetailid/{id}")
    public String removebyorderdetailid(@PathVariable("id") int id, Model model) {
       Optional <OrderDetail> orderDetail = odr.findById(id);
       OrderDetail od=orderDetail.get();
       Product p=od.getProducts();
       int q=od.getProducts().getProductQuantity();
        int qty=q + p.getProductQuantity();
        p.setProductID(p.getProductID());
        p.setProductQuantity(qty);
        pr.save(p);
        Order order1=od.getOrders();
        double totprice=order1.getTotalPrice() - od.getTotalamount();
        double t=od.getTotalamount() - od.getUnitPrice();
        double tax=order1.getTotalTax() - t;
        order1.setOrderID(order1.getOrderID());
        order1.setTotalPrice((float) totprice);
        order1.setTotalTax((float) tax);
        orderRepo.save(order1);
        odr.delete(od);
        Optional<Order> order = orderRepo.findById(orderid);
        Order o = order.get();
        model.addAttribute("shoopingcart", o.getOrderDetails());
        return "redirect:/saveorder";
    }
    @GetMapping("/getinvoicebycustomerid/{id}")
    public String getinvoicebycustomerid(@PathVariable("id") int id,Model model) {
       Optional<Customer> customers=cr.findById(id);
       Customer c=customers.get();
       List<Order> o=c.getOrders();
       model.addAttribute("odata",o);
        return "orderhistorycustomer.html";
    }

    @GetMapping("/invoicebyorderid/{id}")
    public String invoicebyorderid(@PathVariable("id") int id, Model model,Authentication authentication){
        Optional<Order> o =  orderRepo.findById(id);
        Order order = o.get();
        float subtotal=0;
        for (OrderDetail item : o.get().getOrderDetails()) {

            subtotal += item.getUnitPrice();
        }
        String da=order.getOrderDate();
        String da1=da.substring(0,10);
        User user=userRepository.finddByUserName(authentication.getName());
        CompanyDetails cm=user.getCompanies();
        model.addAttribute("comapnyname",cm.getCompanyName());
        model.addAttribute("companyadd",cm.getCompanyAddress());
        model.addAttribute("comapnaynumber",cm.getPhoneNo());
        model.addAttribute("companyemail",cm.getCompanyEmail());




        model.addAttribute("customername",order.getCustomers().getCustomerName());
        model.addAttribute("address",order.getCustomers().getCustomerAddress());
        model.addAttribute("phno",order.getCustomers().getPhonenumber());
        model.addAttribute("email",order.getCustomers().getEmail());
        model.addAttribute("invoiceid",order.getOrderID());
        model.addAttribute("orderdata",order.getOrderDetails());
        model.addAttribute("total",order.getTotalPrice());
        model.addAttribute("totaltax",order.getTotalTax());
        model.addAttribute("subtotal",subtotal);
        model.addAttribute("date",da1);
        return "Invoice.html";
    }

    @GetMapping("/submitpayment/{id}")
    public String submitpayment(@PathVariable("id") int id){
        Optional<Order> o =  orderRepo.findById(id);
        Order order = o.get();
        order.setPaymentType("Offline");
        order.setOrderID(order.getOrderID());
        orderRepo.save(order);
        return "redirect:/invoicebyorderid/"+order.getOrderID();
    }





}
