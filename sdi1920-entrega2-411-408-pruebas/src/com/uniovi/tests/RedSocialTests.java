package com.uniovi.tests;
//Paquetes Java
import java.util.List;
//Paquetes JUnit 
import org.junit.*;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertTrue;
//Paquetes Selenium 
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
//Paquetes Utilidades de Testing Propias
import com.uniovi.tests.util.SeleniumUtils;
//Paquetes con los Page Object
import com.uniovi.tests.pageobjects.*;


//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class RedSocialTests {
	
	//Activar Enrique (Windows)
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String GeckDriver024 = "C:\\Users\\EnriqueJRodriguez\\Downloads\\OneDrive_2020-03-02\\PL-SDI-Ses5-material\\geckodriver024win64.exe";
	
	//En MACOSX (Debe ser la versión 65.0.1 y desactivar las actualizacioens automáticas):
	//static String PathFirefox65 = "/Applications/Firefox 2.app/Contents/MacOS/firefox-bin";
	//static String PathFirefox64 = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
	//static String Geckdriver024 = "/Users/delacal/Documents/SDI1718/firefox/geckodriver024mac";
	//static String Geckdriver022 = "/Users/delacal/Documents/SDI1718/firefox/geckodriver023mac";
	//Común a Windows y a MACOSX
	static WebDriver driver = getDriver(PathFirefox65, GeckDriver024); 
	static String URL = "http://localhost:8081";


	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}


	@Before
	public void setUp(){
		driver.navigate().to(URL);
	}
	@After
	public void tearDown(){
		driver.manage().deleteAllCookies();
	}
	@BeforeClass 
	static public void begin() {
		//COnfiguramos las pruebas.
		//Fijamos el timeout en cada opción de carga de una vista. 2 segundos.
		PO_View.setTimeout(3);
		driver.navigate().to(URL);
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		PO_LoginView.fillForm(driver, "Enrique@sdiuniovi.es", "123456");
		PO_PrivateView.checkElement(driver, "text", "Enrique@sdiuniovi.es");
		driver.navigate().to(URL+"/eliminartodo");

	}
	@AfterClass
	static public void end() {
		//Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	//W1_01. [Prueba1] Registro de Usuario con datos válidos.
	@Test
	public void PR01() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "marcos@sdiuniovi.es", "Marcos", "Rupertez", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		// Nos desconectamos
		PO_PrivateView.logout(driver);		
	}

	//W1_02. [Prueba2] Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos)
	@Test
	public void PR02() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "", "", "", "123456", "123456");
		// Comprobamos que nos salta un error en los campos del formulario
		PO_PrivateView.checkElement(driver, "text", "No puede haber campos vacios");			
	}

	//W1_03. [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
	@Test
	public void PR03() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "marcos@sdiuniovi.es", "Marcos", "Rupertez", "123456", "123456789");
		// Comprobamos que nos salta un error en los campos del formulario
		PO_PrivateView.checkElement(driver, "text", "Las contraseñas no coinciden");			
	}
	
	//W1_04. [Prueba4] Registro de Usuario con datos inválidos (email existente).
	@Test
	public void PR04() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "marcos@sdiuniovi.es", "Marcos", "Rupertez", "123456", "123456");
		// Comprobamos que nos salta un error en los campos del formulario
		PO_PrivateView.checkElement(driver, "text", "Email ya en uso por el sistema");			
	}
	
	//W2_05. [Prueba5] Inicio de sesión con datos válidos (usuario estándar).
	@Test
	public void PR05() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		// Nos desconectamos
		PO_PrivateView.logout(driver);			
	}
	
	//W2_06. [Prueba6] Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
	@Test
	public void PR06() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "");
		// Comprobamos que seguimos en la pagina de logueo
		PO_PrivateView.checkElement(driver, "text", "No puede haber campos vacios");
	}
	
	//W2_07. [Prueba7] Inicio de sesión con datos inválidos (usuario estándar, email existente, pero contraseña
	//				   incorrecta).
	@Test
	public void PR07() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456AAAAAAAA");
		// Comprobamos que seguimos en la pagina de logueo
		PO_PrivateView.checkElement(driver, "text", "Usuario o contraseñas incorrectos");			
	}	
	
	//W2_08. [Prueba8] Inicio de sesión con datos inválidos (usuario estándar, email no existente y contraseña no vacía).
	@Test
	public void PR08() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "paquito@sdiuniovi.es", "123456");
		// Comprobamos que seguimos en la pagina de logueo
		PO_PrivateView.checkElement(driver, "text", "Usuario o contraseñas incorrectos");				
	}	
	
	//W3_09. [Prueba9] Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de inicio de
	//                sesión (Login).
	@Test
	public void PR09() {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		// Nos desconectamos (incluye comprobación).
		PO_PrivateView.logout(driver);					
	}	
	//W3_10. [Prueba10] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
	@Test
	public void PR10() {
		// Comprobamos que está la opción de identificarse
		PO_HomeView.checkElement(driver, "text", "Identificate");
		// Comprobamos que no está la opción de desconectarse
		SeleniumUtils.textoNoPresentePagina(driver, "Desconectarse");
	}	
	
	//W4_11. [Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema. 
	@Test
	public void PR11() {
		//Añadimos un nuevo usuario a la base de datos
		
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "maria@sdiuniovi.es", "Maria", "Perez", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");
		// Nos desconectamos
		PO_PrivateView.logout(driver);
		
		
		// Nos conectamos como el otro usuario
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
      PO_PrivateView.logout(driver);	
	}	
	
	//W5_12. [Prueba12] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
	//      corresponde con el listado usuarios existentes en el sistema.
	@Test
	public void PR12() {
		// Registramos otro usuario
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "carlos@sdiuniovi.es", "Carlos", "Martinez", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_PrivateView.checkElement(driver, "text", "carlos@sdiuniovi.es");
		// Nos desconectamos
		PO_PrivateView.logout(driver);
		
		// Nos conectamos como el otro usuario
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		// Buscamos con el campo vacío
		PO_UsersView.fillForm(driver, "");
		//Se muestran los otros dos usuarios y la página
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "carlos@sdiuniovi.es");
		//Pagina correcta
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "page-link", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		PO_PrivateView.logout(driver);
	}	
	
	//W5_13. [Prueba13] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
	//                  muestra la página que corresponde, con la lista de usuarios vacía.
	@Test
	public void PR13() {
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");	
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		//Buscamos a RomeoSantos
		PO_UsersView.fillForm(driver, "RomeoSantos");
		//No se muestran los otros dos usuarios y la página
		SeleniumUtils.textoNoPresentePagina(driver, "maria@sdiuniovi.es");
		SeleniumUtils.textoNoPresentePagina(driver, "carlos@sdiuniovi.es");
		PO_PrivateView.logout(driver);
	}	
	
	//W5_14. Hacer una búsqueda con un texto específico y comprobar que se muestra la página que
	//      corresponde, con la lista de usuarios en los que el texto especificados sea parte 
	//      de su nombre, apellidos o de su email.
	@Test
	public void PR14() {
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");	
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		//Buscamos a Maria
		PO_UsersView.fillForm(driver, "ia");
		SeleniumUtils.textoPresentePagina(driver, "maria@sdiuniovi.es");
		SeleniumUtils.textoNoPresentePagina(driver, "carlos@sdiuniovi.es");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "page-link", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		PO_PrivateView.logout(driver);
	}	
	
	//W5_12. [Prueba15] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario.
	//                  Comprobar que la solicitud de amistad aparece en el listado de invitaciones (punto siguiente). 
	@Test
	public void PR15() {
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");	
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		// Agregamos al primero que se pueda agregar
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "btn btn-info", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		elementos.get(0).click();
		// Comprobamos que se haya agregado
		SeleniumUtils.textoPresentePagina(driver, "Se ha tramitado con exito su invitacion");
		SeleniumUtils.textoPresentePagina(driver, "Petición de amistad pendiente de aprobación");
		PO_PrivateView.logout(driver);
	}	
	
	//W6_16. [Prueba16] Desde el listado de usuarios de la aplicación, enviar una invitación de amistad a un usuario al
	//                 que ya le habíamos enviado la invitación previamente. No debería dejarnos enviar la invitación, se podría
	//                 ocultar el botón de enviar invitación o notificar que ya había sido enviada previamente
	@Test
	public void PR16() {
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");	
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		//Comprobamos que no se puede agregar
		SeleniumUtils.textoPresentePagina(driver, "Petición de amistad pendiente de aprobación");
		PO_PrivateView.logout(driver);
	}	
	
	//W7_17. [Prueba17] Mostrar el listado de invitaciones de amistad recibidas. Comprobar con un listado que
	//       contenga varias invitaciones recibidas
	@Test
	public void PR17() {
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");	
		//Accedemos a la lista de invitaciones
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "invitations-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "invitation/list", "text", "marcos@sdiuniovi.es");
		PO_PrivateView.logout(driver);
	}	
	
	//W8_18. [Prueba18] Sobre el listado de invitaciones recibidas. Hacer click en el botón/enlace de una de ellas y
	//                  comprobar que dicha solicitud desaparece del listado de invitaciones.
	@Test
	public void PR18() {
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");	
		//Accedemos a la lista de invitaciones
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "invitations-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "invitation/list", "text", "marcos@sdiuniovi.es");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "btn btn-info", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		elementos.get(0).click();
		SeleniumUtils.textoPresentePagina(driver, "Se ha tramitado con exito su aceptación de amigo");
		PO_PrivateView.logout(driver);
	}	
	
	//W9_19. [Prueba19] Mostrar el listado de amigos de un usuario. Comprobar que el listado contiene los amigos que
	//                 deben ser.
	@Test
	public void PR19() {
		// Nos logueamos como maria
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");	
		//Accedemos a la lista de amigos
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "friends-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "friend/list", "text", "marcos@sdiuniovi.es");
		// Nos desconectamos
		PO_PrivateView.logout(driver);
		
		// Nos logueamos como marcos
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "marcos@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de amigos
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		//Accedemos a la lista de usuarios
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "friends-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "friend/list", "text", "maria@sdiuniovi.es");
		// Nos desconectamos
		PO_PrivateView.logout(driver);
	}	
	
	//W10_20. [Prueba20] Intentar acceder sin estar autenticado a la opción de listado de usuarios. Se deberá volver al
	//                   formulario de login.
	@Test
	public void PR20() {
		// Intentamos acceder al listado de usuarios
		driver.get(URL+"/user/list");
		// Comprobamos que estamos en el login
		SeleniumUtils.textoPresentePagina(driver, "Identificación de usuario");			
	}	
	
	//W10_21. [Prueba21] Intentar acceder sin estar autenticado a la opción de listado de invitaciones de amistad recibida
	//                   de un usuario estándar. Se deberá volver al formulario de login.
	@Test
	public void PR21() {
		driver.get(URL+"/invitation/list");
		// Comprobamos que estamos en el login
		SeleniumUtils.textoPresentePagina(driver, "Identificación de usuario");				
	}	
	
	//W10_22. [Prueba22] Intentar acceder estando autenticado como usuario standard a la lista de amigos de otro
	//                   usuario. Se deberá mostrar un mensaje de acción indebida.
	@Test
	public void PR22() {
		driver.get(URL+"/friend/list");
		// Comprobamos que estamos en el login
		SeleniumUtils.textoPresentePagina(driver, "Identificación de usuario");				
	}	
	
	//WC1_23. [Prueba23] Inicio de sesión con datos válidos.
	@Test
	public void PR23() {
		// Nos dirigimos al formulario de login
		driver.get(URL+"/cliente.html");
		PO_PrivateView.checkElement(driver, "text", "Email:");
		// utilizamos credenciales validas
		PO_LoginApi.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que podemos ver sus amigos
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		// Eliminamos las galletas
		driver.manage().deleteAllCookies();
	}	
	
	//WC1_24. [Prueba24] Inicio de sesión con datos inválidos (usuario no existente en la aplicación).
	@Test
	public void PR24() {
		// Nos dirigimos al formulario de login
		driver.get(URL+"/cliente.html");
		PO_PrivateView.checkElement(driver, "text", "Email:");
		// utilizamos credenciales validas
		PO_LoginApi.fillForm(driver, "RomeoSantos@sdiuniovi.es", "123456");
		// Comprobamos que podemos ver sus amigos
		PO_PrivateView.checkElement(driver, "text", "Datos incorrectos");			
	}
	
	//WC2_25. [Prueba25] Acceder a la lista de amigos de un usuario, que al menos tenga tres amigos.
	@Test
	public void PR25() {
		añadirNuevosAmigos();
		
		// Nos dirigimos al formulario de login
		driver.get(URL+"/cliente.html");
		PO_PrivateView.checkElement(driver, "text", "Email:");
		// utilizamos credenciales validas
		PO_LoginApi.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que podemos ver sus amigos
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "kike@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "hugo@sdiuniovi.es");
		// Eliminamos las galletas
		driver.manage().deleteAllCookies();
		
	}	
	
	


	//WC2_26. [Prueba26] Acceder a la lista de amigos de un usuario, y realizar un filtrado para encontrar a un amigo
	//                  concreto, el nombre a buscar debe coincidir con el de un amigo.
	@Test
	public void PR26() {
		// Nos dirigimos al formulario de login
		driver.get(URL+"/cliente.html");
		PO_PrivateView.checkElement(driver, "text", "Email:");
		// Utilizamos credenciales validas
		PO_LoginApi.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que podemos ver sus amigos
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "kike@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "hugo@sdiuniovi.es");
		
		// Rellenamos el formulario y buscamos
		WebElement filtro = driver.findElement(By.id("filtro-nombre"));
		filtro.click();
		filtro.clear();
		filtro.sendKeys("Kike");
		By boton = By.className("btn");
		driver.findElement(boton).click();
		PO_PrivateView.checkElement(driver, "text", "kike@sdiuniovi.es");
		// Eliminamos las galletas
		driver.manage().deleteAllCookies();			
	}	
	
	//WC3_27. [Prueba27] Acceder a la lista de mensajes de un amigo “chat”, la lista debe contener al menos tres
	//                  mensajes.
	@Test
	public void PR27() {
		// Nos dirigimos al formulario de login
		driver.get(URL+"/cliente.html");
		PO_PrivateView.checkElement(driver, "text", "Email:");
		// Utilizamos credenciales validas
		PO_LoginApi.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que podemos ver sus amigos
		PO_PrivateView.checkElement(driver, "text", "marcos@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "kike@sdiuniovi.es");
		PO_PrivateView.checkElement(driver, "text", "hugo@sdiuniovi.es");
				
		// Rellenamos el formulario y buscamos
		WebElement filtro = driver.findElement(By.id("filtro-nombre"));
		filtro.click();
		filtro.clear();
		filtro.sendKeys("Kike");
		By boton = By.className("btn");
		driver.findElement(boton).click();
		PO_PrivateView.checkElement(driver, "text", "kike@sdiuniovi.es");
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "/html/body/div[2]/div/table/tbody/tr[1]/td[4]/a", PO_View.getTimeout());
		elementos.get(0).click();
		
		// Enviamos unos mensajes de prueba
		enviarMensajes();
		
		// Comprobamos los textos
		PO_PrivateView.checkElement(driver, "text", "Kike");
		PO_PrivateView.checkElement(driver, "text", "¿Que tal?");
		PO_PrivateView.checkElement(driver, "text", "Necesito ayuda con una practica...");
		
		// Eliminamos las galletas
		driver.manage().deleteAllCookies();
	}	
	
	


	//WC4_28. [Prueba28] Acceder a la lista de mensajes de un amigo “chat” y crear un nuevo mensaje, validar que el
	//                  mensaje aparece en la lista de mensajes.
	@Test
	public void PR28() {
		// Nos dirigimos al formulario de login
		driver.get(URL+"/cliente.html");
		PO_PrivateView.checkElement(driver, "text", "Email:");
		// Utilizamos credenciales validas
		PO_LoginApi.fillForm(driver, "kike@sdiuniovi.es", "123456");
		// Vemos que carga maria como amiga
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");
		
		// Buscamos para ser mas concisos y abrimos el chat
		WebElement filtro = driver.findElement(By.id("filtro-nombre"));
		filtro.click();
		filtro.clear();
		filtro.sendKeys("Maria");
		By boton = By.className("btn");
		driver.findElement(boton).click();
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "/html/body/div[2]/div/table/tbody/tr[1]/td[4]/a", PO_View.getTimeout());
		elementos.get(0).click();
		
		// Comprobamos los textos
		PO_PrivateView.checkElement(driver, "text", "Kike");
		PO_PrivateView.checkElement(driver, "text", "¿Que tal?");
		PO_PrivateView.checkElement(driver, "text", "Necesito ayuda con una practica...");
		
		// Escribimos respuesta
		WebElement send = driver.findElement(By.id("agregar-texto"));
		send.click();
		send.clear();
		send.sendKeys("Vale, Maria a ver, ¿Que ocurre esta vez?");
		boton = By.id("boton-agregar");
		driver.findElement(boton).click();
		
		// Comprobamos el texto
		PO_PrivateView.checkElement(driver, "text", "Vale, Maria a ver, ¿Que ocurre esta vez?");
	}
//
//	//PR030. Sin hacer /
//	@Test
//	public void PR30() {
//		assertTrue("PR30 sin hacer", false);			
//	}
//	
//	//PR031. Sin hacer /
//	@Test
//	public void PR31() {
//		assertTrue("PR31 sin hacer", false);			
//	}
//	
	
	private void añadirNuevosAmigos() {
		// Amigo nuevo de Maria 1
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "kike@sdiuniovi.es", "Kike", "Rodriguez", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_PrivateView.checkElement(driver, "text", "kike@sdiuniovi.es");
		//Accedemos a la lista de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		//Buscamos a RomeoSantos
		PO_UsersView.fillForm(driver, "maria");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "btn btn-info", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		elementos.get(0).click();
		PO_PrivateView.logout(driver);
		
		// Amigo nuevo de Maria 2
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "registrarse", "text", "Apellidos:");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "hugo@sdiuniovi.es", "Hugo", "Fonseca", "123456", "123456");
		// Comprobamos que entramos en la sección privada
		PO_PrivateView.checkElement(driver, "text", "hugo@sdiuniovi.es");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "users-menu", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "text", "maria@sdiuniovi.es");
		//Buscamos a RomeoSantos
		PO_UsersView.fillForm(driver, "maria");
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "btn btn-info", PO_View.getTimeout());
		//Tiene que haber un sólo elemento.
		elementos.get(0).click();
		PO_PrivateView.logout(driver);
		
		PO_HomeView.clickOption(driver, "identificarse", "text", "Email:");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "maria@sdiuniovi.es", "123456");
		// Comprobamos que entramos en la pagina privada de usuarios
		PO_PrivateView.checkElement(driver, "text", "maria@sdiuniovi.es");	
		//Accedemos a la lista de invitaciones
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "invitations-menu", PO_View.getTimeout());
		assertTrue(elementos.size()==1);
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "invitation/list", "text", "kike@sdiuniovi.es");
		//Aceptamos nuevos amigos.
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "btn btn-info", PO_View.getTimeout());
		elementos.get(0).click();
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "class", "btn btn-info", PO_View.getTimeout());
		elementos.get(0).click();
		PO_PrivateView.logout(driver);
	}
	
	private void enviarMensajes() {
		// Mensaje 1
		WebElement send = driver.findElement(By.id("agregar-texto"));
		send.click();
		send.clear();
		send.sendKeys("Kike");
		By boton = By.id("boton-agregar");
		driver.findElement(boton).click();
		// Mensaje 2
		send = driver.findElement(By.id("agregar-texto"));
		send.click();
		send.clear();
		send.sendKeys("¿Que tal?");
		boton = By.id("boton-agregar");
		driver.findElement(boton).click();
		// Mensaje 3
		send = driver.findElement(By.id("agregar-texto"));
		send.click();
		send.clear();
		send.sendKeys("Necesito ayuda con una practica...");
		boton = By.id("boton-agregar");
		driver.findElement(boton).click();
		
	}
}

