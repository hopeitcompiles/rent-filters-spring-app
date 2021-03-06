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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class RentController {

    @Autowired
    private RentService rentService;
    @Autowired
    private DeviceService deviceService;

    private int maxElementPerPage=10;
    //for filter pagination
    String returned="all";
    Long deviceid=0L;
    LocalDate datestart=null;
    LocalDate dateend=null;
    Integer penalty=0;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
    public String indexFilter(@RequestParam Map<String, Object> params, Model model) {
//        Si el número de la página es null, eso significa que está en la primera página
//        La paginación inicia desde la página 0, pero al usuario se le muestra 1, por lo que retornará page+1
        int page=0;

        try {
            page=params.get("page")!=null? Integer.valueOf(params.get("page").toString())-1:0;
        }catch (Exception e){
            System.out.println(e);
        }
//      número de la página y cantidad de elementos por página
        PageRequest pageRequest=PageRequest.of(page,maxElementPerPage);

        if(params.get("returned")!=null){
            if(params.get("start")!=null) {
                if (params.get("start").toString().isEmpty()) {
                    datestart = null;
                    if(params.get("end")!=null)
                        dateend =!params.get("end").toString().isEmpty() ? LocalDate.parse(params.get("end").toString(), formatter):null;
                }
            }
        }
//        filters
        if(params.get("start")!=null) {
            if(!params.get("start").toString().isEmpty()) {
                datestart = LocalDate.parse(params.get("start").toString(), formatter);
                if (params.get("end")!=null) {
                    dateend =!params.get("end").toString().isEmpty()? LocalDate.parse(params.get("end").toString(), formatter):LocalDate.now();
                } else {
                    dateend = LocalDate.now();
                }
                if (datestart.isAfter(dateend)) {
                    LocalDate temp = datestart;
                    datestart = dateend;
                    dateend = temp;
                }
            }
        }
        //returned
        returned=params.get("returned")!=null? (params.get("returned").toString()):returned;
        if(returned!=null){
            if(returned.compareToIgnoreCase("all")==0){
                returned =null;
            }
        }
        try {
            deviceid = params.get("deviceid") != null ? Long.valueOf( params.get("deviceid").toString()): deviceid;
        }catch (Exception e){
            deviceid=null;
        }
        try {
            penalty=params.get("penalty")!=null? Integer.valueOf(params.get("penalty").toString()):penalty;
        }catch (Exception e){
            penalty=null;
        }

        Page<Rent> pageRents=rentService.getFiltered(pageRequest,datestart,dateend,returned,deviceid,penalty);

        int totalPages=pageRents.getTotalPages();
        if(totalPages>0){
            List<Integer> pages= IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pages",pages) ;
            model.addAttribute("current",page+1);
        }
        model.addAttribute("penalty",penalty);
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
