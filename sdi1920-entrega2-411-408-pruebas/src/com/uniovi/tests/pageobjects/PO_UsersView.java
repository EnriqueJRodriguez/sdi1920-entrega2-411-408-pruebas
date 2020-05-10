package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_UsersView {
	
	static public void fillForm(WebDriver driver, String busqueda) {
		WebElement email = driver.findElement(By.name("busqueda"));
		email.click();
		email.clear();
		email.sendKeys(busqueda);
		By boton = By.className("btn");
		driver.findElement(boton).click();	
	}
}
