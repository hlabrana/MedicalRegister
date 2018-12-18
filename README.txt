* INTEGRANTES:
	Hector Labraña - 201373535-5
	David Rojas - 201573502-6

* DATOS UTILES:
	Maquinas virtuales:
		ssh root@dist29.inf.santiago.usm.cl   PASSWORD: fedorapass 	IP: 10.4.60.169
		ssh root@dist30.inf.santiago.usm.cl   PASSWORD: fedorapass 	IP: 10.4.60.170
		ssh root@dist31.inf.santiago.usm.cl   PASSWORD: fedorapass 	IP: 10.4.60.171
		ssh root@dist32.inf.santiago.usm.cl   PASSWORD: fedorapass 	IP: 10.4.60.172

	Comandos:
		Limpiar procesos JAVA: killall -9 java
		Limpiar Temporales: make clean
		Compilar: make		
		Ejecutar: make run

* INSTRUCCIONES DE EJECUCION:

	1. Verificar que se encuentra instalado la version de JAVA JDK8
	2. Ingresar a cada maquina virtual por ssh
	3. Ingresar a la ruta root/MedicalRegister
	4. Compilar con comando make
	5. Ejecutar mediante el comando "make run" (SIN COMILLAS)
	6. Ingresar IP de cada maquina
	7. Sincronizar la ejecución presionando ENTER en label 'INICIAR:'
	8. Obsevar registro los en Operaciones.log
	9. Para eliminar archivos temporales y de compilacion ejecutar "make clean" (SIN COMILLAS)
