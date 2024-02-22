package com.inventory.InventoryManagement.Controller;

import com.inventory.InventoryManagement.Constants.InventoryManagementConstant;
import com.inventory.InventoryManagement.Models.Order;
import com.inventory.InventoryManagement.Models.ProductTransaction;
import com.inventory.InventoryManagement.Models.User;
import com.inventory.InventoryManagement.Repositary.OrderRepo;
import com.inventory.InventoryManagement.Repositary.ProductTransactionRepository;
import com.inventory.InventoryManagement.Repositary.SubscriptionRepo;
import com.inventory.InventoryManagement.Repositories.UserRepository;
import com.inventory.InventoryManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private final UserService userService;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductTransactionRepository productTransactionRepository;

    @Autowired
    private OrderRepo orderRepo;

    public HomeController(UserService userService1){
        this.userService=userService1;
    }
    @GetMapping("/")
    public String DisplayHomePage(){
        return "login.html";
    }

    @GetMapping("/dashBoard")
    public String DisplayDashBoardPage(){
        return "dashboard.html";
    }

    @ControllerAdvice
    public class globalCommon{
        @ModelAttribute("username")
        public String username(Authentication authentication){
            if(authentication!=null) {
                return authentication.getName();
            }
            else
                return "Not LogIn";
        }

        @ModelAttribute("newAdmins")
        public int newAdmins(){
            return userService.CountAdminUser();
        }

        @ModelAttribute("TotalSubscriptions")
        public int TotalSubscriptions(){
            int count=subscriptionRepo.findAll().size();
            return count;
        }

        @ModelAttribute("TotalCurrentStaff")
        public int TotalCurrentStaff(Authentication authentication){
            if(authentication!=null) {
                User user = userRepository.finddByUserName(authentication.getName());
                List<User> users = userRepository.findByParentUserID(user.getUserID());
                return users.size();
            }
            else return 0;
        }

        @ModelAttribute("chartData")
        public Map<String,Integer> chartData(){
            Map<String,Integer> data=new LinkedHashMap<String,Integer>();
            int juneCount=0,julyMonth=0;
            int[] months=new int[11];
            int[] countMonth=new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
            for (int i = 0; i< Arrays.stream(months).count(); i++){
                months[i]=0;
            }
            List<Order> order=orderRepo.findAll();
            for (Order orderLoop:
                 order) {
                for(int i=0;i<Arrays.stream(countMonth).count();i++){
                    if(orderLoop.getCreatedAt().getMonthValue()==countMonth[i]){
                        months[i]++;
                    }
                }
//                    if(orderLoop.getCreatedAt().getMonthValue()==countMonth[0]){
//                        months[0]++;
//                    }
//                    else if(orderLoop.getCreatedAt().getDayOfMonth()==7){
//                        julyMonth++;
//                    }
                data.put("JANUARY",months[0]);
                data.put("FEBRUARY",months[1]);
                data.put("MARCH",months[2]);
                data.put("APRIL",months[3]);
                data.put("MAY",months[4]);
                    data.put("JUNE",months[5]);
                    data.put("JULY",months[6]);
                data.put("AUGUST",months[7]);
//                data.put("SEPETEMBER",months[8]);
//                data.put("OCTOBER",months[9]);
//                data.put("NOVEMBER",months[10]);
//                data.put("DECEMBER",months[11]);
            }
            return data;
        }

        @ModelAttribute("lineData")
        public Map<String,Integer> lineData(){
            Map<String,Integer> lineData=new LinkedHashMap<String,Integer>();
            String[] Dates=new String[10];
            int[] amount=new int[10];
            for(int i=0;i<10;i++){
                amount[i]=0;
                Dates[i]="Something";
            }
            List<Order> productTransactions=orderRepo.findWithOrder();
                for (Order products :
                        productTransactions) {
                    one :
                    {
                        for (int i = 0; i < productTransactions.size(); i++) {
                            for (int j = 0; j <= i; j++) {
                                if (products.getCreatedAt().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).equals(Dates[j])) {
                                    amount[j] += (int) Math.floor(products.getTotalPrice());
                                    j++;
                                } else {
                                    Dates[i] = products.getCreatedAt().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
                                    amount[i] = (int) Math.floor(products.getTotalPrice());
                                }
                                break one;
                            }

//                    for(int j=0;j<10;j++) {
//                        if (products.getCreatedAt().toString().equals(Dates[j])) {
//                            amount[j] += products.getTotalPrice();
//                        }
//                    }
                        }
                    }
                    lineData.put(Dates[0], amount[0]);
                    lineData.put(Dates[1], amount[1]);
                    lineData.put(Dates[2], amount[2]);
                    lineData.put(Dates[3], amount[3]);
                    lineData.put(Dates[4], amount[4]);
                    lineData.put(Dates[5],amount[5]);
                    lineData.put(Dates[6],amount[6]);
                    lineData.put(Dates[7],amount[7]);
                }
            return lineData;
        }

        @ModelAttribute("TotalSalesOfToday")
        public float TotalSalesOfToday(){
            float amount=0.0F;
            List<Order> orders=orderRepo.findAll();
            for (Order o:
                 orders) {
                if(o.getCreatedAt().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).equals(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))){
                    amount+=o.getTotalPrice();
                }
                else amount=0.0F;
            }
            return amount;
        }
    }

    @ControllerAdvice
    public class globalBackGroundColor{
        @ModelAttribute("bgColor")
        public String setBGColor(){
            return InventoryManagementConstant.BGCOLOR;
        }
    }

}
