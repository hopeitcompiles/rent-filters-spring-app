package com.cake.rent.controller;

import com.cake.rent.model.Device;
import com.cake.rent.model.Rent;
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
    private RentService rentService;

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

    @GetMapping("")
    public String home(){
        return "redirect:/rents";
    }

    @GetMapping("/rents")
    public String rents(@RequestParam Map<String, Object> params, Model model){
//        Si el número de la página es null, eso significa que está en la primera página
//        La paginación inicia desde la página 0, pero al usuario se le muestra 1, por lo que retornará page+1
        int page=params.get("page")!=null? Integer.valueOf(params.get("page").toString())-1:0;
//      número de la página y cantidad de elementos por página
        PageRequest pageRequest=PageRequest.of(page,maxElementPerPage);
        Page<Rent> rentPage=rentService.getAll(pageRequest);
//        para crear botones de navegación entre páginas
        int totalPages=rentPage.getTotalPages();
        if(totalPages>0){
            List<Integer> pages= IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages",pages) ;
            model.addAttribute("current",page+1);
        }
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("control",new Device());
        model.addAttribute("rents",rentPage) ;
        model.addAttribute("devices",deviceService.getAll());
        return "rents";
    }

    @GetMapping("/rents/filter")
    public String indexFilter(@RequestParam Map<String, Object> params, Model model, String start, String end, String returned, int deviceid) {
//        Si el número de la página es null, eso significa que está en la primera página
//        La paginación inicia desde la página 0, pero al usuario se le muestra 1, por lo que retornará page+1
        int page=params.get("page")!=null? Integer.valueOf(params.get("page").toString())-1:0;
//      número de la página y cantidad de elementos por página
        PageRequest pageRequest=PageRequest.of(page,maxElementPerPage);

//        filters
        LocalDate datestart=null;
        LocalDate dateend=null;
        if(!start.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            datestart = LocalDate.parse(start, formatter);
            if(!end.isEmpty()){
                dateend = LocalDate.parse(end, formatter);
            }else{
                dateend=LocalDate.now();
            }
            if(datestart.isAfter(dateend)){
                LocalDate temp=datestart;
                datestart=dateend;
                dateend=temp;
            }
        }

        if(returned.compareTo("all")==0){
            returned=null;
        }
        Page<Rent> pageRents=rentService.getFiltered(pageRequest,datestart,dateend,returned,Long.valueOf(deviceid));
        int totalPages=pageRents.getTotalPages();
        if(totalPages>0){
            List<Integer> pages= IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages",pages) ;
            model.addAttribute("current",page+1);
        }
        model.addAttribute("deviceid",deviceid);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("returned",returned);
        model.addAttribute("start",datestart);
        model.addAttribute("end",dateend);
        model.addAttribute("rents",pageRents) ;
        model.addAttribute("devices",deviceService.getAll());
        return "rents";
    }
}
