package com.cake.rent;

import com.cake.rent.model.Client;
import com.cake.rent.model.Device;
import com.cake.rent.model.Rent;
import com.cake.rent.serviceImp.ClientService;
import com.cake.rent.serviceImp.DeviceService;
import com.cake.rent.serviceImp.RentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class RentApplication {
	@Bean
	CommandLineRunner init(DeviceService deviceService, ClientService clientService, RentService rentService) {
		return args -> {
			List<Device> devices=new ArrayList<>();
			String deviceNames[]={"Xbox Series X",
					"Play Station 5",
					"Nintendo Switch",
					"Chinese Smartwatch",
					"Oculus Quest 2",
					"Oculus Rift S",
					"HTC Vive Pro 2",
					"Valve Index VR",
					"Nvidia RTX 3050",
					"Radeon RX6600 XT",
					"Valve Steam Deck",
					"PS VR2",
					"Apple Smart Glasses"
			};
			Device device;
			for (String name:deviceNames){
				device = deviceService.findByName(name);
				if(device==null) {
					device = new Device();
					device.setName(name);
					device.setStatusAvailable();
					deviceService.save(device);
				}
			}
			List <Client> clients=new ArrayList<>();
			String clientNames[]={"Armando Casas",
					"Zoyla Vaca",
					"Aitor Tilla",
					"Elba Surero",
					"Inés Tornudo",
					"Lola Mento",
					"Andrés Trozado"
			};
			Client client;
			for (String name:clientNames){
				String[] splited=name.split(" ");
				client = clientService.findByNameAndLastName(splited[0],splited[1]);
				if(client==null) {
					client=new Client();
					client.setName(splited[0]);
					client.setLastName(splited[1]);
					clientService.save(client);
				}
			}
			devices=deviceService.getAll();
			clients=clientService.getAll();
			Random rand = new Random();

			Rent rent=new Rent();

			for (int i = 0; i < devices.size() + clients.size()+100; i++) {
				rent.setId(null);
				rent.setStartDate(LocalDate.now().minus(Period.ofDays(rand.nextInt(60))));
				rent.setEndDateByDurationDays(30);
				if (rand.nextInt(60) < 20) {
					rent.setReturnedDate(rent.getStartDate().plus(Period.ofDays(rand.nextInt(50))));
				}
				rent.setClient(clients.get(rand.nextInt(clients.size())));
				rent.setDevice(devices.get(rand.nextInt(devices.size())));
				rentService.save(rent);
			}

		};

	}
	public static void main(String[] args) {
		SpringApplication.run(RentApplication.class, args);
	}
}
