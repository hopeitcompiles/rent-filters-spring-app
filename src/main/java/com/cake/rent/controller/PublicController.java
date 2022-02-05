package com.cake.rent.controller;

import com.cake.rent.model.Client;
import com.cake.rent.model.Device;
import com.cake.rent.model.Rent;
import com.cake.rent.serviceImp.ClientService;
import com.cake.rent.serviceImp.DeviceService;
import com.cake.rent.serviceImp.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PublicController {
    private int maxElementPerPage=10;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ClientService clientService;




    @GetMapping("api/devices")
    @ResponseBody
    public List<Device> fecthDevices(){
        return deviceService.getAll();
    }
    @GetMapping("devices")
    public String devices(@RequestParam Map<String, Object> params, Model model){
        int page=params.get("page")!=null? Integer.valueOf(params.get("page").toString())-1:0;
//      número de la página y cantidad de elementos por página
        PageRequest pageRequest=PageRequest.of(page,maxElementPerPage);
        Page<Device> devicePage=deviceService.getAll(pageRequest);
//        para crear botones de navegación entre páginas
        int totalPages=devicePage.getTotalPages();
        if(totalPages>0){
            List<Integer> pages= IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages",pages) ;
            model.addAttribute("current",page+1);
        }
        model.addAttribute("devices",devicePage);
        model.addAttribute("totalPages",totalPages);
        return "devices";
    }

    @GetMapping("clients")
    public String clients(@RequestParam Map<String, Object> params, Model model){
        int page=params.get("page")!=null? Integer.valueOf(params.get("page").toString())-1:0;
//      número de la página y cantidad de elementos por página
        PageRequest pageRequest=PageRequest.of(page,maxElementPerPage);
        Page<Client> clientPage=clientService.getAll(pageRequest);
//        para crear botones de navegación entre páginas
        int totalPages=clientPage.getTotalPages();
        if(totalPages>0){
            List<Integer> pages= IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages",pages) ;
            model.addAttribute("current",page+1);
        }
        model.addAttribute("clients",clientPage);
        model.addAttribute("totalPages",totalPages);
        return "clients";
    }

    @GetMapping("")
    public String home(){
        return "redirect:/rents";
    }




}
