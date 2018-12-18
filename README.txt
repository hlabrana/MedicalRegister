* INTEGRANTES:
	Hector Labraña - 201373535-5
	David Rojas - 201573502-6

* DATOS UTILES:
	Maquinas virtuales:
		ssh root@dist29.inf.santiago.usm.cl   PASSWORD: fedorapass
		ssh root@dist30.inf.santiago.usm.cl   PASSWORD: fedorapass
		ssh root@dist31.inf.santiago.usm.cl   PASSWORD: fedorapass
		ssh root@dist32.inf.santiago.usm.cl   PASSWORD: fedorapass

	Comandos:
		Limpiar procesos JAVA: killall -9 java
		Limpiar Temporales: make clean
		Compilar: make		
		Ejecutar: make run

* INSTRUCCIONES DE EJECUCION:

	1. Verificar que se encuentra instalado la version de JAVA JDK8
	3. Ingresar a cada maquina virtual por ssh
	4. Ingresar a la ruta root/MedicalRegister
	5. Compilar con comando make
	4. Ejecutar mediante el comando "make run" (SIN COMILLAS)
	5. Ingresar IP de cada maquina
	6. Sincronizar la ejecución presionando ENTER en label 'INICIAR:'
	5. Obsevar registro los en Operaciones.log
	6. Para eliminar archivos temporales y de compilacion ejecutar "make clean" (SIN COMILLAS)
