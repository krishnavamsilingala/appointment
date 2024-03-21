package com.agency.appointment;

import com.agency.appointment.entity.Operator;
import com.agency.appointment.service.ISlotService;
import com.agency.appointment.service.impl.OperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class AppointmentApplication implements ApplicationRunner {

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private ISlotService slotService;

	public static void main(String[] args) {
		SpringApplication.run(AppointmentApplication.class, args);
	}





	@Override
	public void run(ApplicationArguments args) throws Exception {
		preDataLoad();
		log.info("Predata Loaded");
	}

	private void preDataLoad() {
		// Your custom logic here
		List<Operator> operators=new ArrayList<>();
		operators.add(new Operator("ServiceOperator0"));
		operators.add(new Operator("ServiceOperator0"));
		operators.add(new Operator("ServiceOperator0"));

		operatorService.saveAll(operators);
		for (Operator operator : operators) {
			try {
				slotService.generateSlots(operator, 7);
			} catch (Exception ex){
				log.error("Exception:",ex);
			}
		}
		System.out.println("Executing custom method...");
	}
}
