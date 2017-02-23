package ru.vyukov.anotherliverefresh.sampleapp;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class SecurityDemoApplication {

	private Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(SecurityDemoApplication.class, args);
	}

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("rnd", random.nextInt());

		return "index";
	}

}
